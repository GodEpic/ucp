/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.entity.CouponImage;
import com.yum.ucp.modules.activity.service.CouponImageService;
import com.yum.ucp.modules.activity.utils.CouponImageUtils;
import com.yum.ucp.modules.sys.utils.ResponseMessage;

/**
 * 券图模块Controller
 * @author Zachary
 * @version 2019-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/couponImage")
public class CouponImageController extends BaseController {

	@Autowired
	private CouponImageService couponImageService;
	
	@ModelAttribute
	public CouponImage get(@RequestParam(required=false) String id) {
		CouponImage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = couponImageService.get(id);
		}
		if (entity == null){
			entity = new CouponImage();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:couponImage:view")
	@RequestMapping(value = {"list", ""})
	public String list(CouponImage couponImage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CouponImage> page = couponImageService.findPage(new Page<CouponImage>(request, response), couponImage); 
		model.addAttribute("page", page);
		return "modules/activity/couponImageList";
	}

	@RequiresPermissions("activity:couponImage:view")
	@RequestMapping(value = "form")
	public String form(CouponImage couponImage, Model model) {
		model.addAttribute("couponImage", couponImage);
		return "modules/activity/couponImageForm";
	}

	@RequiresPermissions("activity:couponImage:edit")
	@RequestMapping(value = "save")
	public String save(CouponImage couponImage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, couponImage)){
			return form(couponImage, model);
		}
		couponImageService.save(couponImage);
		addMessage(redirectAttributes, "保存券图成功");
		return "redirect:"+Global.getAdminPath()+"/activity/couponImage/?repage";
	}
	
	@RequiresPermissions("activity:couponImage:edit")
	@RequestMapping(value = "delete")
	public String delete(CouponImage couponImage, RedirectAttributes redirectAttributes) {
		couponImageService.delete(couponImage);
		addMessage(redirectAttributes, "删除券图成功");
		return "redirect:"+Global.getAdminPath()+"/activity/couponImage/?repage";
	}

	@RequestMapping(value = "findByActId", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray findByActId(String actId) {
        return CouponImageUtils.findByActId(actId);
    }
	
	@RequestMapping(value = "deleteCouponImage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage deleteCouponImage(CouponImage couponImage) {
		try {
			couponImageService.delete(couponImage);
		} catch (Exception e) {
			logger.error("删除券图失败", e);
			return ResponseMessage.error("删除券图失败");
		}
        return ResponseMessage.success("删除券图成功");
    }
}