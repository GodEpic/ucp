package com.yum.ucp.modules.common.entity;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.annotation.IgnoreAnnotation;
import com.yum.ucp.common.annotation.SelectAnnotation;
import com.yum.ucp.common.persistence.DataEntity;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/16.
 */
public class CommonName implements Serializable {
    /**
     * case类型Id
     */
    @IgnoreAnnotation
    public String issueTypeId;
    /**
     * 优先级
     */
    @IgnoreAnnotation
    public String priority;
    /**
     * 数据库case状态
     */
    @IgnoreAnnotation
    public String status;
    /**
     * JIRAcase状态
     */
    @IgnoreAnnotation
    public String jiraStatus;
    /**
     * 是否催办
     */
    @IgnoreAnnotation
    public Boolean isPress = false;
    /**
     * 查看页面
     */
    @IgnoreAnnotation
    public String viewPage;
    /**
     * L1页面地址
     */
    @IgnoreAnnotation
    public String lv1Page;
    /**
     * L2页面地址
     */
    @IgnoreAnnotation
    public String lv2Page;
    /**
     * 摘要
     */
    public String summary;
    /***
     * 创建时间
     */
    public DateTime creationDate;
    /***
     * 问题Key
     */
    @IgnoreAnnotation
    public String key;
    /**
     * 描述
     */
    public String description;


    /**
     * 渠道来源
     */
    @SelectAnnotation
    public String channel;


    /**
     * 项目KEY
     */
    @IgnoreAnnotation
    public String projectKey;


    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJiraStatus() {
        return jiraStatus;
    }

    public void setJiraStatus(String jiraStatus) {
        this.jiraStatus = jiraStatus;
    }

    public Boolean getIsPress() {
        return isPress;
    }

    public void setIsPress(Boolean isPress) {
        this.isPress = isPress;
    }

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getLv1Page() {
        return lv1Page;
    }

    public void setLv1Page(String lv1Page) {
        this.lv1Page = lv1Page;
    }

    public String getLv2Page() {
        return lv2Page;
    }

    public void setLv2Page(String lv2Page) {
        this.lv2Page = lv2Page;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return JSONObject.toJSON(this).toString();
    }


}
