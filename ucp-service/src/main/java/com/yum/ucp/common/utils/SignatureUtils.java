package com.yum.ucp.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SignatureUtils {
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtils.class);

    private SignatureUtils() {
    }

    /**
     * 签名算法
     *
     * @param map     要参与签名的数据
     * @param signKey 签名用的Key
     * @return 签名
     */
    public static String getSign(Map<String, ?> map, String signKey, String... ignoreKeys) {
        StringBuilder signContent = new StringBuilder();
        signContent.append("{");
        if (null != map) {
            List<String> keys = new ArrayList<String>(map.keySet());
            Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
            for (String key : keys) {
                if (map.get(key) == null || ArrayUtils.contains(ignoreKeys, key)) {
                    continue;
                }
                Object valueObj = map.get(key);
                String[] valueArr;
                if (valueObj instanceof String[]) {
                    valueArr = (String[]) valueObj;
                } else {
                    valueArr = new String[]{valueObj.toString()};
                }
                String value;
                StringBuilder sb = new StringBuilder();
                for (String v : valueArr) {
                    sb.append(",");
                    sb.append(v);
                }
                value = sb.substring(1);
                if (value.length() > 0) {
                    signContent.append(key);
                    signContent.append("=");
                    signContent.append(value);
                }
                signContent.append("#");
            }
        }
        // SIGN_KEY_PARAMETER_NAME = partner_secret
        //signContent.append(Constants.SIGN_KEY_PARAMETER_NAME);
        signContent.append("}");
        signContent.append(signKey);
        logger.debug("Sign Before MD5:" + signContent.toString());
        String result = DigestUtils.md5Hex(signContent.toString()).toUpperCase();
        logger.debug("Sign Result:" + result);
        return result;
    }

}
