/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.QaResult;

/**
 * 检查结果DAO接口
 * @author Zachary
 * @version 2019-08-16
 */
@MyBatisDao
public interface QaResultDao extends CrudDao<QaResult> {
	
	/**
	 * 根据活动ID获取最后一个结果的版本号
	 * @param actId
	 * @param delFlag
	 * @return
	 */
	Integer getLastVersionByActId(@Param("actId") String actId, @Param("delFlag") String delFlag);

	/**
	 * 查询最新的未通过的QA检查结果
	 * @param actId
	 * @param result
	 * @param delFlag
	 * @return
	 */
	QaResult findLastVersionByActId(@Param("actId") String actId, @Param("result") String result, @Param("delFlag") String delFlag);
}