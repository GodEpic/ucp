/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.CouponImage;

/**
 * 券图模块DAO接口
 * @author Zachary
 * @version 2019-08-06
 */
@MyBatisDao
public interface CouponImageDao extends CrudDao<CouponImage> {
	
	/**
	 * 
	 * @param actId
	 * @param delFlag
	 * @return
	 */
	List<CouponImage> findListByActId(@Param("actId") String actId, @Param("delFlag") String delFlag);

	/**
	 * 根据活动ID和任务ID查询券图
	 * @param couponImage
	 * @return
	 */
	List<CouponImage> findListByActIdAndTaskId(CouponImage couponImage);
	/**
	 * 查询未发送的券图
	 * @param couponImage
	 * @return
	 */
	List<CouponImage> findUnsentList(CouponImage couponImage);

	/**
	 * 更新券图的任务ID
	 * @param couponImage
	 */
	void updateTaskId(CouponImage couponImage);
}