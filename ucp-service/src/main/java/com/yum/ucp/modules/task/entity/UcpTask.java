/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 任务模块Entity
 * @author tony
 * @version 2019-07-29
 */
public class UcpTask extends DataEntity<UcpTask> {
	
	private static final long serialVersionUID = 1L;
	private String actId;		// 活动ID
	private String configStatus;		// 配置情况
	private String verifyStatus;		// 检查情况
	private Integer version;		// 任务版本
	private String jiraNo;
	private String parentJiraNo;
	private String jiraStatus;
	
	public UcpTask() {
		super();
	}

	public UcpTask(String id){
		super(id);
	}

	@Length(min=0, max=32, message="活动ID长度必须介于 0 和 32 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=32, message="配置情况长度必须介于 0 和 32 之间")
	public String getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}
	
	@Length(min=0, max=32, message="检查情况长度必须介于 0 和 32 之间")
	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getJiraNo() {
		return jiraNo;
	}

	public void setJiraNo(String jiraNo) {
		this.jiraNo = jiraNo;
	}

	public String getParentJiraNo() {
		return parentJiraNo;
	}

	public void setParentJiraNo(String parentJiraNo) {
		this.parentJiraNo = parentJiraNo;
	}

	public String getJiraStatus() {
		return jiraStatus;
	}

	public void setJiraStatus(String jiraStatus) {
		this.jiraStatus = jiraStatus;
	}
}