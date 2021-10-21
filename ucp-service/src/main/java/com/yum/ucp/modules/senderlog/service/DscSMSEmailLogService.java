/**
 * Copyright &copy; 2012-2014
 * <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yum.ucp.modules.senderlog.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.common.utils.IdGen;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.utils.YumUtils;
import com.yum.ucp.modules.senderlog.dao.DscSMSEmailLogDao;
import com.yum.ucp.modules.senderlog.entity.DscSMSEmailLog;
import com.yum.ucp.modules.sys.dao.DictDao;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.utils.UserUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * case配置Service
 *
 * @author Edward.Luo
 * @version 2016-12-12
 */
@Service
@Transactional(readOnly = true)
public class DscSMSEmailLogService extends CrudService<DscSMSEmailLogDao, DscSMSEmailLog>
{
    @Autowired
    private DscSMSEmailLogDao dscSMSEmailLogDao;

    /**
     * 日志对象
     */
    public org.slf4j.Logger logger = LoggerFactory.getLogger(DscSMSEmailLogService.class);
    @Autowired
    private DictDao dictDao;

    public DscSMSEmailLog get(String id)
    {
        return super.get(id);
    }

    public List<DscSMSEmailLog> findList(DscSMSEmailLog dscCaseConfig)
    {
        return super.findList(dscCaseConfig);
    }

    public Page<DscSMSEmailLog> findPage(Page<DscSMSEmailLog> page, DscSMSEmailLog dscCaseConfig)
    {
        return super.findPage(page, dscCaseConfig);
    }

    @Transactional(readOnly = false)
    public void save(DscSMSEmailLog dscCaseConfig)
    {
        super.save(dscCaseConfig);
    }

    @Transactional(readOnly = false)
    public void delete(DscSMSEmailLog dscCaseConfig)
    {
        super.delete(dscCaseConfig);
    }

    /**
     * 发送短信
     *
     * @param brand
     * @param phoneNum
     * @param code
     * @param key
     * @param restClient
     *
     * @return
     */
    public String sendInvoiceSms(String brand, String phoneNum, String code, String key, JiraRestClient restClient)
    {
        Dict smsDict = dictDao.getByValue(Global.SMS_TEMPLATE);
        List<Dict> dicts = dictDao.findByParentIdAndValue(smsDict.getId(), brand);
        if (dicts == null || dicts.isEmpty())
        {
            logger.warn("sms send fail, no sms template");
            return Global.SEND_FAILED;
        }
        Dict brandDict = dicts.get(0);
        dicts = dictDao.findByParentId(brandDict.getId());
        if (dicts == null || dicts.isEmpty() || dicts.size() < 3)
        {
            logger.info("sms send fail, no sms template content is missing, brand:{}", brand);
            return Global.SEND_FAILED;
        }
        String sn = null, psw = null, smscontent = null;
        for (Dict dict : dicts)
        {
            if (StringUtils.equals(Global.SMS_CONTENT, dict.getLabel()))
            {
                smscontent = dict.getValue();
                continue;
            }
            if (StringUtils.equals(Global.SMS_PSW, dict.getLabel()))
            {
                psw = dict.getValue();
                continue;
            }
            if (StringUtils.equals(Global.SMS_SN, dict.getLabel()))
            {
                sn = dict.getValue();
            }
        }
        logger.info("短信发送号码：{}，品牌：{}，内容：{}", phoneNum, brand, smscontent);
        if (StringUtils.isAnyEmpty(sn, psw, smscontent))
        {
            logger.info("sms send fail, sending parameter missing.");
            return Global.SEND_FAILED;
        }

        smscontent = StringUtils.replace(smscontent, Global.CODE_MASK, code);

        String result = YumUtils.initUtils().sendMsg(phoneNum, smscontent, sn, psw);
        switch (result)
        {
            case Global.SUCCESS:
                result = Global.SUCCESS;
                logger.info("短信发送成功");
                break;
            case Global.SEND_FAILED:
                result = Global.SEND_FAILED;
                logger.warn("短信发送失败");
                break;
            default:
                logger.warn("短信发送失败:{}", result);
                break;
        }
        insertSMSLog(phoneNum, smscontent, brand, result, key, restClient);
        return result;
    }

    private void insertSMSLog(String phone, String content, String brand, String result, String key, JiraRestClient restClient)
    {
        DscSMSEmailLog dscSMSEmailLog = new DscSMSEmailLog();
        dscSMSEmailLog.preInsert();
        dscSMSEmailLog.setIsNewRecord(true);
        dscSMSEmailLog.setBrand(brand);
        dscSMSEmailLog.setContent(content);
        dscSMSEmailLog.setTarget(phone);
        dscSMSEmailLog.setType("0");
        dscSMSEmailLog.setRemarks(key);
        String msg = "";
        String status = "0";
        switch (result)
        {
            case Global.SUCCESS:
                status = "1";
                msg = "发送成功".intern();
                break;
            case Global.SEND_FAILED:
                break;
            default:
                dscSMSEmailLog.setErrorMessage(result);
                msg = result;
                break;
        }
        dscSMSEmailLog.setStatus(status);
        save(dscSMSEmailLog);

        /**
         * 写到jira的备注里
         */
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        StringBuilder sb = new StringBuilder();
        sb.append(phone).append(" : ").append(content).append(" : ").append(msg);
        Comment com = Comment.valueOf(sb.toString());
        restClient.getIssueClient().addComment(issue.getCommentsUri(), com);
    }


    /**
     * 短信日志
     * @param phone
     * @param content
     * @param brand
     * @param result
     * @param flag
     */
    public void insertSMSLog(String phone, String content, String brand, String result, String flag) {
        DscSMSEmailLog dscSMSEmailLog = new DscSMSEmailLog();
        Date now = new Date();
        //初始化
        if(Global.JOB.equals(flag)){
            //Job任务
            User user = UserUtils.get("1");
            dscSMSEmailLog.setId(IdGen.uuid());
            dscSMSEmailLog.setCreateBy(user);
            dscSMSEmailLog.setCreateDate(now);
            dscSMSEmailLog.setUpdateBy(user);
            dscSMSEmailLog.setUpdateDate(now);
        }else{
            dscSMSEmailLog.preInsert();
        }

        dscSMSEmailLog.setIsNewRecord(true);
        dscSMSEmailLog.setBrand(brand);
        dscSMSEmailLog.setContent(content);
        dscSMSEmailLog.setTarget(phone);
        dscSMSEmailLog.setType("0");
        switch (result) {
            case Global.SUCCESS:
                dscSMSEmailLog.setStatus("1");
                break;
            default:
                dscSMSEmailLog.setStatus("0");
                dscSMSEmailLog.setErrorMessage(result);
                break;
        }
        dscSMSEmailLogDao.insert(dscSMSEmailLog);
    }
}
