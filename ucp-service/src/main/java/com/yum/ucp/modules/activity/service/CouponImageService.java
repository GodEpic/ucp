/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.service;

import java.util.Date;
import java.util.List;

import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.entity.CouponImage;
import com.yum.ucp.modules.activity.dao.CouponImageDao;

/**
 * 券图模块Service
 * @author Zachary
 * @version 2019-08-06
 */
@Service
@Transactional(readOnly = true)
public class CouponImageService extends CrudService<CouponImageDao, CouponImage> {
	
	@Autowired
	private CouponImageDao couponImageDao;
	
	public CouponImage get(String id) {
		return super.get(id);
	}
	
	public List<CouponImage> findList(CouponImage couponImage) {
		return super.findList(couponImage);
	}
	
	public Page<CouponImage> findPage(Page<CouponImage> page, CouponImage couponImage) {
		return super.findPage(page, couponImage);
	}
	
	@Transactional(readOnly = false)
	public void save(CouponImage couponImage) {
		super.save(couponImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(CouponImage couponImage) {
		super.delete(couponImage);
	}
	
	/**
	 * 
	 * @param actId
	 * @return
	 */
	public List<CouponImage> findListByActId(String actId){
		return couponImageDao.findListByActId(actId, CouponImage.DEL_FLAG_NORMAL);
	}

	public List<CouponImage> findListByActIdAndTaskId(CouponImage couponImage) {
		return couponImageDao.findListByActIdAndTaskId(couponImage);
	}

	public List<CouponImage> findUnsentList(CouponImage couponImage) {
		return couponImageDao.findUnsentList(couponImage);
	}

	@Transactional(readOnly = false)
	public void updateTaskId(CouponImage couponImage) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			couponImage.setUpdateBy(user);
		}
		couponImage.setUpdateDate(new Date());
		couponImageDao.updateTaskId(couponImage);
	}
}