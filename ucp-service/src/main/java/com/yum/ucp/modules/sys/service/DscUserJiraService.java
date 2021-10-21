/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.sys.dao.DscUserJiraDao;
import com.yum.ucp.modules.sys.entity.DscUserJira;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * jira用户Service
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Service
@Transactional(readOnly = true)
public class DscUserJiraService extends CrudService<DscUserJiraDao, DscUserJira> {

	public DscUserJira get(String id) {
		return super.get(id);
	}
	
	public List<DscUserJira> findList(DscUserJira dscUserJira) {
		return super.findList(dscUserJira);
	}
	
	public Page<DscUserJira> findPage(Page<DscUserJira> page, DscUserJira dscUserJira) {
		return super.findPage(page, dscUserJira);
	}
	
	@Transactional(readOnly = false)
	public void save(DscUserJira dscUserJira) {
		if(!StringUtils.isNotEmpty(dscUserJira.getPassword()))
		{
			DscUserJira old = dao.get(dscUserJira.getId());
			if(null !=old) {
				dscUserJira.setPassword(old.getPassword());
			}
		}
		if(null ==dao.get(dscUserJira.getId()))
		{
			String id = dscUserJira.getId();
			dscUserJira.preInsert();
			dscUserJira.setId(id);
			dao.insert(dscUserJira);
		}
		else
		{
			dscUserJira.preUpdate();
			dao.update(dscUserJira);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(DscUserJira dscUserJira) {
		super.delete(dscUserJira);
	}
	
}