package com.yum.ucp.modules.Interface.entity;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

public class NotityRequest {
    /**
     * 事件
     */
    private String event;
    /**
     * 活动编号
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
     * Excel地址
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


    /**
     * 申请人
     */
    private String proposer;


    /**
     * 是否加急
     */
    private String isUrgent;
    /**
     * 角色
     */
    private String role;

    /**
     * 加急时间
     * @return
     */
    private String urgentDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgentDate() {
        return urgentDate;
    }

    public void setUrgentDate(String urgentDate) {
        this.urgentDate = urgentDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }




    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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


    public static void main(String[] args) {
        System.out.println(new BASE64Encoder().encode("sadasdasd".getBytes(StandardCharsets.UTF_8)));;
    }




}
