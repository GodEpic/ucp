/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.QaReport;

/**
 * 测试报告DAO接口
 * @author Zachary
 * @version 2019-08-15
 */
@MyBatisDao
public interface QaReportDao extends CrudDao<QaReport> {
	
	/**
	 * 
	 * @param actId
	 * @param delFlag
	 * @return
	 */
	QaReport getByActId(@Param("actId") String actId, @Param("delFlag") String delFlag); 
}