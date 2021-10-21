/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yum.ucp.modules.task;

import com.yum.ucp.modules.task.service.SyncUserService;

import java.util.Date;
import org.slf4j.Logger;

/**
 *
 * @author Administrator
 */
public class SyncTask
{
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SyncTask.class);
    
    private SyncUserService syncService;

    public SyncUserService getSyncService()
    {
        return syncService;
    }

    public void setSyncService(SyncUserService syncService)
    {
        this.syncService = syncService;
    }
    
    /**
     * 用户信息同步
     */
    public void syncUserinfo(){
        String taskId = "dsc_userinfo_sync_" + new Date().getTime();
        LOGGER.info("用户信息同步任务（任务ID：{}）开始执行", taskId);
        
        getSyncService().syncUserinfo(taskId);
        
        LOGGER.info("用户信息同步任务（任务ID：{}）执行结束", taskId);
    }
    
    /**
     * 餐厅信息同步
     */
    public void syncRestaurant(){
        
    }
    
}
