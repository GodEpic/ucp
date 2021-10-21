/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.task.entity.UcpTaskReceive;
import com.yum.ucp.modules.task.dao.UcpTaskReceiveDao;

/**
 * 任务认领Service
 * @author tony
 * @version 2019-08-06
 */
@Service
@Transactional(readOnly = true)
public class UcpTaskReceiveService extends CrudService<UcpTaskReceiveDao, UcpTaskReceive> {

	@Autowired
	private UcpTaskReceiveDao dao;

	@Override
	public UcpTaskReceive get(String id) {
		return super.get(id);
	}

	@Override
	public List<UcpTaskReceive> findList(UcpTaskReceive ucpTaskReceive) {
		return super.findList(ucpTaskReceive);
	}

	@Override
	public Page<UcpTaskReceive> findPage(Page<UcpTaskReceive> page, UcpTaskReceive ucpTaskReceive) {
		return super.findPage(page, ucpTaskReceive);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(UcpTaskReceive ucpTaskReceive) {
		super.save(ucpTaskReceive);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(UcpTaskReceive ucpTaskReceive) {
		super.delete(ucpTaskReceive);
	}

	public Integer countReceiveByTaskIdAndJiraStatus(UcpTaskReceive ucpTaskReceive) {
		return dao.countReceiveByTaskIdAndJiraStatus(ucpTaskReceive);
	}

	public UcpTaskReceive getByTaskId(String taskId){
		return dao.getByTaskId(taskId);
	}

	public UcpTaskReceive getByTaskIdAndJiraStatus(String taskId, String jiraStatus) {
		return dao.getByTaskIdAndJiraStatus(taskId, jiraStatus);
	}
}