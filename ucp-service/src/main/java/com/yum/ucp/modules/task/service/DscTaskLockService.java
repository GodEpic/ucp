/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.task.dao.DscTaskLockDao;
import com.yum.ucp.modules.task.entity.DscTaskLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 锁caseService
 * @author Edward.Luo
 * @version 2016-12-12
 */
@Service
@Transactional(readOnly = true)
public class DscTaskLockService extends CrudService<DscTaskLockDao, DscTaskLock> {
	@Autowired
	private DscTaskLockDao dscTaskLockDao;

	@Override
	public DscTaskLock get(String id) {
		return super.get(id);
	}

	public DscTaskLock getByCode(String key) {
		return dao.getByCode(key);
	}

	@Transactional
	public void updateLockStatus(String key){
		 dao.updateLockStatus(key);
	}

	@Override
	public List<DscTaskLock> findList(DscTaskLock dscTaskLock) {
		return super.findList(dscTaskLock);
	}

	@Override
	public Page<DscTaskLock> findPage(Page<DscTaskLock> page, DscTaskLock dscTaskLock) {
		return super.findPage(page, dscTaskLock);
	}
	
	@Transactional(readOnly = false)
	public void save(DscTaskLock dscTaskLock) {
		super.save(dscTaskLock);
	}
	
	@Transactional(readOnly = false)
	public void delete(DscTaskLock dscTaskLock) {
		super.delete(dscTaskLock);
	}

	public void updateByCode(String key){
		dscTaskLockDao.updateByCode(key);
	}

	public void updateCodeTime(String time,String key){
		dscTaskLockDao.updateCodeTime(time,key);
	}

	public void deleteByCode(String key){
		dscTaskLockDao.deleteByCode(key);
	}

	public void complainCode(String startTime,String endTime){
		dscTaskLockDao.complainCode(startTime,endTime);
	}

	public  void insertWorkTimeConfig(String startDate,String endDate,String cron){
		dscTaskLockDao.insertWorkTimeConfig(startDate,endDate,cron);
	}
	public  void updateWorkTimeConfig(String startDate,String endDate,String cron){
		dscTaskLockDao.updateWorkTimeConfig(startDate,endDate,cron);
	}
	public Map<String,String> findWorkTimeConfig(){
		return dscTaskLockDao.findWorkTimeConfig();
	}
	
}