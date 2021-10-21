/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.pojo.ActivityVO;

import java.util.List;

/**
 * 活动模块DAO接口
 * @author tony
 * @version 2019-07-26
 */
@MyBatisDao
public interface ActivityDao extends CrudDao<Activity> {
    void updateStatus(Activity activity);

    List<ActivityVO> findActivityList(ActivityVO activityVO);

    List<Activity> findByNameAndStatus(Activity activity);

    List<Activity> findByNotifyActivityNo(Activity activity);
    
    List<Activity> selectWarnData(ActivityVO activity);
}