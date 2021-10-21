/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.entity.QaCheck;
import com.yum.ucp.modules.activity.entity.QaResult;
import com.yum.ucp.modules.activity.dao.QaCheckDao;

/**
 * 反馈报告Service
 * @author Zachary
 * @version 2019-08-16
 */
@Service
@Transactional(readOnly = true)
public class QaCheckService extends CrudService<QaCheckDao, QaCheck> {

	@Autowired
	private QaCheckDao qaCheckDao;
	
	public QaCheck get(String id) {
		return super.get(id);
	}
	
	public List<QaCheck> findList(QaCheck qaCheck) {
		return super.findList(qaCheck);
	}
	
	public Page<QaCheck> findPage(Page<QaCheck> page, QaCheck qaCheck) {
		return super.findPage(page, qaCheck);
	}
	
	@Transactional(readOnly = false)
	public void save(QaCheck qaCheck) {
		super.save(qaCheck);
	}
	
	@Transactional(readOnly = false)
	public void delete(QaCheck qaCheck) {
		super.delete(qaCheck);
	}
	
	public Integer getLastVersionByActId(String actId) {
		return qaCheckDao.getLastVersionByActId(actId, QaCheck.DEL_FLAG_NORMAL);
	}
	
	public Integer getCurrentVersionByActId(String actId) {
		return getLastVersionByActId(actId) + 1;
	}

	/**
	 * 查询最后一个版本的检查结果
	 * @param actId
	 * @return
	 */
	public List<QaCheck> findListByActIdAndVersion(String actId){
		return qaCheckDao.findListByActIdAndVersion(actId, getLastVersionByActId(actId), QaCheck.DEL_FLAG_NORMAL);
	}

	/**
	 * 判断资深员工是否已全部反馈
	 * @param actId
	 * @return
	 */
	public Integer findAllQaCheckIsFeedBack(String actId){
		return qaCheckDao.findAllQaCheckIsFeedBack(actId, getLastVersionByActId(actId), QaCheck.DEL_FLAG_NORMAL);
	}
}