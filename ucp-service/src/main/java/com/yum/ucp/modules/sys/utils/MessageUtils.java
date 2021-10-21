package com.yum.ucp.modules.sys.utils;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.domain.util.ErrorCollection;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 异常解析
 */
public class MessageUtils {

    public static String date_error = "Error parsing date string";

    public static String field_not_exist = "cannot be set";

    public static String field_is_required = "is required";

    public static String summary_is_specify = "You must specify a summary of the issue";


    /**
     * status:400
     *
     * @return
     */
    public static String returnErrors(Collection<ErrorCollection> errorCollections, Map<String, String> maps) {
        System.out.println("jira 异常信息" + JSONObject.toJSON(errorCollections).toString());
        ErrorCollection errorCollection = Iterators.getOnlyElement(errorCollections.iterator());
        Map<String, String> errors = errorCollection.getErrors();
        StringBuilder stringBuffer = new StringBuilder();
        String str = "";
        int index = 0;
        boolean flag = false;
        for (Map.Entry<String, String> map : errors.entrySet()) {
            flag = true;
//            index++;
//            for (Map.Entry<String, String> m : maps.entrySet()) {
//                if (map.getKey().equals(m.getKey())) {
//                    if (map.getValue().trim().contains(field_not_exist.trim())) {
//                        str = "不存在";
//                    } else if (map.getValue().trim().contains(date_error.trim())) {
//                        str = "时间格式不正确";
//                    } else if (map.getValue().trim().contains(field_is_required.trim())) {
//                        str = "必填";
//                    } else if (map.getValue().trim().contains(summary_is_specify.trim())) {
//                        str = "摘要必须";
//                    }else{
//                        str = map.getValue();
//                        stringBuffer.append(str);
//                        break;
//                    }
//                    stringBuffer.append(index + "、字段" + m.getValue() + str).append("\n");
//                    continue;
//                }else{
            stringBuffer.append(map.getValue());
//                }
//        }
        }
        if (!flag && null != errorCollection.getErrorMessages())
        {
            stringBuffer.append("JIRA ERROR:");
            Iterator<String> errorMessages = errorCollection.getErrorMessages().iterator();
            while (errorMessages.hasNext()) {
                stringBuffer.append(errorMessages.next());
            }

        }

        return stringBuffer.toString();
    }

}
