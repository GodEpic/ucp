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
import com.yum.ucp.modules.task.entity.ModuleActValue;
import com.yum.ucp.modules.task.dao.ModuleActValueDao;

/**
 * 导入模块值Service
 * @author tony
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class ModuleActValueService extends CrudService<ModuleActValueDao, ModuleActValue> {

	@Autowired
	private ModuleActValueDao moduleActValueDao;
	
	public ModuleActValue get(String id) {
		return super.get(id);
	}
	
	public List<ModuleActValue> findList(ModuleActValue moduleActValue) {
		return super.findList(moduleActValue);
	}
	
	public Page<ModuleActValue> findPage(Page<ModuleActValue> page, ModuleActValue moduleActValue) {
		return super.findPage(page, moduleActValue);
	}
	
	@Transactional(readOnly = false)
	public void save(ModuleActValue moduleActValue) {
		super.save(moduleActValue);
	}
	
	@Transactional(readOnly = false)
	public void delete(ModuleActValue moduleActValue) {
		super.delete(moduleActValue);
	}
	
	public List<ModuleActValue> findRowDataByRowIdAndColumnName(String rowId){
		return moduleActValueDao.findRowDataByRowIdAndColumnName(rowId, ModuleActValue.DEL_FLAG_NORMAL);
	}

	public ModuleActValue getByColumnIdAndRowId(String columnId, String rowId){
		return moduleActValueDao.getByColumnIdAndRowId(columnId, rowId);
	}

	/**
	 * 导出测试任务
	 * @param rowId
	 * @param version
	 * @return
	 */
	public List<ModuleActValue> exportRowDataByActIdAndRowId(String actId, String rowId, Integer version){
		return moduleActValueDao.exportRowDataByActIdAndRowId(actId, rowId, version, ModuleActValue.DEL_FLAG_NORMAL);
	}
}