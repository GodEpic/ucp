package com.yum.ucp.modules.activity.utils;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.activity.dao.CouponImageDao;
import com.yum.ucp.modules.activity.entity.CouponImage;

public class CouponImageUtils {
	
	private static CouponImageDao couponImageDao = SpringContextHolder.getBean(CouponImageDao.class);
	
	public static JSONArray findByActId(String actId) {
		JSONArray result = new JSONArray();
    	List<CouponImage> lists = couponImageDao.findListByActId(actId, CouponImage.DEL_FLAG_NORMAL);
    	if(lists != null && !lists.isEmpty()) {
    		for(CouponImage cp : lists) {
    			JSONObject imageObject = new JSONObject();
    			imageObject.put("tempId", System.currentTimeMillis());
    			imageObject.put("cpId", cp.getId());
//    			imageObject.put("taskId", cp.getTaskId());
    			imageObject.put("couponLink", cp.getCouponLink());
    			imageObject.put("couponDesc", cp.getCouponDesc());
    			imageObject.put("taskId", cp.getTaskId());
    			result.add(imageObject);
    		}
    	}
        return result;
    }
}
