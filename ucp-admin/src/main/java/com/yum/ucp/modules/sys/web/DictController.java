/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.utils.JedisUtils;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.listener.SelectDataInit;
import com.yum.ucp.modules.sys.service.DictService;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 字典Controller
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    @ModelAttribute
    public Dict get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return dictService.get(id);
        } else {
            return new Dict();
        }
    }

    @RequestMapping(value = {""})
    public String index() {
        return "modules/sys/dictIndex";
    }

    @RequestMapping(value = "list")
    public String list(Dict dict, Model model) {
        model.addAttribute("list", dictService.findList(dict));
        return "modules/sys/dictList1";
    }

//    @RequestMapping(value = "list")
//    public String list() {
//        return "modules/sys/dictMenu";
//    }

    @RequestMapping(value = "flush")
    public String flush(HttpServletRequest httpServletRequest, Model model) {
        try {
            // 删除redis中的选择项数据
            Jedis jedis = null;
            jedis = JedisUtils.getResource();
            jedis.del("selectData");
            // 重新加载选择项数据
            new SelectDataInit().initSelect(httpServletRequest.getSession().getServletContext());
            model.addAttribute("message", "刷新成功");
        } catch (Exception e) {
            model.addAttribute("message", "刷新失败");
        }
        return "modules/sys/flush";
    }

//    @RequestMapping(value = "listByType")
//    public String listByType(String type, String parentId, Model model) {
//        Dict dict = new Dict();
//        dict.setType(type);
//        dict.setParentId(parentId);
//        model.addAttribute("list", dictService.findList(dict));
//        return "modules/sys/dictList";
//    }

    @ResponseBody
    @RequestMapping(value = "updateDict")
    public ResponseMessage updateDict(Dict dict) {
        dictService.updateDict(dict);
        return ResponseMessage.success(true);
    }

    @ResponseBody
    @RequestMapping(value = "getDict")
    public ResponseMessage getDict(Dict dict) {
        return ResponseMessage.success(dict);
    }

//    @ResponseBody
//    @RequestMapping(value = "addDict")
//    public ResponseMessage addDict(String parentId) {
//        Dict dict=new Dict();
//        dict.setParentId(parentId);
//        dict.setType("1");
//        List<Dict> dicts=dictService.findList(dict);
//        if(null!=dicts&&dicts.size()>0){
//            return ResponseMessage.success(false);
//        }
//
//        return ResponseMessage.success(true);
//    }

    @RequestMapping(value = "saveDict")
    @ResponseBody
    public ResponseMessage saveDict(Dict dict) {
        Dict dictMenu = dictService.get(dict.getParentId());
        dict.setParentIds(dictMenu.getParentIds() + "," + dict.getParentId());
        dictService.save(dict);
        return ResponseMessage.success();
    }

    @RequestMapping(value = "form")
    public String form(Dict dict, Model model) {
        if (dict.getParent() != null && dict.getParent().getId() != null) {
            dict.setParent(dictService.get(dict.getParent().getId()));
        }
        model.addAttribute("dict", dict);
        return "modules/sys/dictForm";
    }

    @RequestMapping(value = "save")//@Valid
    public String save(Dict dict, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/dict/?repage&type=" + dict.getType();
        }
        if (!beanValidator(model, dict)) {
            return form(dict, model);
        }
        dictService.save(dict);
        addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
//        return "redirect:" + adminPath + "/sys/dict/list?repage&type=" + dict.getType();
        String id = "0".equals(dict.getParentId()) ? "" : dict.getParentId();
        return "redirect:" + adminPath + "/sys/dict/list?id="+id+"&parentIds="+dict.getParentIds();
    }

//    @RequestMapping(value = "delete")
//    @ResponseBody
//    public ResponseMessage delete(Dict dict) {
//        Dict dict1 = new Dict();
//        dict1.setParentId(dict.getId());
//        List<Dict> dicts = dictService.findList(dict1);
//        if(null!=dicts&&dicts.size()>0){
//            return ResponseMessage.error("此节点下有数据不能删除");
//        }
//        dictService.delete(dict);
//
//        return ResponseMessage.success();
//    }

    @RequestMapping(value = "delete")
    public String delete(Dict dict, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/dict/?repage";
        }
        dictService.delete(dict);
        addMessage(redirectAttributes, "删除字典成功");
        return "redirect:" + adminPath + "/sys/dict/list?repage&type="+dict.getType();
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String type, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Dict dict = new Dict();
//        dict.setType("1");
        List<Dict> list = dictService.findAllList(dict);
        for (int i = 0; i < list.size(); i++) {
            Dict e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();

            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("pIds", e.getParentIds());
            map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

}
