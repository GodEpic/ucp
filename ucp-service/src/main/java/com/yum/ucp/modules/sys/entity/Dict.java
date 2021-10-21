/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import com.yum.ucp.common.persistence.TreeEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 字典Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Dict extends TreeEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
//	private String parentId;//父Id
//	private String parentIds;//父Id
	private String isSelect;//是否被选中

	public Dict() {
		super();
	}
	
	public Dict(String id){
		super(id);
	}

	@Override
	public Dict getParent() {
		return parent;
	}

	@Override
	public void setParent(Dict parent) {
		this.parent = parent;
	}

	public Dict(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

//	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
//	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Override
	public Integer getSort() {
		return sort;
	}

	@Override
	public void setSort(Integer sort) {
		this.sort = sort;
	}

//	@Length(min=1, max=100)
//	public String getParentId() {
//		return parentId;
//	}
//
//	public void setParentId(String parentId) {
//		this.parentId = parentId;
//	}
//
//	public String getParentIds() {
//		return parentIds;
//	}
//
//	public void setParentIds(String parentIds) {
//		this.parentIds = parentIds;
//	}

	@Override
	public String toString() {
		return label;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

}