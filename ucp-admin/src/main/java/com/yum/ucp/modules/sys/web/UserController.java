/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.utils.excel.ExportExcel;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.sys.entity.DscUserJira;
import com.yum.ucp.modules.sys.entity.Office;
import com.yum.ucp.modules.sys.entity.Role;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.service.DscUserJiraService;
import com.yum.ucp.modules.sys.service.RoleService;
import com.yum.ucp.modules.sys.service.SystemService;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import com.yum.ucp.modules.work.service.DscWorkCodeService;
import com.yum.ucp.modules.ztree.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private DscUserJiraService dscUserJiraService;
	@Autowired
	private DscWorkCodeService dscWorkCodeService;

	@Autowired
	private RoleService roleService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	@RequestMapping(value = {""})
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	@RequestMapping(value = {"list"})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
		return "modules/sys/userList";
	}

	@RequestMapping(value = {"workCodeList"})
	public String workCodeList(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		user = systemService.getUser(user.getId());
		DscWorkCode dscWorkCode = new DscWorkCode();
		dscWorkCode.setUser(user);
		Page<DscWorkCode> page = dscWorkCodeService.getUserWorkCode(new Page<DscWorkCode>(request, response), dscWorkCode);
		model.addAttribute("page", page);
		model.addAttribute("u", user);
		return "modules/sys/workCodeList";
	}
	
	@ResponseBody
	@RequestMapping(value = {"listData"})
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		return page;
	}

	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		
		logger.info("user:{}",user);
		
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/userForm";
	}

	@RequestMapping(value = "jira")
	public String jira(DscUserJira user, Model model) {
		DscUserJira result = dscUserJiraService.get(user.getId());
		if(null==result)
		{
			result = new DscUserJira();
			result.setId(user.getId());
		}
		model.addAttribute("user", result);
		model.addAttribute("u", systemService.getUser(user.getId()));
		return "modules/sys/jiraForm";
	}

	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		}else{
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }



	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	/**
	 * 返回用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}
	
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	@RequestMapping(value = "workCodeForm")
	public String workCodeForm(User user, Model model) {
		user = systemService.getUser(user.getId());
		DscWorkCode workCode = new DscWorkCode();
		workCode.setUser(user);
		model.addAttribute("user", user);
		model.addAttribute("workCode", workCode);
		return "modules/sys/workCodeForm";
	}


	@RequestMapping(value = "workCodeEdit")
	public String workCodeEdit(DscWorkCode workCode, Model model) {
		workCode = dscWorkCodeService.get(workCode.getId());
		List<String> list = dscWorkCodeService.getWorkCodeRole(workCode.getId());
		List<NodeBean> nodes = roleService.getRoleByWorkCode(workCode.getRoleType());
		workCode.setRoleIdList(list);
		User user = systemService.getUser(workCode.getUser().getId());
		workCode.setUser(user);
		model.addAttribute("workCode", workCode);
		model.addAttribute("roles", nodes);
		model.addAttribute("user", user);
		model.addAttribute("roleIds", org.apache.commons.lang3.StringUtils.join(list, ","));
		return "modules/sys/workCodeEdit";
	}


	@RequestMapping(value = "saveWorkCode")
	public String saveWorkCode(DscWorkCode workCode, Model model) {
		dscWorkCodeService.saveWorkCode(workCode);
		User user = systemService.getUser(workCode.getUser().getId());
		return "redirect:" + adminPath + "/sys/user/workCodeList?repage&id="+user.getId();
	}


	@RequestMapping(value = "deleteWorkCode")
	public String deleteWorkCode(DscWorkCode workCode, Model model) {
		workCode = dscWorkCodeService.get(workCode.getId());
		dscWorkCodeService.deleteWorkCode(workCode);
		User user = systemService.getUser(workCode.getUser().getId());
		return "redirect:" + adminPath + "/sys/user/workCodeList?repage&id="+user.getId();
	}


	@ResponseBody
	@RequestMapping(value = "checkWorkCode")
	public String checkWorkCode(String oldWorkCode,String workCode) {
		if (workCode!=null && workCode.equals(oldWorkCode)) {
			return "true";
		} else if (workCode!=null && dscWorkCodeService.getByWorkCode(workCode) == null) {
			return "true";
		}
		return "false";
	}
}
