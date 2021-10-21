/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;
import com.yum.ucp.modules.func.service.DscLinkFunctionService;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.service.SystemService;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.task.service.DscTaskLockService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 菜单Controller
 *
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

//    @Autowired
//    private CompleteScheduleConfig completeScheduleConfig;

    @Autowired
    private SystemService systemService;

    @Autowired
    private DscTaskLockService dscTaskLockService;

    @Autowired
    private DscLinkFunctionService dscLinkFunctionService;

    @ModelAttribute("menu")
    public Menu get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getMenu(id);
        } else {
            return new Menu();
        }
    }

    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/menuList";
    }


    @RequestMapping(value = "listByType")
    public String listByType(String parentId, Model model) {
        model.addAttribute("list", menuService.findMenusByParentIds(parentId, 3));
        return "modules/sys/menuListByType";
    }

    @RequestMapping(value = "findParentMenus")
    public String findParentMenus(String parentId, Model model) {
        model.addAttribute("list", menuService.findMenusByParentIds(parentId, 3));
        return "modules/sys/menuListByType";
    }
    @RequestMapping(value = "findSearchMenus")
    public String findSearchMenus(Menu menu, Model model) {
        model.addAttribute("list", menuService.findMenus());
        return "modules/sys/searchList";
    }

    @RequestMapping(value = "findWorkTimeMenus")
    public ModelAndView findWorkTimeMenus(Menu menu, Model model) {
        ModelAndView mav = new ModelAndView("modules/sys/workTimeConfig");
        Map<String,String> workTimeConfig = dscTaskLockService.findWorkTimeConfig();
        if(workTimeConfig!=null){
            mav.addObject("workTimeConfig",workTimeConfig);
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "updateMenu", method = RequestMethod.POST)
    public ResponseMessage updateMenu(String name, String href, String id) {
        Menu menu = menuService.get(id);
        menu.setId(id);
        menu.setName(name);
        menu.setHref(href);
        menuService.save(menu);
        return ResponseMessage.success(true);
    }

    @ResponseBody
    @RequestMapping(value = "getMenu")
    public ResponseMessage getMenu(Menu menu) {
        return ResponseMessage.success(menu);
    }


    @RequestMapping(value = "saveMenu", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage saveMenu(@RequestBody Menu menu) {
        Menu menuMenu = menuService.get(menu.getParentId());
        menu.setParentIds(menuMenu.getParentIds() + "," + menu.getParentId());
        menuService.save(menu);
        return ResponseMessage.success();
    }

    @RequestMapping(value = "form")
    public String form(Menu menu, Model model) {
        model.addAttribute("menu", menu);
        model.addAttribute("functionLinks",dscLinkFunctionService.findListByType("0"));
        model.addAttribute("linkIds",org.apache.commons.lang3.StringUtils.join(menuService.findLinkFunctionIds(menu.getId()), ","));
        return "modules/sys/searchForm";
    }

    @RequestMapping(value = "save")
    public String save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
        menuService.deleteBatchMenuLinkByMenuId(menu.getId());
        List<DscMenuFuncLink> dscMenuFuncLinks=new ArrayList<>();
        for(String linkId:menu.getLinkIds()){
            DscMenuFuncLink dscMenuFuncLink=new DscMenuFuncLink();
            dscMenuFuncLink.setMenuId(menu.getId());
            dscMenuFuncLink.setFunctionId(linkId);
            dscMenuFuncLinks.add(dscMenuFuncLink);
        }
        if(dscMenuFuncLinks.size()>0){
            menuService.insertBatchMenuLinkByMenuId(dscMenuFuncLinks);
        }

        return "redirect:" + adminPath + "/sys/menu/findSearchMenus";
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public ResponseMessage delete(Menu menu, RedirectAttributes redirectAttributes) {

        systemService.deleteMenu(menu);

        return ResponseMessage.success();
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "tree")
    public String tree() {
        return "modules/sys/menuTree";
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "treeselect")
    public String treeselect(String parentId, Model model) {
        model.addAttribute("parentId", parentId);
        return "modules/sys/menuTreeselect";
    }

    /**
     * 批量修改菜单排序
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "updateSort")
    public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/menu/";
        }
        for (int i = 0; i < ids.length; i++) {
            Menu menu = new Menu(ids[i]);
            menu.setSort(sorts[i]);
            systemService.updateMenuSort(menu);
        }
        addMessage(redirectAttributes, "保存菜单排序成功!");
        return "redirect:" + adminPath + "/sys/menu/";
    }

    @ResponseBody
    @RequestMapping(value = "addMenu")
    public ResponseMessage addMenu(String parentId) {
        Menu menu = menuService.get(parentId);
//		if(null!=menu&&menu.getMenuType().equals("1")){
//			return ResponseMessage.success(false);
//		}
        return ResponseMessage.success(true);
    }

    /**
     * isShowHide是否显示隐藏菜单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Menu> list = menuService.findAllMenus();
        for (int i = 0; i < list.size(); i++) {
            Menu e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     *工作时间配置
     *
     * @return
     *
     * @throws Exception
     */
//    @RequestMapping("workTimeConfig/{startDate}/{endDate}")
//    @ResponseBody
//    public ResponseMessage workTimeConfig(@PathVariable String startDate,@PathVariable String endDate) throws Exception
//    {
//        try{
//            String cron = null;
//            if(StringUtils.isNotEmpty(startDate)){
//                Date time = DateUtils.parseDate(startDate,"HH:mm:ss");
//                cron = "0 "+time.getMinutes()+" "+time.getHours()+" * * ?";
//            }
//            if(dscTaskLockService.findWorkTimeConfig()!=null){
//                dscTaskLockService.updateWorkTimeConfig(startDate,endDate,cron);
//            }else{
//                dscTaskLockService.insertWorkTimeConfig(startDate,endDate,cron);
//            }
//            //completeScheduleConfig.setCron();
//        }catch (RuntimeException e){
//            e.printStackTrace();
//            return ResponseMessage.error("保存失败");
//        }
//        return ResponseMessage.success("保存成功");
//    }
}
