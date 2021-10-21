/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.service;

import java.util.List;

import com.yum.ucp.modules.task.pojo.UcpTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.dao.UcpTaskDao;

/**
 * 任务模块Service
 * @author tony
 * @version 2019-07-31
 */
@Service
@Transactional(readOnly = true)
public class UcpTaskService extends CrudService<UcpTaskDao, UcpTask> {

	@Autowired
	private UcpTaskDao dao;

	@Override
	public UcpTask get(String id) {
		return super.get(id);
	}

	@Override
	public List<UcpTask> findList(UcpTask ucpTask) {
		return super.findList(ucpTask);
	}

	@Override
	public Page<UcpTask> findPage(Page<UcpTask> page, UcpTask ucpTask) {
		return super.findPage(page, ucpTask);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(UcpTask ucpTask) {
		super.save(ucpTask);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(UcpTask ucpTask) {
		super.delete(ucpTask);
	}

	public Page<UcpTaskVO> findTaskPage(Page<UcpTaskVO> page, UcpTaskVO ucpTaskVO) {
		ucpTaskVO.setPage(page);
		page.setList(dao.findTaskList(ucpTaskVO));
		return page;
	}
	
	public Integer countByActId(String actId, String delFlag) {
		return dao.countByActId(actId);
	}

	public Integer getCurrentVersion(String actId, String delFlag) {
		return dao.countByActId(actId) + 1;
	}

	public UcpTask getByJiraNo(String jiraNo) {
		return dao.getByJiraNo(jiraNo);
	}

	public UcpTask getByActIdAndVersion(UcpTask ucpTask) {
		return dao.getByActIdAndVersion(ucpTask);
	}

	public List<UcpTask> getByActId(String actId) {
		return dao.getByActId(actId);
	}
}