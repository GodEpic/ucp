/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 导入模块值Entity
 * @author tony
 * @version 2019-07-24
 */
public class ModuleActValue extends DataEntity<ModuleActValue> {
	
	private static final long serialVersionUID = 1L;
	private String columnId;		// 字段ID
	private String rowId;		// 行数
	private String value;		// 值
	private String change;		// 是否改变

	private Integer sort; //排序
	
	
	private String columnName;
	private String columnLabel;
	private String columnType;
	
	public ModuleActValue() {
		super();
	}

	public ModuleActValue(String id){
		super(id);
	}

	@Length(min=0, max=32, message="字段ID长度必须介于 0 和 32 之间")
	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	@Length(min=0, max=32, message="行数长度必须介于 0 和 32 之间")
	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	@Length(min=0, max=4000, message="值长度必须介于 0 和 4000 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=0, max=1, message="是否改变长度必须介于 0 和 1 之间")
	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
}