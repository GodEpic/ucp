/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yum.ucp.modules.task.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.task.dao.SyncUserDao;
import com.yum.ucp.modules.task.entity.UserForTask;
import org.slf4j.Logger;

/**
 *
 * @author Administrator
 */

public class SyncUserService extends CrudService<SyncUserDao, UserForTask>
{
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SyncUserService.class);
    
    /**
     * 
     * @param taskId 任务ID，用于日志记录
     */
    public void syncUserinfo(String taskId){
        //从视图中读取用户信息总条数
        //固定每次读取一百个用户信息进行处理，计算需要执行多少次
        
        //循环一百个用户信息
        //开始处理一个用户信息
        //判断用户是否已存在DSC系统中
        //不在系统中，则新增到DSC系统中
    }
    
    @Override
    public void save(UserForTask userForTask)
    {
        super.save(userForTask); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserForTask get(String id)
    {
        return super.get(id); //To change body of generated methods, choose Tools | Templates.
    }

}
