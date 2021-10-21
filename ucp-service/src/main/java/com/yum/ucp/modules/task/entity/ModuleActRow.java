/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 导入模块行Entity
 * @author tony
 * @version 2019-07-24
 */
public class ModuleActRow extends DataEntity<ModuleActRow> {
	
	private static final long serialVersionUID = 1L;
	private String configCertifiCenter;		// 券号
	private String actId;		// 活动ID
	private String change;		// 是否改变
	private Integer version;		// 版本
	private Integer sort;		// 版本
	
	private String configStatus; //配置状态 是否完成
	private String checkStatus;  //检查状态 是否完成
	private Date configDate; //配置时间
	private Date checkDate; //检查时间

	private Integer qaResult; //QA检查结果
	
	public ModuleActRow() {
		super();
	}

	public ModuleActRow(String id){
		super(id);
	}
	
	public ModuleActRow(String actId, Integer version) {
		this.actId = actId;
		this.version = version;
	}

	public ModuleActRow(String actId, Integer version, String configStatus, String checkStatus) {
		this.actId = actId;
		this.version = version;
		this.configStatus = configStatus;
		this.checkStatus = checkStatus;
	}

	@Length(min=0, max=255, message="券号长度必须介于 0 和 255 之间")
	public String getConfigCertifiCenter() {
		return configCertifiCenter;
	}

	public void setConfigCertifiCenter(String configCertifiCenter) {
		this.configCertifiCenter = configCertifiCenter;
	}
	
	@Length(min=0, max=32, message="活动ID长度必须介于 0 和 32 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=1, message="是否改变长度必须介于 0 和 1 之间")
	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Date getConfigDate() {
		return configDate;
	}

	public void setConfigDate(Date configDate) {
		this.configDate = configDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Integer getQaResult() {
		return qaResult;
	}

	public void setQaResult(Integer qaResult) {
		this.qaResult = qaResult;
	}
}