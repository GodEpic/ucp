package com.yum.ucp.modules.activity.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.sys.entity.User;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tony
 * @version 2019/10/21
 */
public class ActivityVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
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
     * 配置完成时间
     */
    private String confFinishedTime;

    /**
     * 建议测试时间
     */
    private String recommendedTestTime;

    /**
     * 测试完成时间
     */
    private String testFinishedTime;

    /**
     * 上架时间
     */
    private String launchTime;

    /**
     * 注意事项
     */
    private String description;

    /**
     * jira编号
     */
    private String jiraNo;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 反馈时间
     */
    private String feedbackTime;

    /**
     * 接收人
     */
    private String receiveUser;

    /**
     * 活动状态
     */
    private String status;

    /**
     * 0 普通，1 紧急
     */
    private String type;

    /**
     * 发布状态
     */
    private String releaseStatus;

    /**
     * jira节点状态
     */
    private String jiraStatus;

    /**
     * 认领人L2
     */
    private String receiveL2;

    /**
     * L2提交人（流程提交到QA的人）
     */
    private String l2Submitter;

    /**
     * 创建人
     */
    private User createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    private String searchTxt;

    private Boolean recommendedTestTimeOutBoolean;

    /**
     * 当前实体分页对象
     */
    protected Page<ActivityVO> page;

    @JsonIgnore
    @XmlTransient
    public Page<ActivityVO> getPage() {
        if (page == null) {
            page = new Page<ActivityVO>();
        }
        return page;
    }

    public Page<ActivityVO> setPage(Page<ActivityVO> page) {
        this.page = page;
        return page;
    }

    private Boolean inStatus;

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
        try {
            if (StringUtils.isNotBlank(recommendedTestTime)) {
                Date recommendedTestDate = DateUtils.parseDate(recommendedTestTime);
                Date outDate = DateUtils.addDays(recommendedTestDate, 8);
                if (outDate.compareTo(new Date()) >= 1) {
                    setRecommendedTestTimeOutBoolean(false);
                } else {
                    setRecommendedTestTimeOutBoolean(true);
                }
            }
        } catch (Exception e) {
            setRecommendedTestTimeOutBoolean(false);
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJiraNo() {
        return jiraNo;
    }

    public void setJiraNo(String jiraNo) {
        this.jiraNo = jiraNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getReceiveL2() {
        return receiveL2;
    }

    public void setReceiveL2(String receiveL2) {
        this.receiveL2 = receiveL2;
    }

    public String getL2Submitter() {
        return l2Submitter;
    }

    public void setL2Submitter(String l2Submitter) {
        this.l2Submitter = l2Submitter;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean isInStatus() {
        return inStatus;
    }

    public void setInStatus(Boolean inStatus) {
        this.inStatus = inStatus;
    }

    public Boolean getRecommendedTestTimeOutBoolean() {
        return recommendedTestTimeOutBoolean;
    }

    public void setRecommendedTestTimeOutBoolean(Boolean recommendedTestTimeOutBoolean) {
        this.recommendedTestTimeOutBoolean = recommendedTestTimeOutBoolean;
    }

    @Override
    public String toString() {
        return "ActivityVO{" +
                "id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", brand='" + brand + '\'' +
                ", couponType='" + couponType + '\'' +
                ", activityName='" + activityName + '\'' +
                ", couponCount=" + couponCount +
                ", confFinishedTime='" + confFinishedTime + '\'' +
                ", recommendedTestTime='" + recommendedTestTime + '\'' +
                ", testFinishedTime='" + testFinishedTime + '\'' +
                ", launchTime='" + launchTime + '\'' +
                ", description='" + description + '\'' +
                ", jiraNo='" + jiraNo + '\'' +
                ", priority='" + priority + '\'' +
                ", feedbackTime='" + feedbackTime + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", l2Submitter='" + l2Submitter + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", releaseStatus='" + releaseStatus + '\'' +
                ", jiraStatus='" + jiraStatus + '\'' +
                ", receiveL2='" + receiveL2 + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }

    public String getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }
}
