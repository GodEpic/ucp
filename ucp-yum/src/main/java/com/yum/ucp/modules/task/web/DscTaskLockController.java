/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.web;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.task.entity.DscTaskLock;
import com.yum.ucp.modules.task.service.DscTaskLockService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 锁caseController
 * @author Edward.Luo
 * @version 2016-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/task/dscTaskLock")
public class DscTaskLockController extends DscBaseController {

	@Autowired
	private DscTaskLockService dscTaskLockService;
	
	@ModelAttribute
	public DscTaskLock get(@RequestParam(required=false) String id) {
		DscTaskLock entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dscTaskLockService.get(id);
		}
		if (entity == null){
			entity = new DscTaskLock();
		}
		return entity;
	}
	
	@RequiresPermissions("task:dscTaskLock:view")
	@RequestMapping(value = {"list", ""})
	public String list(DscTaskLock dscTaskLock, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DscTaskLock> page = dscTaskLockService.findPage(new Page<DscTaskLock>(request, response), dscTaskLock); 
		model.addAttribute("page", page);
		return "modules/task/dscTaskLockList";
	}

	@RequiresPermissions("task:dscTaskLock:view")
	@RequestMapping(value = "form")
	public String form(DscTaskLock dscTaskLock, Model model) {
		model.addAttribute("dscTaskLock", dscTaskLock);
		return "modules/task/dscTaskLockForm";
	}

	@RequiresPermissions("task:dscTaskLock:edit")
	@RequestMapping(value = "save")
	public String save(DscTaskLock dscTaskLock, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dscTaskLock)){
			return form(dscTaskLock, model);
		}
		dscTaskLockService.save(dscTaskLock);
		addMessage(redirectAttributes, "保存case成功");
		return "redirect:"+Global.getAdminPath()+"/task/dscTaskLock/?repage";
	}
	
	@RequiresPermissions("task:dscTaskLock:edit")
	@RequestMapping(value = "delete")
	public String delete(DscTaskLock dscTaskLock, RedirectAttributes redirectAttributes) {
		dscTaskLockService.delete(dscTaskLock);
		addMessage(redirectAttributes, "删除case成功");
		return "redirect:"+Global.getAdminPath()+"/task/dscTaskLock/?repage";
	}

}