/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 导入模块列Entity
 * @author tony
 * @version 2019-07-24
 */
public class ModuleActColumn extends DataEntity<ModuleActColumn> {
	
	private static final long serialVersionUID = 1L;
	private String actId;		// 活动ID
	private Integer version;		// 版本
	private String name;		// 字段名
	private String label;		// 展示名
	private String description;		// 字段描述
	private String type;		// 字段类型
	private String moduleId;		// 模块ID
	private Integer sort;		// 排序
	
	public ModuleActColumn() {
		super();
	}

	public ModuleActColumn(String id){
		super(id);
	}

	@Length(min=0, max=32, message="活动ID长度必须介于 0 和 32 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Length(min=0, max=255, message="字段名长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="展示名长度必须介于 0 和 255 之间")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Length(min=0, max=255, message="字段描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=50, message="字段类型长度必须介于 0 和 50 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=32, message="模块ID长度必须介于 0 和 32 之间")
	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}