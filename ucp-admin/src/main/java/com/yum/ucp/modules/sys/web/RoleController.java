/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.Collections3;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.sys.entity.Office;
import com.yum.ucp.modules.sys.entity.Role;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.service.OfficeService;
import com.yum.ucp.modules.sys.service.RoleService;
import com.yum.ucp.modules.sys.service.SystemService;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.ztree.NodeBean;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

    @Autowired
    private SystemService systemService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute("role")
    public Role get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getRole(id);
        } else {
            return new Role();
        }
    }

    @RequestMapping(value = {"list", ""})
    public String list(Role role, Model model, HttpServletRequest request, HttpServletResponse response) {
        Page<Role> page = roleService.findPage(new Page<Role>(request, response), role);
        model.addAttribute("page", page);
        return "modules/sys/roleList";
    }

    @RequestMapping(value = "form")
    public String form(Role role, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("menuList", menuService.findMenus());
        return "modules/sys/roleForm";
    }

    @RequestMapping(value = "save")
    public String save(Role role, Model model, RedirectAttributes redirectAttributes) {

        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/role/?repage";
        }
        if(StringUtils.isNotBlank(role.getAnnounce())){
            role.setAnnounce(StringEscapeUtils.unescapeHtml4(role.getAnnounce()));
        }
        roleService.save(role);
        roleService.updateMenu(role);

        addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
        return "redirect:" + adminPath + "/sys/role/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(Role role, RedirectAttributes redirectAttributes) {
        Integer count = roleService.findCountByRoleId(role);
        if(null!=count&&count>0){
            addMessage(redirectAttributes, "角色已经被引用不能删除");
            return "redirect:" + adminPath + "/sys/role/?repage";
        }
        roleService.delete(role);
        addMessage(redirectAttributes, "删除角色成功");
        return "redirect:" + adminPath + "/sys/role/?repage";
    }

    /**
     * 角色分配页面
     *
     * @param role
     * @param model
     * @return
     */
    @RequestMapping(value = "assign")
    public String assign(Role role, Model model) {
        List<User> userList = systemService.findUser(new User(new Role(role.getId())));
        model.addAttribute("userList", userList);
        return "modules/sys/roleAssign";
    }

    /**
     * 角色分配 -- 打开角色分配对话框
     *
     * @param role
     * @param model
     * @return
     */
    @RequestMapping(value = "usertorole")
    public String selectUserToRole(Role role, Model model) {
        List<User> userList = systemService.findUser(new User(new Role(role.getId())));
        model.addAttribute("role", role);
        model.addAttribute("userList", userList);
        model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
        model.addAttribute("officeList", officeService.findAll());
        return "modules/sys/selectUserToRole";
    }

    /**
     * 角色分配 -- 根据部门编号获取用户列表
     *
     * @param officeId
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "users")
    public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        User user = new User();
        user.setOffice(new Office(officeId));
        Page<User> page = systemService.findUser(new Page<User>(1, -1), user);
        for (User e : page.getList()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", 0);
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 角色分配 -- 从角色中移除用户
     *
     * @param userId
     * @param roleId
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "outrole")
    public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/role/assign?id=" + roleId;
        }
        Role role = systemService.getRole(roleId);
        User user = systemService.getUser(userId);
        if (UserUtils.getUser().getId().equals(userId)) {
            addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
        } else {
            if (user.getRoleList().size() <= 1) {
                addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
            } else {
//				Boolean flag = systemService.outUserInRole(role, user);
//				if (!flag) {
//					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
//				}else {
//					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
//				}
            }
        }
        return "redirect:" + adminPath + "/sys/role/assign?id=" + role.getId();
    }

    /**
     * 角色分配
     *
     * @param role
     * @param idsArr
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "assignrole")
    public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/role/assign?id=" + role.getId();
        }
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
//			User user = systemService.assignUserToRole(role, systemService.getUser(idsArr[i]));
//			if (null != user) {
//				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
//				newNum++;
//			}
        }
        addMessage(redirectAttributes, "已成功分配 " + newNum + " 个用户" + msg);
        return "redirect:" + adminPath + "/sys/role/assign?id=" + role.getId();
    }

    /**
     * 验证角色名是否有效
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(Role role) {
        Role entity = roleService.getRole(role);
        if (entity != null) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * 验证角色英文名是否有效
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkEnname")
    public String checkEnname(String oldEnname, String enname) {
//		if (enname!=null && enname.equals(oldEnname)) {
//			return "true";
//		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
//			return "true";
//		}
        return "false";
    }

    @ResponseBody
    @RequestMapping(value = "roleTree")
    public List<NodeBean> treeData(@RequestParam(required = false) String roleType, HttpServletResponse response) {
        List<NodeBean> list = roleService.getRoleByWorkCode(roleType);
        return list;
    }

}
