/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.entity.QaResult;
import com.yum.ucp.modules.activity.dao.QaResultDao;

/**
 * 检查结果Service
 * @author Zachary
 * @version 2019-08-16
 */
@Service
@Transactional(readOnly = true)
public class QaResultService extends CrudService<QaResultDao, QaResult> {

	@Autowired
	private QaResultDao qaResultDao;
	
	public QaResult get(String id) {
		return super.get(id);
	}
	
	public List<QaResult> findList(QaResult qaResult) {
		return super.findList(qaResult);
	}
	
	public Page<QaResult> findPage(Page<QaResult> page, QaResult qaResult) {
		return super.findPage(page, qaResult);
	}
	
	@Transactional(readOnly = false)
	public void save(QaResult qaResult) {
		super.save(qaResult);
	}
	
	@Transactional(readOnly = false)
	public void delete(QaResult qaResult) {
		super.delete(qaResult);
	}
	
	public Integer getLastVersionByActId(String actId) {
		return qaResultDao.getLastVersionByActId(actId, QaResult.DEL_FLAG_NORMAL);
	}
	
	public Integer getCurrentVersionByActId(String actId) {
		return getLastVersionByActId(actId) + 1;
	}

	/**
	 * 查询最新的未通过的QA检查结果
	 * @param actId
	 * @return
	 */
	public QaResult findLastVersionByActId(String actId){
		return qaResultDao.findLastVersionByActId(actId, "1", QaResult.DEL_FLAG_NORMAL);
	}
}