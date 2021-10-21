/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.pojo.UcpTaskVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 任务模块DAO接口
 * @author tony
 * @version 2019-07-31
 */
@MyBatisDao
public interface UcpTaskDao extends CrudDao<UcpTask> {
	/**
	 * 查询任务列表
	 * @param ucpTaskVO
	 * @return
	 */
	List<UcpTaskVO> findTaskList(UcpTaskVO ucpTaskVO);
	
	/**
	 * 根据活动ID查询子任务数量
	 * @param actId
	 * @return
	 */
	Integer countByActId(@Param("actId") String actId);

	UcpTask getByJiraNo(@Param("jiraNo") String jiraNo);

	UcpTask getByActIdAndVersion(UcpTask ucpTask);

	List<UcpTask> getByActId(@Param("actId") String actId);
}