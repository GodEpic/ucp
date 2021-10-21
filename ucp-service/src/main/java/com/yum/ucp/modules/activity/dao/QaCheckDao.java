/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.QaCheck;

import java.util.List;

/**
 * 反馈报告DAO接口
 * @author Zachary
 * @version 2019-08-16
 */
@MyBatisDao
public interface QaCheckDao extends CrudDao<QaCheck> {

	/**
	 * 根据活动ID获取最后一个结果的版本号
	 * @param actId
	 * @param delFlag
	 * @return
	 */
	Integer getLastVersionByActId(@Param("actId") String actId, @Param("delFlag") String delFlag);

	/**
	 *
	 * @param actId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	List<QaCheck> findListByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version,  @Param("delFlag") String delFlag);

	/**
	 * 判断资深员工是否已全部反馈
	 * @param actId
	 * @param delFlag
	 * @return
	 */
	Integer findAllQaCheckIsFeedBack(@Param("actId") String actId, @Param("version") Integer version, @Param("delFlag") String delFlag);
}