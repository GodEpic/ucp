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
import com.yum.ucp.modules.task.entity.ModuleActColumn;
import com.yum.ucp.modules.task.dao.ModuleActColumnDao;

/**
 * 导入模块列Service
 * @author tony
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class ModuleActColumnService extends CrudService<ModuleActColumnDao, ModuleActColumn> {

	@Autowired
	private ModuleActColumnDao dao;
	
	public ModuleActColumn get(String id) {
		return super.get(id);
	}
	
	public List<ModuleActColumn> findList(ModuleActColumn moduleActColumn) {
		return super.findList(moduleActColumn);
	}
	
	public Page<ModuleActColumn> findPage(Page<ModuleActColumn> page, ModuleActColumn moduleActColumn) {
		return super.findPage(page, moduleActColumn);
	}
	
	@Transactional(readOnly = false)
	public void save(ModuleActColumn moduleActColumn) {
		super.save(moduleActColumn);
	}
	
	@Transactional(readOnly = false)
	public void delete(ModuleActColumn moduleActColumn) {
		super.delete(moduleActColumn);
	}

	public ModuleActColumn getByActIdAndNameAndVersion(String actId, String name, Integer version){
		return dao.getByActIdAndNameAndVersion(actId, name, version);
	}

	public List<ModuleActColumn> findListByActIdAndVersion(String actId, Integer version, String delFlag){
		return dao.findListByActIdAndVersion(actId, version, delFlag);
	}
}