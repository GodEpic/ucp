/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 导入模块Entity
 * @author tony
 * @version 2019-07-24
 */
public class UcpModule extends DataEntity<UcpModule> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 模块名
	private String sysId;		// 系统ID
	private Integer sort;		// 排序
	
	private String sysName;

	private Integer changeCount; //模块中字段修改数量
	
	public UcpModule() {
		super();
	}

	public UcpModule(String id){
		super(id);
	}

	@Length(min=0, max=255, message="模块名长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=32, message="系统ID长度必须介于 0 和 32 之间")
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public Integer getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(Integer changeCount) {
		this.changeCount = changeCount;
	}
}