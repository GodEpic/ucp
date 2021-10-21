/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.entity.QaReport;
import com.yum.ucp.modules.activity.dao.QaReportDao;

/**
 * 测试报告Service
 * @author Zachary
 * @version 2019-08-15
 */
@Service
@Transactional(readOnly = true)
public class QaReportService extends CrudService<QaReportDao, QaReport> {

	@Autowired
	private QaReportDao qaReportDao;
	
	public QaReport get(String id) {
		return super.get(id);
	}
	
	public List<QaReport> findList(QaReport qaReport) {
		return super.findList(qaReport);
	}
	
	public Page<QaReport> findPage(Page<QaReport> page, QaReport qaReport) {
		return super.findPage(page, qaReport);
	}
	
	@Transactional(readOnly = false)
	public void save(QaReport qaReport) {
		super.save(qaReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(QaReport qaReport) {
		super.delete(qaReport);
	}
	
	public QaReport getByActId(String actId) {
		return qaReportDao.getByActId(actId, QaReport.DEL_FLAG_NORMAL);
	}
}