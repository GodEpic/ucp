/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.web;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.func.entity.DscLinkFunction;
import com.yum.ucp.modules.func.service.DscLinkFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对接功能明细Controller
 * @author Edward.Luo
 * @version 2016-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/func/dscLinkFunction")
public class DscLinkFunctionController extends BaseController {

	@Autowired
	private DscLinkFunctionService dscLinkFunctionService;
	
	@ModelAttribute
	public DscLinkFunction get(@RequestParam(required=false) String id) {
		DscLinkFunction entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dscLinkFunctionService.get(id);
		}
		if (entity == null){
			entity = new DscLinkFunction();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(DscLinkFunction dscLinkFunction, HttpServletRequest request, HttpServletResponse response, Model model) {
		dscLinkFunction=new DscLinkFunction();
		dscLinkFunction.setFunType("1");
		Page<DscLinkFunction> page = dscLinkFunctionService.findPage(new Page<DscLinkFunction>(request, response), dscLinkFunction);
		model.addAttribute("page", page);
		return "modules/func/funcList";
	}

	@RequestMapping(value = "form")
	public String form(DscLinkFunction dscLinkFunction, Model model) {
		model.addAttribute("dscLinkFunction", dscLinkFunction);
		return "modules/func/funcForm";
	}

	@RequestMapping(value = "save")
	public String save(DscLinkFunction dscLinkFunction, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dscLinkFunction)){
			return form(dscLinkFunction, model);
		}
		dscLinkFunctionService.save(dscLinkFunction);
		addMessage(redirectAttributes, "保存对接功能明细成功");
		return "redirect:"+Global.getAdminPath()+"/func/dscLinkFunction/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(DscLinkFunction dscLinkFunction, RedirectAttributes redirectAttributes) {
		dscLinkFunctionService.delete(dscLinkFunction);
		addMessage(redirectAttributes, "删除对接功能明细成功");
		return "redirect:"+Global.getAdminPath()+"/func/dscLinkFunction/?repage";
	}

}