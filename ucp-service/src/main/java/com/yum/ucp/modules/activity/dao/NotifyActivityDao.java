package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.pojo.ActivityVO;
import com.yum.ucp.modules.activity.pojo.NotifyActivityVO;

import java.util.List;

/**
 * 接口导入活动接口
 */
@MyBatisDao
public interface NotifyActivityDao extends CrudDao<NotifyActivity> {
    List<NotifyActivity> findByActivityIdAndStatus(NotifyActivity notifyActivity);


    void updateStatusById(NotifyActivity notifyActivity);

    List<NotifyActivity> findByActivityId(NotifyActivity notifyActivity);

   void deleteByActivityId(NotifyActivity notifyActivity);
}
