/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.senderlog.entity;

import com.yum.ucp.common.persistence.DataEntity;


/**
 * case配置Entity
 * @author Edward.Luo
 * @version 2016-12-12
 */
public class DscSMSEmailLog extends DataEntity<DscSMSEmailLog> {
	
	private static final long serialVersionUID = 1L;
	private String target;		// 手机号或邮箱地址
	private String content;		// 内容
	private String type;		// 类型 0短信 1 email
	private String brand;		// 品牌缩写
	private String status;
	private String errorMessage;

	public DscSMSEmailLog() {
		super();
	}

	public DscSMSEmailLog(String id){
		super(id);
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}