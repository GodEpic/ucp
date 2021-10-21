package com.yum.ucp.modules.sys.utils;

import com.yum.ucp.common.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ReadConfig {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReadConfig.class);
    private volatile static ReadConfig config;

    private ReadConfig() {
    }

    public static ReadConfig init() {
        if (config == null) {
            synchronized (ReadConfig.class) {
                if (config == null) {
                    config = new ReadConfig();
                }
            }
        }
        return config;
    }

    private volatile Map<String, String> smsCodeMap;

    //sso  config
    private volatile Properties ssoProps;
    private volatile boolean isProd;
    private volatile String deployMode;

    //error  config
    private volatile Properties errorProps;

    private volatile Properties fieldUcpProps;

    private Properties loadConfig() {
        String properties_name = "config.properties";
        try {
            if (ssoProps == null) {
                ssoProps = new Properties();
                ssoProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties_name));
                deployMode = ssoProps.getProperty("deploy.mode");
                isProd = StringUtils.isNotEmpty(deployMode) && StringUtils.equalsIgnoreCase("prod", deployMode);
            }
        } catch (FileNotFoundException e) {
            logger.error("未找到配置文件{}，异常信息：", properties_name, e);
        } catch (IOException e) {
            logger.error("配置文件读取失败{}，异常信息：", properties_name, e);
        }
        return ssoProps;
    }

    public static String getSsoValue(String key) {
        Properties properties = ReadConfig.init().loadConfig();
        if (null == properties) {
            return "";
        }
        return properties.getProperty(key);
    }

    private Properties loadError() {
        String properties_name = "errors.properties";
        try {
            if (errorProps == null) {
                errorProps = new Properties();
                errorProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties_name));
            }
        } catch (FileNotFoundException e) {
            logger.error("未找到配置文件{}，异常信息：", properties_name, e);
        } catch (IOException e) {
            logger.error("配置文件读取失败{}，异常信息：", properties_name, e);
        }
        return errorProps;
    }

    public Map<String, String> getSMSCodeMap() {
        if (smsCodeMap == null) {
            smsCodeMap = new HashMap<>();
            String prefix = "sms_code_";
            Set<String> smsKeySet = ReadConfig.init().loadError().stringPropertyNames();
            for (String key : smsKeySet) {
                if (!StringUtils.startsWith(key, prefix)) {
                    continue;
                }
                String content = getErrorValue(key);
                String code = StringUtils.replace(key, prefix, "");
                smsCodeMap.put(code, content);
            }
        }
        return smsCodeMap;
    }

    public static String getErrorValue(String key) {
        Properties properties = ReadConfig.init().loadError();
        if (null == properties) {
            return "";
        }
        return properties.getProperty(key);
    }

    private Properties loadUCP() {
        String properties_name = isProd ? "field_ucp_config_prod.properties" : "field_ucp_config.properties";
        if("uat".equals(deployMode)) {
            properties_name = "field_ucp_config_uat.properties";
        }
        try {
            if (fieldUcpProps == null) {
                fieldUcpProps = new Properties();
                fieldUcpProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties_name));
            }
        } catch (FileNotFoundException e) {
            logger.error("未找到配置文件{}，异常信息：", properties_name, e);
        } catch (IOException e) {
            logger.error("配置文件读取失败{}，异常信息：", properties_name, e);
        }
        return fieldUcpProps;
    }

    public static String getUcpFieldValue(String key) {
        Properties properties = ReadConfig.init().loadUCP();
        if (null == properties) {
            return "";
        }
        return properties.getProperty(key);
    }

}
