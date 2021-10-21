/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.service;

import java.util.List;

import com.yum.ucp.modules.activity.dao.QaCheckDao;
import com.yum.ucp.modules.activity.entity.QaCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.dao.ModuleActRowDao;

/**
 * 导入模块行Service
 * @author tony
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class ModuleActRowService extends CrudService<ModuleActRowDao, ModuleActRow> {
	
	@Autowired
	private ModuleActRowDao moduleActRowDao;

	@Autowired
	private QaCheckDao qaCheckDao;
	
	public ModuleActRow get(String id) {
		return super.get(id);
	}
	
	public List<ModuleActRow> findList(ModuleActRow moduleActRow) {
		return super.findList(moduleActRow);
	}
	
	public Page<ModuleActRow> findPage(Page<ModuleActRow> page, ModuleActRow moduleActRow) {
		return super.findPage(page, moduleActRow);
	}
	
	@Transactional(readOnly = false)
	public void save(ModuleActRow moduleActRow) {
		super.save(moduleActRow);
	}
	
	@Transactional(readOnly = false)
	public void delete(ModuleActRow moduleActRow) {
		super.delete(moduleActRow);
	}
	
	public List<ModuleActRow> findListByActIdAndVersion(ModuleActRow row){
		return moduleActRowDao.findListByActIdAndVersion(row);
	}

	public ModuleActRow getByActIdAndValueAndVersion(String actId, String value, Integer version){
		return moduleActRowDao.getByActIdAndValueAndVersion(actId, value, version);
	}

	public Integer countByActIdAndVersion(ModuleActRow row) {
		return moduleActRowDao.countByActIdAndVersion(row);
	}
	
	public Integer getModuleRowLastVersionByActId(String actId) {
		return moduleActRowDao.getModuleRowLastVersionByActId(actId, ModuleActRow.DEL_FLAG_NORMAL);
	}

	public List<ModuleActRow> findModuleRowQaResult(String actId, Integer checkVersion){
		if(checkVersion == 0){
			checkVersion = qaCheckDao.getLastVersionByActId(actId, QaCheck.DEL_FLAG_NORMAL);
		}
		Integer rowVersion = moduleActRowDao.getModuleRowLastVersionByActId(actId, ModuleActRow.DEL_FLAG_NORMAL);
		return moduleActRowDao.findModuleRowQaResult(actId, checkVersion, rowVersion,ModuleActRow.DEL_FLAG_NORMAL);
	}
}