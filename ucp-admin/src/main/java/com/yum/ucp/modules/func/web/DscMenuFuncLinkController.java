/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.web;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;
import com.yum.ucp.modules.func.service.DscMenuFuncLinkService;
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
 * 菜单与功能关联关系Controller
 * @author Edward.Luo
 * @version 2016-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/func/dscMenuFuncLink")
public class DscMenuFuncLinkController extends BaseController {

	@Autowired
	private DscMenuFuncLinkService dscMenuFuncLinkService;
	
	@ModelAttribute
	public DscMenuFuncLink get(@RequestParam(required=false) String id) {
		DscMenuFuncLink entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dscMenuFuncLinkService.get(id);
		}
		if (entity == null){
			entity = new DscMenuFuncLink();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(DscMenuFuncLink dscMenuFuncLink, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DscMenuFuncLink> page = dscMenuFuncLinkService.findPage(new Page<DscMenuFuncLink>(request, response), dscMenuFuncLink); 
		model.addAttribute("page", page);
		return "modules/sys/dscMenuFuncLinkList";
	}

	@RequestMapping(value = "form")
	public String form(DscMenuFuncLink dscMenuFuncLink, Model model) {
		model.addAttribute("dscMenuFuncLink", dscMenuFuncLink);
		return "modules/sys/dscMenuFuncLinkForm";
	}

	@RequestMapping(value = "save")
	public String save(DscMenuFuncLink dscMenuFuncLink, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dscMenuFuncLink)){
			return form(dscMenuFuncLink, model);
		}
		dscMenuFuncLinkService.save(dscMenuFuncLink);
		addMessage(redirectAttributes, "保存菜单与功能关联关系成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dscMenuFuncLink/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(DscMenuFuncLink dscMenuFuncLink, RedirectAttributes redirectAttributes) {
		dscMenuFuncLinkService.delete(dscMenuFuncLink);
		addMessage(redirectAttributes, "删除菜单与功能关联关系成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dscMenuFuncLink/?repage";
	}

}