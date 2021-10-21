package com.yum.ucp.modules.codegen.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.common.service.ServiceException;
import com.yum.ucp.common.utils.*;
import com.yum.ucp.modules.codegen.CommonErrorCode;
import com.yum.ucp.modules.codegen.dao.CodegenDao;
import com.yum.ucp.modules.codegen.dao.CodegenValueDao;
import com.yum.ucp.modules.codegen.entity.Codegen;
import com.yum.ucp.modules.codegen.entity.CodegenValue;

import com.yum.ucp.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class CodegenService extends CrudService<CodegenDao, Codegen>
{

    @Autowired
    private CodegenValueDao codegenValueDao;

    private final String REDIS_PREFIX = "CODE_GEN_";

    public Codegen loadCodegen(String systemCode, String category){
        String key = REDIS_PREFIX + systemCode + "_" + category;
        Codegen po = (Codegen) JedisUtils.getObject(key);
        if(po == null){
            po = dao.loadCodegen(systemCode,category);
            if(po!=null){
                JedisUtils.setObject(key,po,0);
            }
        }
        return po;
    }

    private final String REDIS_LOCK = "CODE_GEN_REDIS_LOCK_";

    /**
     * 获取发票提取码
     * @param size
     * @param userId
     * @return
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public List<String> genSmsGetCode( int size, String userId)
        throws ServiceException
    {
        List<String> codes = new ArrayList<String>();
        String uniqueKey = "sms";

        Jedis jedis = null;
        JedisLock redisLock = null;

        try {
            jedis = JedisUtils.getResource();
            redisLock = new JedisLock(jedis, REDIS_LOCK + uniqueKey, 3000, 5000);
            if (redisLock.acquire()) {
                CodegenValue codegenValue = codegenValueDao.getCodegenValue(uniqueKey,"1");

                Date date = new Date();
                String ymd = DateUtils.formatDate(date,"yyyyMMdd");
                boolean isNew = false;

                if(codegenValue == null){
                    isNew = true;
                    codegenValue = new CodegenValue();
                    codegenValue.setId(IdGen.uuid());
                    codegenValue.setVersion(1);
                    codegenValue.setCreateBy(new User(userId));
                    codegenValue.setCreateDate(date);
                    codegenValue.setUpdateBy(codegenValue.getCreateBy());
                    codegenValue.setUpdateDate(date);
                    codegenValue.setUniqueKey(uniqueKey);
                    codegenValue.setCodegenId("1");
                }else {
                    Date update = codegenValue.getUpdateDate();
                    if(DateUtils.isSameDay(update,date)){
                        int oldVersion = codegenValue.getVersion();
                        if(oldVersion>=9999){
                            logger.error("今日短信提取码以达上限9999，无法继续生成提取码");
                            throw new InterruptedException("今日短信提取码以达上限9999，无法继续生成提取码");
                        }
                    }else{
                        codegenValue.setVersion(1); // 不是同一天update,则恢复version版本
                    }
                }

                int version = 0;
                for (int i = 0; i < size; i++)
                {
                    version = codegenValue.getVersion()+i;
                    String code=ymd+digital4Bit(version);
                    if(!StringUtils.equals("-1",code)){
                        codes.add(ymd+digital4Bit(version));
                    }
                }
                codegenValue.setVersion(version);
                if(isNew){
                    codegenValueDao.insert(codegenValue);

                }else{
                    if(codegenValueDao.updateCodegenValue(codegenValue.getId(), "", codegenValue.getVersion()+1,userId,date) != 1){
                        throw new ServiceException(CommonErrorCode.SYSTEM_ERROR);
                    }
                }

                return codes;
            } else {
                throw new ServiceException(CommonErrorCode.TIME_OUT);
            }
        } catch (InterruptedException e) {
            throw new ServiceException(CommonErrorCode.SYSTEM_ERROR);
        } finally {
            if (redisLock != null) {
                try {
                    redisLock.release();// 则解锁
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (jedis != null) {
                try {
                    JedisUtils.returnResource(jedis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private static String digital4Bit(int version){
        String result ="-1";
        if(version<=9999 && version >=1){
            result = String.format("%04d", version);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(digital4Bit(1));
        System.out.println(digital4Bit(9999));
        System.out.println(digital4Bit(10000));
        System.out.println(digital4Bit(0));
    }

    private  boolean alnum(String category)
    {  
        if (StringUtils.isEmpty(category))
        {
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]+$");
        Matcher m = p.matcher(category);
        if (m.find()) {
            return true;
        }
        return false;
    }
    /**
     * see isExistedCodegen 根据category验证数据是否是重复数据
     */
    private boolean isExistedCodegen(Codegen codegen)
        throws ServiceException
    {
        String category = codegen.getCategory();
        int count = dao.count(category);
        if (count > 0)
        {
            return true;
        }
        return false;
    }
}
