package com.yum.ucp.modules.activity.pojo;

import java.io.Serializable;
import java.util.Date;


public class NotifyActivityVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 描述
     */
    private String remarks;

    /**
     * 创建人
     */
    private String createBy;


    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String updateBy;


    /**
     * 修改时间
     */
    private String updateDate;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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
