/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.entity.Attach;
import com.yum.ucp.modules.sys.dao.AttachDao;

/**
 * 附件Service
 * @author Zachary
 * @version 2019-08-15
 */
@Service
@Transactional(readOnly = true)
public class AttachService extends CrudService<AttachDao, Attach> {

	@Autowired
	private AttachDao attachDao;

	public Attach get(String id) {
		return super.get(id);
	}
	
	public List<Attach> findList(Attach attach) {
		return super.findList(attach);
	}
	
	public Page<Attach> findPage(Page<Attach> page, Attach attach) {
		return super.findPage(page, attach);
	}
	
	@Transactional(readOnly = false)
	public void save(Attach attach) {
		super.save(attach);
	}
	
	@Transactional(readOnly = false)
	public void delete(Attach attach) {
		super.delete(attach);
	}

	public List<Attach> findListByClassAndType(Attach attach){
		return attachDao.findListByClassAndType(attach);
	}

	/**
	 *
	 * @param attach
	 * @return
	 */
	public Attach getByClassNameAndClassPk(Attach attach){
		return attachDao.getByClassNameAndClassPk(attach);
	}

	public Attach getByFilePath(String filePath){
		return attachDao.getByFilePath(filePath);
	}

	public Attach getByFileName(String fileName){
		return attachDao.getByFileName(fileName);
	}

	@Transactional(readOnly = false)
	public void deleteByFilePath(Attach attach){
		attachDao.deleteByFilePath(attach);
	}

	@Transactional(readOnly = false)
	public void deleteByClassNameAndClassPkAndType(Attach attach){
		attachDao.deleteByClassNameAndClassPkAndType(attach);
	}
}