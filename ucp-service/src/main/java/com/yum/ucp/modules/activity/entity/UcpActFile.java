/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

import java.beans.Transient;

/**
 * 特殊活动附件关联Entity
 * @author cherlin
 * @version 2019-08-23
 */
public class UcpActFile extends DataEntity<UcpActFile> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 活动id
 	 */
	private String actId;
	/**
	 * 描述
 	 */
	private String description;
	/**
	 * 排序
 	 */
	private String sort;

	private String tempId;
	private String rowId;

	private String content;
	
	public UcpActFile() {
		super();
	}

	public UcpActFile(String id){
		super(id);
	}

	@Length(min=0, max=32, message="活动id长度必须介于 0 和 32 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Transient
	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	@Transient
	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}