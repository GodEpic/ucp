/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import com.yum.ucp.modules.activity.dao.UcpActFileDao;

/**
 * 特殊活动附件关联Service
 * @author cherlin
 * @version 2019-08-23
 */
@Service
@Transactional(readOnly = true)
public class UcpActFileService extends CrudService<UcpActFileDao, UcpActFile> {

	@Override
	public UcpActFile get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<UcpActFile> findList(UcpActFile ucpActFile) {
		return super.findList(ucpActFile);
	}

	public List<UcpActFile> findByActId(String actId){
		return dao.findByActId(actId);
	}
	
	@Override
	public Page<UcpActFile> findPage(Page<UcpActFile> page, UcpActFile ucpActFile) {
		return super.findPage(page, ucpActFile);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(UcpActFile ucpActFile) {
		super.save(ucpActFile);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(UcpActFile ucpActFile) {
		super.delete(ucpActFile);
	}
	
}