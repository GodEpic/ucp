/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 券图模块Entity
 * @author Zachary
 * @version 2019-08-06
 */
public class CouponImage extends DataEntity<CouponImage> {
	
	private static final long serialVersionUID = 1L;
	private String actId;		// 活动ID
	private String couponLink;		// 图片链接
	private String couponDesc;		// 图片备注
	private String taskId;//任务ID
	
	public CouponImage() {
		super();
	}

	public CouponImage(String actId, String taskId) {
		this.actId = actId;
		this.taskId = taskId;
	}
	
	public CouponImage(String actId, String couponLink, String couponDesc) {
		this.actId = actId;
		this.couponLink = couponLink;
		this.couponDesc = couponDesc;
	}

	public CouponImage(String id){
		super(id);
	}

	@Length(min=0, max=32, message="活动ID长度必须介于 0 和 32 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=2000, message="图片链接长度必须介于 0 和 2000 之间")
	public String getCouponLink() {
		return couponLink;
	}

	public void setCouponLink(String couponLink) {
		this.couponLink = couponLink;
	}
	
	@Length(min=0, max=2000, message="图片备注长度必须介于 0 和 2000 之间")
	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}