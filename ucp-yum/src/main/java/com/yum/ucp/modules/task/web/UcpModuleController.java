/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.service.UcpModuleService;

/**
 * 导入模块Controller
 * @author tony
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/task/ucpModule")
public class UcpModuleController extends DscBaseController {

	@Autowired
	private UcpModuleService ucpModuleService;
	
	@ModelAttribute
	public UcpModule get(@RequestParam(required=false) String id) {
		UcpModule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ucpModuleService.get(id);
		}
		if (entity == null){
			entity = new UcpModule();
		}
		return entity;
	}
	
	@RequiresPermissions("task:ucpModule:view")
	@RequestMapping(value = {"list", ""})
	public String list(UcpModule ucpModule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UcpModule> page = ucpModuleService.findPage(new Page<UcpModule>(request, response), ucpModule); 
		model.addAttribute("page", page);
		return "modules/task/ucpModuleList";
	}

	@RequiresPermissions("task:ucpModule:view")
	@RequestMapping(value = "form")
	public String form(UcpModule ucpModule, Model model) {
		model.addAttribute("ucpModule", ucpModule);
		return "modules/task/ucpModuleForm";
	}

	@RequiresPermissions("task:ucpModule:edit")
	@RequestMapping(value = "save")
	public String save(UcpModule ucpModule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ucpModule)){
			return form(ucpModule, model);
		}
		ucpModuleService.save(ucpModule);
		addMessage(redirectAttributes, "保存导入模块成功");
		return "redirect:"+Global.getAdminPath()+"/task/ucpModule/?repage";
	}
	
	@RequiresPermissions("task:ucpModule:edit")
	@RequestMapping(value = "delete")
	public String delete(UcpModule ucpModule, RedirectAttributes redirectAttributes) {
		ucpModuleService.delete(ucpModule);
		addMessage(redirectAttributes, "删除导入模块成功");
		return "redirect:"+Global.getAdminPath()+"/task/ucpModule/?repage";
	}
}