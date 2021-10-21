/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 检查结果Entity
 * @author Zachary
 * @version 2019-08-16
 */
public class QaResult extends DataEntity<QaResult> {
	
	private static final long serialVersionUID = 1L;
	private String actId;		// 活动主键ID
	private String result;		// 测试结果
	private Integer version;		// 版本号
	
	public QaResult() {
		super();
	}

	public QaResult(String id){
		super(id);
	}

	@Length(min=0, max=75, message="活动主键ID长度必须介于 0 和 75 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=1, message="测试结果长度必须介于 0 和 1 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}