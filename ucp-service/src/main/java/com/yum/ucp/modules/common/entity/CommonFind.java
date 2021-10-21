package com.yum.ucp.modules.common.entity;

import com.yum.ucp.modules.sys.constants.CommonConstants;
import org.joda.time.DateTime;

/**
 * Created by Administrator on 2017/1/16.
 */
public class CommonFind {
    public String summary;
    public String creator;
    public String key;
    public String type;
    public String status;
    public String jiraStatus;
    public Boolean isLock=false;
    public String lockUserName;
    public Object lockUser;
    public String assignee;
    public String resolvePerson;

    /**
     * 经办人
     */
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * L1是否处理过
     */
    public Boolean l1IsDeal;
    /***
     * 创建时间
     */
    public DateTime creationDate;

    public String priority;
    /**
     * 是否催办
     */
    public Boolean isColor;

    /**
     * L1页面地址
     */
    public String lv1Page;
    /**
     * L2页面地址
     */
    public String lv2Page;
    /**
     * 查看页面
     */
    public String viewPage;

    public Object getLockUser() {
        return lockUser;
    }

    public void setLockUser(Object lockUser) {
        this.lockUser = lockUser;
    }

    public Boolean getIsColor() {
        return isColor;
    }

    public void setIsColor(Boolean isColor) {
        this.isColor = isColor;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getJiraStatus() {
        return jiraStatus;
    }

    public void setJiraStatus(String jiraStatus) {
        this.jiraStatus = jiraStatus;
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

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getStatus() {
        if (null != status) {
            if (status.equals(CommonConstants.DscTaskLockType.YES)) {
                return CommonConstants.DscTaskLockType.LOCK;
            } else {
                return CommonConstants.DscTaskLockType.UNLOCK;
            }
        }
        return CommonConstants.DscTaskLockType.UNLOCK;
    }

    public String getCreationDate() {
        if (null == creationDate) {
            return null;
        }
        return creationDate.toString("yyyy-MM-dd HH:mm:ss");
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public String getLockUserName() {
        return lockUserName;
    }

    public void setLockUserName(String lockUserName) {
        this.lockUserName = lockUserName;
    }

    public Boolean getL1IsDeal() {
        return l1IsDeal;
    }

    public void setL1IsDeal(Boolean l1IsDeal) {
        this.l1IsDeal = l1IsDeal;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getResolvePerson() {
        return resolvePerson;
    }

    public void setResolvePerson(String resolvePerson) {
        this.resolvePerson = resolvePerson;
    }
}
