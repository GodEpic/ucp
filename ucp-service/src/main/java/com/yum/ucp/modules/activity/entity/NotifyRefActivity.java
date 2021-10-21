package com.yum.ucp.modules.activity.entity;


import com.yum.ucp.common.persistence.DataEntity;

public class NotifyRefActivity   extends DataEntity<NotifyRefActivity> {
    private String id;
    private String activityId;
    private String notifyId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }
}
