/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.UcpTaskReceive;
import org.apache.ibatis.annotations.Param;

/**
 * 任务认领DAO接口
 * @author tony
 * @version 2019-08-06
 */
@MyBatisDao
public interface UcpTaskReceiveDao extends CrudDao<UcpTaskReceive> {

    /**
     * 根据任务ID和jira状态查询这个任务是否已经被认领
     * @param ucpTaskReceive
     * @return
     */
    Integer countReceiveByTaskIdAndJiraStatus(UcpTaskReceive ucpTaskReceive);

    /**
     * 根据任务ID和当前jira状态查询任务处理人
     * @param taskId
     * @return
     */
    UcpTaskReceive getByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID和jira状态查询任务处理人
     * @param taskId
     * @param jiraStatus
     * @return
     */
    UcpTaskReceive getByTaskIdAndJiraStatus(@Param("taskId") String taskId, @Param("jiraStatus") String jiraStatus);
}