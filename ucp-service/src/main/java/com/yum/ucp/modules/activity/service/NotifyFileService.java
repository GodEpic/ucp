package com.yum.ucp.modules.activity.service;

import com.yum.ucp.modules.activity.dao.NotifyFileDao;
import com.yum.ucp.modules.activity.entity.NotifyFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyFileService {
    @Autowired
    private NotifyFileDao notifyFileDao;

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(NotifyFile entity){
       return notifyFileDao.insert(entity);
    }


    public List<NotifyFile> findByNotifyId(String notifyId){
        return notifyFileDao.findByNotifyId(notifyId);
    }
}
