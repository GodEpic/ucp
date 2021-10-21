/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.sys.constants.CommonConstants;
import com.yum.ucp.modules.sys.entity.DscHotLine;
import com.yum.ucp.modules.sys.entity.DscHotLineConfig;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.service.DscHotLineConfigService;
import com.yum.ucp.modules.sys.service.DscHotLineService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * jira用户Controller
 *
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dscHotLine")
public class DscHotLineController extends BaseController {

    @Autowired
    private DscHotLineService dscHotLineService;

    @Autowired
    private DscHotLineConfigService dscHotLineConfigService;

    @ModelAttribute
    public DscHotLine get(@RequestParam(required = false) String id) {
        DscHotLine entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = dscHotLineService.get(id);
        }
        if (entity == null) {
            entity = new DscHotLine();
        }
        return entity;
    }

    @RequestMapping(value = {"list", ""})
    public String list(DscHotLine dscHotLine, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DscHotLine> page = dscHotLineService.findPage(new Page<DscHotLine>(request, response), dscHotLine);
        model.addAttribute("page", page);
        return "modules/sys/dscHotLineList";
    }

    @RequestMapping(value = "form")
    public String form(DscHotLine dscHotLine, Model model) {
        //所有case
        List<String> strs = dscHotLineService.findMenuIdById(dscHotLine.getId());
        model.addAttribute("menuIds", org.apache.commons.lang3.StringUtils.join(strs, ","));
        List<Menu> menuParents = menuService.findAllMenus();
        model.addAttribute("menuParents", menuParents);
        //热线配置
        List<String> configs = dscHotLineService.findConfigIdById(dscHotLine.getId());
        List<DscHotLineConfig> hotLineConfigMember=new ArrayList<>();
        List<DscHotLineConfig> hotLineConfigRes=new ArrayList<>();
        List<DscHotLineConfig> hotLineConfigHistory=new ArrayList<>();
        List<DscHotLineConfig> hotLineConfigs = dscHotLineConfigService.findList(new DscHotLineConfig());
        for (DscHotLineConfig dscHotLineConfig : hotLineConfigs) {
            if (dscHotLineConfig.getType().equals(CommonConstants.HOT_LINE_CONFIG.MEM_INFO)){
                hotLineConfigMember.add(dscHotLineConfig);
            }else if(dscHotLineConfig.getType().equals(CommonConstants.HOT_LINE_CONFIG.RES_INFO)){
                hotLineConfigRes.add(dscHotLineConfig);
            }else{
                hotLineConfigHistory.add(dscHotLineConfig);
            }
        }
        model.addAttribute("configIds", org.apache.commons.lang3.StringUtils.join(configs, ","));
        model.addAttribute("hotLineConfigMember", hotLineConfigMember);
        model.addAttribute("hotLineConfigRes", hotLineConfigRes);
        model.addAttribute("hotLineConfigHistory", hotLineConfigHistory);

        model.addAttribute("dscHotLine", dscHotLine);
        return "modules/sys/dscHotLineForm";
    }

    @RequestMapping(value = "save")
    public String save(DscHotLine dscHotLine, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, dscHotLine)) {
            return form(dscHotLine, model);
        }
        dscHotLineService.save(dscHotLine);
        addMessage(redirectAttributes, "保存热线成功");
        return "redirect:" + adminPath + "/sys/dscHotLine/list?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(DscHotLine dscHotLine, RedirectAttributes redirectAttributes) {
        dscHotLineService.delete(dscHotLine);
        addMessage(redirectAttributes, "删除jira用户成功");
        return "redirect:" + Global.getAdminPath() + "/sys/dscHotLine/?repage";
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName
     * @param name
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null && dscHotLineService.getRoleByNum(name) == null) {
            return "true";
        }
        return "false";
    }
}