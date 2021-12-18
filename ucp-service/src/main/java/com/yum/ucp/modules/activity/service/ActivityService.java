/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.List;

import com.atlassian.jira.rest.client.api.domain.Session;
import com.yum.ucp.modules.sys.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.dao.ActivityDao;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.pojo.ActivityVO;

/**
 * 活动模块Service
 *
 * @author tony
 * @version 2019-07-26
 */
@Service
@Transactional(readOnly = true)
public class ActivityService extends CrudService<ActivityDao, Activity> {
    @Autowired
    private ActivityDao dao;

    @Autowired
    private NotifyActivityService notifyActivityService;


    @Override
    public Activity get(String id) {
        return super.get(id);
    }

    @Override
    public List<Activity> findList(Activity activity) {
        return super.findList(activity);
    }

    @Override
    public Page<Activity> findPage(Page<Activity> page, Activity activity) {
        return super.findPage(page, activity);
    }

    public Page<ActivityVO> findActivityPage(Page<ActivityVO> page, ActivityVO activityVO) {
        activityVO.setPage(page);
        page.setList(dao.findActivityList(activityVO));
        return page;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(Activity activity) {
        super.save(activity);
    }

    @Transactional(readOnly = false)
    public void saveActivity(Activity activity) {
        super.save(activity);
        if (StringUtils.isNotEmpty(activity.getNotifyActivityId())) {
            notifyActivityService.accept(activity.getId(), activity.getNotifyActivityId());
        }
    }


    @Override
    @Transactional(readOnly = false)
    public void delete(Activity activity) {
        super.delete(activity);
    }

    @Transactional(readOnly = false)
    public void updateStatus(Activity activity) {
        dao.updateStatus(activity);
    }


    public List<Activity> findByNameAndStatus(String name, String status) {
        Activity activity = new Activity();
        activity.setActivityName(name);
        activity.setStatus(status);
        return dao.findByNameAndStatus(activity);
    }


    public boolean isActivityExisting(String notifyActivityNo) {
        Activity activity = new Activity();
        activity.setNotifyActivityNo(notifyActivityNo);
        return CollectionUtils.isEmpty(dao.findByNotifyActivityNo(activity)) ? false : true;
    }
}