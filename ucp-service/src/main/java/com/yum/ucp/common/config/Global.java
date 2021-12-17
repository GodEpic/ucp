/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.common.config;

import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Maps;
import com.yum.ucp.common.utils.PropertiesLoader;
import com.yum.ucp.common.utils.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

	public static final Pattern PATTERN = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

	public static final String SMS_CONTENT = "smscontent" ;
	public static final String SMS_SN = "sn" ;
	public static final String SMS_PSW = "psw" ;
	public static final String CODE_MASK = "#code#" ;
	public static final String SUCCESS = "success";
	public static final String DATA = "data";
	public static final String JOB = "job";
	public static final String WORKFLOW = "workflow";

	public static final String EMAIL_INVOICE = "invoice" ;
	public static final String EMAIL_SUBJECT = "subject" ;
	public static final String EMAIL_BODY = "body" ;
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("jeesite.properties");

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	public static final String SEND_FAILED = "sendFailed";


	public static final String YUM_EMAIL_HOST = "yum.email.host";
	public static final String YUM_EMAIL_PORT = "yum.email.port";
	public static final String YUM_EMAIL_BCC = "yum.email.bcc";
	public static final String YUM_EMAIL_FROM= "yum.email.from";
	public static final String YUM_EMAIL_USERNAME = "yum.email.username";
	public static final String YUM_EMAIL_PWD = "yum.email.pwd";
	public static final String YUM_EMAIL_HOSTNAME = "yum.email.hostname";
	public static final String YUM_EMAIL_SSL = "yum.email.ssl";
	public static final String YUM_EMAIL_CC_MAIL = "yum.email.cc.mail";
	public static final String YUM_EMAIL_CC_NAME = "yum.email.cc.name";
        
	public static final String YUM_SMS_URL = "yum.sms.url";
	public static final String DHST_ACCOUNT = "dhst.account";
	public static final String DHST_PASSWORD = "dhst.password";
	public static final String DHST_URL = "dhst.url";
	
	public static final String FTP_USERNAME = "ftp_username";
	public static final String FTP_PASSWORD = "ftp_password";
	public static final String FTP_IP = "ftp_ip";
	public static final String FTP_PORT = "ftp_port";
	public static final String FTP_PATH_URL = "ftp_path_url";

	public static final String NOTIFY_SUCCESS_URL="notify_success_url";
	public static final String NOTIFY_REJECT_URL="notify_reject_url";

	public static final String SSO_OAUTH_TOKEN_ADDRESS = "sso_oauth_token_address";
	public static final String SSO_OAUTH_TOKEN_CLIENT_ID = "sso_oauth_token_client_id";
	public static final String SSO_OAUTH_TOKEN_CLIENT_SECRET = "sso_oauth_token_client_secret";
	public static final String SSO_OAUTH_TOKEN_CLIENT_REDIRECT_URI = "sso_oauth_token_client_redirect_uri";
	public static final String SSO_OAUTH_TOKEN_CLIENT_GRANT_TYPE = "sso_oauth_token_client_grantType";
	public static final String SSO_OAUTH_USERINFO_ADDRESS = "sso_oauth_userinfo_address";



	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

	public static final String SMS_TEMPLATE = "smsTemplate";
	public static final String EMAIL_TEMPLATE = "emailtemplate";

	/**
	 * 获取配置
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static final String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	/**
	 * 获取管理端根路径
	 */
	public static final String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static final String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static final String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * 同步任务批量抓取条数
	 */
	public static final int TASK_BATCH_NUM = 100;

	/**
	 * Yum 用户同步视图中的离职状态标识（固定）
	 */
	public static final String DISABLE_USER_FLAG = "TER_TER";

	/**
	 * 默认初始密码
	 */
	public static final String DEFAULT_PASSWORD_SHA = "02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032";

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 页面获取常量
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

	/**
	 * 获取上传文件的根目录
	 * @return
	 */
	public static final String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)){
			try {
				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
//		System.out.println("userfiles.basedir: " + dir);
		return dir;
	}
	
    /**
     * 获取工程路径
     * @return
     */
    public static final String getProjectPath(){
    	// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
	
}
