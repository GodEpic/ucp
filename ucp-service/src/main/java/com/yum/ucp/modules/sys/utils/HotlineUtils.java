package com.yum.ucp.modules.sys.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.mapper.JsonMapper;
import com.yum.ucp.common.utils.CacheUtils;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.sys.dao.DscHotLineDao;
import com.yum.ucp.modules.sys.entity.DscHotLine;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 热线工具类
 */
public class HotlineUtils {

    private static DscHotLineDao hotLineDao = SpringContextHolder.getBean(DscHotLineDao.class);

    public static final String CACHE_HOT_LINE_LIST = "hotLineList";

    public static String getHotLineLabel(String phoneNum, String defaultValue){
        if (StringUtils.isNotBlank(phoneNum)){
            for (DscHotLine hotLine : getHotLineList()){
                if (phoneNum.equals(hotLine.getPhoneNum())){
                    return hotLine.getRemarks();
                }
            }
        }
        return defaultValue;
    }

    public static String getHotLineLabels(String values, String type, String defaultValue){
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
            List<String> valueList = Lists.newArrayList();
            for (String value : StringUtils.split(values, ",")){
                valueList.add(getHotLineLabel(value, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static List<DscHotLine> getHotLineList(){

        List<DscHotLine> hotLinList = (List<DscHotLine>)CacheUtils.get(CACHE_HOT_LINE_LIST);
        if (hotLinList == null){
            hotLinList = hotLineDao.findAllList(new DscHotLine());
            CacheUtils.put(CACHE_HOT_LINE_LIST, hotLinList);
        }
        return hotLinList;
    }

    public  static  String getHotLineListJson(){
        return JsonMapper.toJsonString(getHotLineList());
    }
}
