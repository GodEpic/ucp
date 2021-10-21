/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 反馈报告Entity
 * @author Zachary
 * @version 2019-08-16
 */
public class QaCheck extends DataEntity<QaCheck> {
	
	private static final long serialVersionUID = 1L;
	private String actId;		// 活动ID
	private String couponId;		// 优惠券ID
	private String description;		// 问题描述
	private String type;		// 问题类型
	private String ignore;      //是否忽略
	private String feedback;    //反馈
	private Integer version;		// 版本号

	public QaCheck() {
		super();
	}

	public QaCheck(String id){
		super(id);
	}

	@Length(min=0, max=75, message="活动ID长度必须介于 0 和 75 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=75, message="优惠券ID长度必须介于 0 和 75 之间")
	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	@Length(min=0, max=255, message="问题描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=255, message="问题类型长度必须介于 0 和 255 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}