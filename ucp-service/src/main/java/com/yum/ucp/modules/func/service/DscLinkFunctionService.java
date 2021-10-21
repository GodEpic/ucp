/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.func.dao.DscLinkFunctionDao;
import com.yum.ucp.modules.func.entity.DscLinkFunction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 对接功能明细Service
 * @author Edward.Luo
 * @version 2016-12-22
 */
@Service
@Transactional(readOnly = true)
public class DscLinkFunctionService extends CrudService<DscLinkFunctionDao, DscLinkFunction> {

	public DscLinkFunction get(String id) {
		return super.get(id);
	}
	
	public List<DscLinkFunction> findList(DscLinkFunction dscLinkFunction) {
		return super.findList(dscLinkFunction);
	}
	
	public Page<DscLinkFunction> findPage(Page<DscLinkFunction> page, DscLinkFunction dscLinkFunction) {
		return super.findPage(page, dscLinkFunction);
	}
	
	@Transactional(readOnly = false)
	public void save(DscLinkFunction dscLinkFunction) {
		super.save(dscLinkFunction);
	}
	
	@Transactional(readOnly = false)
	public void delete(DscLinkFunction dscLinkFunction) {
		super.delete(dscLinkFunction);
	}

	public List<DscLinkFunction> findListByType(String type) {
		return dao.findListByType(type);
	}

	public List<DscLinkFunction> findListByMenuId(List<String>  menuIds) {
		return dao.findListByMenuId(menuIds);
	}
}