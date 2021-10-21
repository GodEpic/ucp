package com.yum.ucp.modules.activity.entity;

import com.yum.ucp.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

public class NotifyActivity extends DataEntity<NotifyActivity> {
	private String id;

	/**
	 * 活动id
	 */
	private String activityId;

	/**
	 * 活动名称
	 */
	private String activityName;

	/**
	 * 活动代码
	 */
	private String campaignCode;

	/**
	 * excel url
	 */
	private String excelUrl;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 标题
	 */
	private String summary;

	/**
	 * 品牌
	 */
	private String brand;

	/**
	 * 券类型
	 */
	private String couponType;

	private String statusValue;

	private String fileName;

	private String isUrgent;

	private String proposer;

	private List<NotifyFile> notifyFileList;

	private Date urgentDate;

	private String comments;

	private String searchTxt;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getUrgentDate() {
		return urgentDate;
	}

	public void setUrgentDate(Date urgentDate) {
		this.urgentDate = urgentDate;
	}

	public List<NotifyFile> getNotifyFileList() {
		return notifyFileList;
	}

	public void setNotifyFileList(List<NotifyFile> notifyFileList) {
		this.notifyFileList = notifyFileList;
	}

	public String getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getExcelUrl() {
		return excelUrl;
	}

	public void setExcelUrl(String excelUrl) {
		this.excelUrl = excelUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
