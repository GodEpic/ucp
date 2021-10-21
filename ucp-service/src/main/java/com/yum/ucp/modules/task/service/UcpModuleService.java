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
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.pojo.UcpModuleVO;
import com.yum.ucp.modules.task.dao.UcpModuleDao;

/**
 * 导入模块Service
 * @author tony
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class UcpModuleService extends CrudService<UcpModuleDao, UcpModule> {

	@Autowired
	private UcpModuleDao ucpModuleDao;
	
	public UcpModule get(String id) {
		return super.get(id);
	}
	
	public List<UcpModule> findList(UcpModule ucpModule) {
		return super.findList(ucpModule);
	}
	
	public Page<UcpModule> findPage(Page<UcpModule> page, UcpModule ucpModule) {
		return super.findPage(page, ucpModule);
	}
	
	@Transactional(readOnly = false)
	public void save(UcpModule ucpModule) {
		super.save(ucpModule);
	}
	
	@Transactional(readOnly = false)
	public void delete(UcpModule ucpModule) {
		super.delete(ucpModule);
	}
	
	/**
	 * 
	 * @param actId
	 * @param version
	 * @return
	 */
	public List<UcpModule> findByActIdAndVersion(String actId, String rowId, Integer version){
		return ucpModuleDao.findByActIdAndRowIdAndVersion(actId, rowId, version);
	}
	
	/**
	 * 
	 * @param actId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	public List<UcpModuleVO> findSysByActIdAndVersion(String actId, Integer version, String delFlag){
		return ucpModuleDao.findSysByActIdAndVersion(actId, version, delFlag);
	}
	
	/**
	 * 
	 * @param actId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	public List<UcpModuleVO> findModByActIdAndVersion(String actId, Integer version, String delFlag){
		return ucpModuleDao.findModByActIdAndVersion(actId, version, delFlag);
	}
}