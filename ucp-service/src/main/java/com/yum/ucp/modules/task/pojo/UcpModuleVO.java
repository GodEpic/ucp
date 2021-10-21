package com.yum.ucp.modules.task.pojo;

public class UcpModuleVO {

	/**
	 * 系统模块字段
	 */
	private String sysName;
	private String sysSort;
	private String sysCount;

	/**
	 * 模块字段
	 */
	private String modName;
	private String modSort;
	private String modCount;

	/**
	 * 列字段信息
	 */
	private String columnName;
	private String columnSort;
	private String columnValue;

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysSort() {
		return sysSort;
	}

	public void setSysSort(String sysSort) {
		this.sysSort = sysSort;
	}

	public String getSysCount() {
		return sysCount;
	}

	public void setSysCount(String sysCount) {
		this.sysCount = sysCount;
	}

	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getModSort() {
		return modSort;
	}

	public void setModSort(String modSort) {
		this.modSort = modSort;
	}

	public String getModCount() {
		return modCount;
	}

	public void setModCount(String modCount) {
		this.modCount = modCount;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnSort() {
		return columnSort;
	}

	public void setColumnSort(String columnSort) {
		this.columnSort = columnSort;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

}
