package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.entity.NotifyRefActivity;

import java.util.List;

@MyBatisDao
public interface NotifyRefActivityDao  extends CrudDao<NotifyRefActivity> {

    List<NotifyRefActivity> findByActivityId(String activityId);

    NotifyRefActivity findByNotifyId(String notifyRefActivity);

}
