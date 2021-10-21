package com.yum.ucp.common.exception;

import java.text.MessageFormat;

public enum ResponseErrorCode {
	
	SUCCESS("0" ,"ok"),
	
	ERROR_IMPORT_VALID("10000", "导入验证错误"),
	ERROR_IMPORT_EXT("10001", "文件格式错误"),
	ERROR_IMPORT_COUPONT_COUNT("10002", "导入的券数量与活动基本信息中券数量不匹配"),
	ERROR_ACTIVITY_SAVE("10003", "保存活动失败"),
	ERROR_CONFIG_UNFINISHED("10004", "还有未配置完成的券，无法提交至下一步"),
	ERROR_CHECK_UNFINISHED("10005", "还有未检查完成的券"),
	ERROR_IMPORT_DUPLICATE("10006", "重复提交，关闭页面，刷新接收活动列表再试。"),
	SYS_ERROR("-1", "未知错误，请联系系统管理员。");
	
	private String code;
	private String desc;

	ResponseErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String parse(Object... args) {
		return MessageFormat.format(desc, args);
	}
}
