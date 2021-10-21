package com.yum.ucp.common.constants;

public class ActivityImportConstants {

	public final static String ACT_SYS_PARENT_NAME = "sys_type";
	public final static String ACT_SYS_PARENT_LABEL = "系统类型";
	public final static String ACT_SYS_TYPE = "act_sys_type";
	
	public final static String IMPORT_EXT_ALLOW = "xls,xlsx";
	public final static String IMPORT_DEFAULT_SYSNAME = "CommonField";
	
	public final static String IMPORT_ROWDATA_VALUE_NORMAL = "0";
	public final static String IMPORT_ROWDATA_VALUE_CHANGE = "1";

	public enum ImportType {
		STRING("文本"), DATE("时间"), INTEGER("整数"), DOUBLE("数字,两位小数");

		ImportType(String value) {
			this.value = value;
		}

		public String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public static boolean existImportType(String typeValue) {
		for (ImportType type : ImportType.values()) {
			if (type.getValue().equals(typeValue)) {
				return true;
			}
		}
		return false;
	}
}
