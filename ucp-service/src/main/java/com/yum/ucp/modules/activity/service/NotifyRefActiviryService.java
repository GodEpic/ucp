package com.yum.ucp.modules.activity.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.dao.ActivityDao;
import com.yum.ucp.modules.activity.dao.NotifyActivityDao;
import com.yum.ucp.modules.activity.dao.NotifyRefActivityDao;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.entity.NotifyRefActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NotifyRefActiviryService  extends CrudService<NotifyRefActivityDao, NotifyRefActivity> {
    @Autowired
    private NotifyRefActivityDao dao;




   public List<NotifyRefActivity> findByActivityId(String activityId){
        return dao.findByActivityId( activityId);
    }

    public NotifyRefActivity findByNotifyId(String notivityId){
        return dao.findByNotifyId(notivityId);

    }





}
