package com.yum.ucp.modules.task.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.modules.sys.entity.User;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

/**
 * 任务模块VO
 * @author tony
 * @version 2019-07-30
 */
public class UcpTaskVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 优先级
	 */
	private String priority;
	/**
	 * 任务发送时间
	 */
	private Date createDate;
	/**
	 * 配置完成时间
	 */
	private String confFinishedTime;
	/**
	 * 活动ID
	 */
	private String actId;
	
	/**
	 * 活动名
	 */
	private String activityName;

	/**
	 * 活动编号
	 */
	private String notifyActivityNo;
	/**
	 * 券数量
	 */
	private Integer couponCount;
	/**
	 * 配置情况
	 */
	private String configStatus;
	/**
	 * 检查情况
	 */
	private String verifyStatus;
	/**
	 * jira节点状态
	 */
	private String jiraStatus;
	/**
	 * jira节点状态L1
	 */
	private String jiraStatusL1;
	/**
	 * jira节点状态L2
	 */
	private String jiraStatusL2;
	/**
	 * 活动流程jira节点状态
	 */
	private String activityJiraStatus;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 创建人
	 */
	private User createBy;
	/**
	 * 认领人
	 */
	private String receive;
	/**
	 * 认领人L1
	 */
	private String receiveL1;
	/**
	 * 认领人L2
	 */
	private String receiveL2;

	/**
	 * 0 普通，1 紧急
	 */
	private String type;

	/**
	 * 当前实体分页对象
	 */
	protected Page<UcpTaskVO> page;
	
	private String configer;
	
	private String searchTxt;

	@JsonIgnore
	@XmlTransient
	public Page<UcpTaskVO> getPage() {
		if (page == null){
			page = new Page<UcpTaskVO>();
		}
		return page;
	}

	public Page<UcpTaskVO> setPage(Page<UcpTaskVO> page) {
		this.page = page;
		return page;
	}

	public String getNotifyActivityNo() {
		return notifyActivityNo;
	}

	public void setNotifyActivityNo(String notifyActivityNo) {
		this.notifyActivityNo = notifyActivityNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getConfFinishedTime() {
		return confFinishedTime;
	}

	public void setConfFinishedTime(String confFinishedTime) {
		this.confFinishedTime = confFinishedTime;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public String getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getJiraStatus() {
		return jiraStatus;
	}

	public void setJiraStatus(String jiraStatus) {
		this.jiraStatus = jiraStatus;
	}

	public String getActivityJiraStatus() {
		return activityJiraStatus;
	}

	public void setActivityJiraStatus(String activityJiraStatus) {
		this.activityJiraStatus = activityJiraStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJiraStatusL1() {
		return jiraStatusL1;
	}

	public void setJiraStatusL1(String jiraStatusL1) {
		this.jiraStatusL1 = jiraStatusL1;
	}

	public String getJiraStatusL2() {
		return jiraStatusL2;
	}

	public void setJiraStatusL2(String jiraStatusL2) {
		this.jiraStatusL2 = jiraStatusL2;
	}

	public String getReceiveL1() {
		return receiveL1;
	}

	public void setReceiveL1(String receiveL1) {
		this.receiveL1 = receiveL1;
	}

	public String getReceiveL2() {
		return receiveL2;
	}

	public void setReceiveL2(String receiveL2) {
		this.receiveL2 = receiveL2;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	
	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}

	@Override
	public String toString() {
		return "UcpTaskVO{" +
				"priority='" + priority + '\'' +
				", createDate='" + createDate + '\'' +
				", confFinishedTime='" + confFinishedTime + '\'' +
				", activityName='" + activityName + '\'' +
				", couponCount=" + couponCount +
				", configStatus='" + configStatus + '\'' +
				", verifyStatus='" + verifyStatus + '\'' +
                ", jiraStatus='" + jiraStatus + '\'' +
				", jiraStatusL1='" + jiraStatusL1 + '\'' +
				", jiraStatusL2='" + jiraStatusL2 + '\'' +
				", activityJiraStatus='" + activityJiraStatus + '\'' +
				", createBy='" + createBy + '\'' +
                ", receive='" + receive + '\'' +
				", receiveL1='" + receiveL1 + '\'' +
				", receiveL2='" + receiveL2 + '\'' +
				", type='" + type + '\'' +
				'}';
	}
	
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getConfiger() {
		return configer;
	}

	public void setConfiger(String configer) {
		this.configer = configer;
	}
	
	private String keyConfigIf;

	public String getKeyConfigIf() {
		return keyConfigIf;
	}

	public void setKeyConfigIf(String keyConfigIf) {
		this.keyConfigIf = keyConfigIf;
	}
	
}