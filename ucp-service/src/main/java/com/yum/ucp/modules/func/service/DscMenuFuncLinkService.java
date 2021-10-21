/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.func.dao.DscMenuFuncLinkDao;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单与功能关联关系Service
 * @author Edward.Luo
 * @version 2016-12-22
 */
@Service
@Transactional(readOnly = true)
public class DscMenuFuncLinkService extends CrudService<DscMenuFuncLinkDao, DscMenuFuncLink> {

	public DscMenuFuncLink get(String id) {
		return super.get(id);
	}
	
	public List<DscMenuFuncLink> findList(DscMenuFuncLink dscMenuFuncLink) {
		return super.findList(dscMenuFuncLink);
	}
	
	public Page<DscMenuFuncLink> findPage(Page<DscMenuFuncLink> page, DscMenuFuncLink dscMenuFuncLink) {
		return super.findPage(page, dscMenuFuncLink);
	}
	
	@Transactional(readOnly = false)
	public void save(DscMenuFuncLink dscMenuFuncLink) {
		super.save(dscMenuFuncLink);
	}
	
	@Transactional(readOnly = false)
	public void delete(DscMenuFuncLink dscMenuFuncLink) {
		super.delete(dscMenuFuncLink);
	}
	
}