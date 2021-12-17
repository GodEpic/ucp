package com.yum.ucp.modules.activity.entity;

import com.yum.ucp.common.annotation.SelectAnnotation;
import com.yum.ucp.common.persistence.DataEntity;
import com.yum.ucp.common.utils.StringUtils;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class Activity extends DataEntity<Activity> {

    /**
     * 标题
     */
    public String summary;

    /**
     * 品牌
     */
    @SelectAnnotation
    public String brand;

    /**
     * 券类型
     */
    @SelectAnnotation
    public String couponType;

    /**
     * 活动名
     */
    public String activityName;
    /**
     * 券数量
     */
    public Integer couponCount;

    /**
     * 配置完成时间
     */
    public String confFinishedTime;

    /**
     * 建议测试时间
     */
    public String recommendedTestTime;

    /**
     * 测试完成时间
     */
    public String testFinishedTime;

    /**
     * 上架时间
     */
    public String launchTime;

    /**
     * 注意事项
     */
    public String description;

    /**
     * jira编号
     */
    public String jiraNo;

    /**
     * 优先级
     */
    public String priority;

    /**
     * 反馈时间
     */
    public String feedbackTime;

    /**
     * 接收人
     */
    public String receiveUser;

    /**
     * L2提交人（流程提交到QA的人）
     */
    public String l2Submitter;

    /**
     * 活动状态
     */
    public String status;

    /**
     * 0 普通，1 紧急
     */
    private String type;

    /**
     * 发布状态
     */
    public String releaseStatus;

    /**
     * jira节点状态
     */
    public String jiraStatus;

    private String notifyActivityId;

    private String notifyActivityNo;

    private String keyconfigif;

    private String configer;

    private Date configdate;

    private String sourceFlag;

    public String getKeyconfigif() {
        return keyconfigif;
    }

    public void setKeyconfigif(String keyconfigif) {
        this.keyconfigif = keyconfigif;
    }

    public String getNotifyActivityNo() {
        return notifyActivityNo;
    }

    public void setNotifyActivityNo(String notifyActivityNo) {
        this.notifyActivityNo = notifyActivityNo;
    }

    public String getNotifyActivityId() {
        return notifyActivityId;
    }

    public void setNotifyActivityId(String notifyActivityId) {
        this.notifyActivityId = notifyActivityId;
    }

    public List<UcpActFile> actFiles;

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

    public String getConfFinishedTime() {
        return confFinishedTime;
    }

    public void setConfFinishedTime(String confFinishedTime) {
        this.confFinishedTime = confFinishedTime;
    }

    public String getRecommendedTestTime() {
        return recommendedTestTime;
    }

    public void setRecommendedTestTime(String recommendedTestTime) {
        this.recommendedTestTime = recommendedTestTime;
    }

    public String getTestFinishedTime() {
        return testFinishedTime;
    }

    public void setTestFinishedTime(String testFinishedTime) {
        this.testFinishedTime = testFinishedTime;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public String getJiraNo() {
        return jiraNo;
    }

    public void setJiraNo(String jiraNo) {
        this.jiraNo = jiraNo;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getL2Submitter() {
        return l2Submitter;
    }

    public void setL2Submitter(String l2Submitter) {
        this.l2Submitter = l2Submitter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getJiraStatus() {
        return jiraStatus;
    }

    public void setJiraStatus(String jiraStatus) {
        this.jiraStatus = jiraStatus;
    }

    @Transient
    public List<UcpActFile> getActFiles() {
        return actFiles;
    }

    public void setActFiles(List<UcpActFile> actFiles) {
        this.actFiles = actFiles;
    }

    public String getType() {
        if (StringUtils.isBlank(type)) {
            return "0";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfiger() {
        return configer;
    }

    public void setConfiger(String configer) {
        this.configer = configer;
    }

    public Date getConfigdate() {
        return configdate;
    }

    public void setConfigdate(Date configdate) {
        this.configdate = configdate;
    }

    public String getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(String sourceFlag) {
        this.sourceFlag = sourceFlag;
    }
}
