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
import com.yum.ucp.modules.task.entity.ModuleActValue;
import com.yum.ucp.modules.task.service.ModuleActValueService;

/**
 * 导入模块值Controller
 * @author tony
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/task/moduleActValue")
public class ModuleActValueController extends BaseController {

	@Autowired
	private ModuleActValueService moduleActValueService;
	
	@ModelAttribute
	public ModuleActValue get(@RequestParam(required=false) String id) {
		ModuleActValue entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moduleActValueService.get(id);
		}
		if (entity == null){
			entity = new ModuleActValue();
		}
		return entity;
	}
	
	@RequiresPermissions("task:moduleActValue:view")
	@RequestMapping(value = {"list", ""})
	public String list(ModuleActValue moduleActValue, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ModuleActValue> page = moduleActValueService.findPage(new Page<ModuleActValue>(request, response), moduleActValue); 
		model.addAttribute("page", page);
		return "modules/task/moduleActValueList";
	}

	@RequiresPermissions("task:moduleActValue:view")
	@RequestMapping(value = "form")
	public String form(ModuleActValue moduleActValue, Model model) {
		model.addAttribute("moduleActValue", moduleActValue);
		return "modules/task/moduleActValueForm";
	}

	@RequiresPermissions("task:moduleActValue:edit")
	@RequestMapping(value = "save")
	public String save(ModuleActValue moduleActValue, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moduleActValue)){
			return form(moduleActValue, model);
		}
		moduleActValueService.save(moduleActValue);
		addMessage(redirectAttributes, "保存导入模块值成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActValue/?repage";
	}
	
	@RequiresPermissions("task:moduleActValue:edit")
	@RequestMapping(value = "delete")
	public String delete(ModuleActValue moduleActValue, RedirectAttributes redirectAttributes) {
		moduleActValueService.delete(moduleActValue);
		addMessage(redirectAttributes, "删除导入模块值成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActValue/?repage";
	}

}