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
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.task.entity.ModuleActColumn;
import com.yum.ucp.modules.task.service.ModuleActColumnService;

/**
 * 导入模块列Controller
 * @author tony
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/task/moduleActColumn")
public class ModuleActColumnController extends BaseController {

	@Autowired
	private ModuleActColumnService moduleActColumnService;
	
	@ModelAttribute
	public ModuleActColumn get(@RequestParam(required=false) String id) {
		ModuleActColumn entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moduleActColumnService.get(id);
		}
		if (entity == null){
			entity = new ModuleActColumn();
		}
		return entity;
	}
	
	@RequiresPermissions("task:moduleActColumn:view")
	@RequestMapping(value = {"list", ""})
	public String list(ModuleActColumn moduleActColumn, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ModuleActColumn> page = moduleActColumnService.findPage(new Page<ModuleActColumn>(request, response), moduleActColumn); 
		model.addAttribute("page", page);
		return "modules/task/moduleActColumnList";
	}

	@RequiresPermissions("task:moduleActColumn:view")
	@RequestMapping(value = "form")
	public String form(ModuleActColumn moduleActColumn, Model model) {
		model.addAttribute("moduleActColumn", moduleActColumn);
		return "modules/task/moduleActColumnForm";
	}

	@RequiresPermissions("task:moduleActColumn:edit")
	@RequestMapping(value = "save")
	public String save(ModuleActColumn moduleActColumn, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moduleActColumn)){
			return form(moduleActColumn, model);
		}
		moduleActColumnService.save(moduleActColumn);
		addMessage(redirectAttributes, "保存导入模块列成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActColumn/?repage";
	}
	
	@RequiresPermissions("task:moduleActColumn:edit")
	@RequestMapping(value = "delete")
	public String delete(ModuleActColumn moduleActColumn, RedirectAttributes redirectAttributes) {
		moduleActColumnService.delete(moduleActColumn);
		addMessage(redirectAttributes, "删除导入模块列成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActColumn/?repage";
	}

}