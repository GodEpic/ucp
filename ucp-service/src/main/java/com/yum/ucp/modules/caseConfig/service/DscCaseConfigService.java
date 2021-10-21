/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.caseConfig.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.caseConfig.dao.DscCaseConfigDao;
import com.yum.ucp.modules.caseConfig.entity.DscCaseConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * case配置Service
 * @author Edward.Luo
 * @version 2016-12-12
 */
@Service
@Transactional(readOnly = true)
public class DscCaseConfigService extends CrudService<DscCaseConfigDao, DscCaseConfig> {

	public DscCaseConfig get(String id) {
		return super.get(id);
	}
	
	public List<DscCaseConfig> findList(DscCaseConfig dscCaseConfig) {
		return super.findList(dscCaseConfig);
	}
	public List<DscCaseConfig> findListByType(String caseType) {
		return dao.findListByType(caseType);
	}
	public Page<DscCaseConfig> findPage(Page<DscCaseConfig> page, DscCaseConfig dscCaseConfig) {
		return super.findPage(page, dscCaseConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(DscCaseConfig dscCaseConfig) {
		super.save(dscCaseConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(DscCaseConfig dscCaseConfig) {
		super.delete(dscCaseConfig);
	}
	
}