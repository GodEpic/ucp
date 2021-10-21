package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.NotifyFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface NotifyFileDao {

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(NotifyFile entity);


    List<NotifyFile> findByNotifyId(@Param("notifyId") String notifyId);
}
