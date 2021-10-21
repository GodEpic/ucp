/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.sys.entity.DscUserJira;
import com.yum.ucp.modules.sys.service.DscUserJiraService;
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
 * jira用户Controller
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dscUserJira")
public class DscUserJiraController extends BaseController {

	@Autowired
	private DscUserJiraService dscUserJiraService;
	
	@ModelAttribute
	public DscUserJira get(@RequestParam(required=false) String id) {
		DscUserJira entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dscUserJiraService.get(id);
		}
		if (entity == null){
			entity = new DscUserJira();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(DscUserJira dscUserJira, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DscUserJira> page = dscUserJiraService.findPage(new Page<DscUserJira>(request, response), dscUserJira); 
		model.addAttribute("page", page);
		return "modules/sys/dscUserJiraList";
	}

	@RequestMapping(value = "form")
	public String form(DscUserJira dscUserJira, Model model) {
		model.addAttribute("dscUserJira", dscUserJira);
		return "modules/sys/dscUserJiraForm";
	}

	@RequestMapping(value = "save")
	public String save(DscUserJira dscUserJira, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dscUserJira)){
			return form(dscUserJira, model);
		}
		dscUserJiraService.save(dscUserJira);
		addMessage(redirectAttributes, "保存jira用户成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(DscUserJira dscUserJira, RedirectAttributes redirectAttributes) {
		dscUserJiraService.delete(dscUserJira);
		addMessage(redirectAttributes, "删除jira用户成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dscUserJira/?repage";
	}

}