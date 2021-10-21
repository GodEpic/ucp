/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yum.ucp.modules.task.entity.ModuleActColumn;
import com.yum.ucp.modules.task.entity.ModuleActValue;
import com.yum.ucp.modules.task.pojo.TaskSysVO;
import com.yum.ucp.modules.task.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.constants.TaskConstants.ModuleActRowStatus;
import com.yum.ucp.common.constants.TaskConstants.TaskType;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.UcpModule;

/**
 * 导入模块行Controller
 * @author tony
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/task/moduleActRow")
public class ModuleActRowController extends BaseController {

	@Autowired
	private UcpModuleService ucpModuleService;

	@Autowired
	private ModuleActColumnService moduleActColumnService;

	@Autowired
	private ModuleActRowService moduleActRowService;

	@Autowired
	private ModuleActValueService moduleActValueService;

	@Autowired
	private UcpTaskReceiveService ucpTaskReceiveService;

	private static final String TASK_ACTIVITY_ID = "activityid";
	private static final String TASK_TEST_ACCOUNT = "testaccount";
	
	@ModelAttribute
	public ModuleActRow get(@RequestParam(required=false) String id) {
		ModuleActRow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moduleActRowService.get(id);
		}
		if (entity == null){
			entity = new ModuleActRow();
		}
		return entity;
	}
	
	@RequiresPermissions("task:moduleActRow:view")
	@RequestMapping(value = {"list", ""})
	public String list(ModuleActRow moduleActRow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ModuleActRow> page = moduleActRowService.findPage(new Page<ModuleActRow>(request, response), moduleActRow); 
		model.addAttribute("page", page);
		return "ucp/task/moduleActRowList";
	}

	@RequiresPermissions("task:moduleActRow:view")
	@RequestMapping(value = "form")
	public String form(ModuleActRow moduleActRow, Model model) {
		model.addAttribute("moduleActRow", moduleActRow);
		return "ucp/task/moduleActRowForm";
	}

	@RequiresPermissions("task:moduleActRow:edit")
	@RequestMapping(value = "save")
	public String save(ModuleActRow moduleActRow, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moduleActRow)){
			return form(moduleActRow, model);
		}
		moduleActRowService.save(moduleActRow);
		addMessage(redirectAttributes, "保存导入模块行成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActRow/?repage";
	}
	
	@RequiresPermissions("task:moduleActRow:edit")
	@RequestMapping(value = "delete")
	public String delete(ModuleActRow moduleActRow, RedirectAttributes redirectAttributes) {
		moduleActRowService.delete(moduleActRow);
		addMessage(redirectAttributes, "删除导入模块行成功");
		return "redirect:"+Global.getAdminPath()+"/task/moduleActRow/?repage";
	}

	
	@RequestMapping(value = {"rowList", ""})
	public String rowList(ModuleActRow moduleActRow, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ModuleActRow> lists = moduleActRowService.findListByActIdAndVersion(moduleActRow);
		model.addAttribute("lists", lists);
		return "ucp/task/moduleActRowList";
	}
	
	@RequestMapping(value = {"rowDetail", ""})
	public String rowDetail(ModuleActRow moduleActRow, String type, String taskId, HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer currentVersion = moduleActRow.getVersion();
		List<UcpModule> modList = ucpModuleService.findByActIdAndVersion(moduleActRow.getActId(), moduleActRow.getId(), currentVersion);
		
		Map<String, TaskSysVO> sysMap = new LinkedHashMap<String, TaskSysVO>();
		Map<String, List<UcpModule>> modMap = new LinkedHashMap<String, List<UcpModule>>();
		
		if(modList != null && !modList.isEmpty()) {
			for(UcpModule um : modList) {
				if(!sysMap.containsKey(um.getSysId())) {
					TaskSysVO ts = new TaskSysVO();
					ts.setSysId(um.getSysId());
					ts.setSysName(um.getSysName());
					ts.setSysCount(um.getChangeCount());
					sysMap.put(um.getSysId(), ts);
				} else {
					TaskSysVO ts = sysMap.get(um.getSysId());
					ts.setSysCount(ts.getSysCount() + um.getChangeCount());
					sysMap.put(um.getSysId(), ts);
				}
				
				if(modMap.containsKey(um.getSysId())) {
					modMap.get(um.getSysId()).add(um);
				} else {
					List<UcpModule> list = new ArrayList<UcpModule>();
					list.add(um);
					modMap.put(um.getSysId(), list);
				}
			}
		}
		model.addAttribute("sysMap", sysMap);
		model.addAttribute("modMap", modMap);
		model.addAttribute("row", moduleActRow);
		model.addAttribute("type", type);
		model.addAttribute("configType", TaskType.CONFIG.toString());
		model.addAttribute("checkType", TaskType.CHECK.toString());
		model.addAttribute("rowStatus", ModuleActRowStatus.PENDING.getAbbr());
		model.addAttribute("taskReceive", ucpTaskReceiveService.getByTaskId(taskId));
		if(type.equals(TaskType.CONFIG.toString())){
			model.addAttribute("inputVal", getModuleValueByColumn(TASK_ACTIVITY_ID, moduleActRow.getActId(), moduleActRow.getId(), moduleActRow.getConfigCertifiCenter(), currentVersion));
		} else {
			model.addAttribute("inputVal", getModuleValueByColumn(TASK_TEST_ACCOUNT, moduleActRow.getActId(), moduleActRow.getId(), moduleActRow.getConfigCertifiCenter(), currentVersion));
		}

		return "ucp/task/moduleActRowDetail";
	}

	private String getModuleValueByColumn(String columnName, String actId, String rowId, String rowValue, Integer currentVersion){
		String inputVal = "";
		ModuleActColumn mac = moduleActColumnService.getByActIdAndNameAndVersion(actId, columnName, currentVersion);
		if(mac != null){
			ModuleActValue mav = moduleActValueService.getByColumnIdAndRowId(mac.getId(), rowId);

			if(mav != null && StringUtils.isNotBlank(mav.getValue())){
				inputVal = mav.getValue();
			} else if(currentVersion > 1){
				Integer prevVersion = currentVersion - 1;

				ModuleActRow row = moduleActRowService.getByActIdAndValueAndVersion(actId, rowValue, prevVersion);
				mac = moduleActColumnService.getByActIdAndNameAndVersion(actId, columnName, prevVersion);
				if(mac != null && row != null){
					mav = moduleActValueService.getByColumnIdAndRowId(mac.getId(), row.getId());
					if(mav != null){
						inputVal = mav.getValue();
					}
				}
			}
		}
		return inputVal;
	}
	
	@RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateStatus(String rowId, String type, String inputVal, HttpServletRequest request, HttpServletResponse response) {
    	
    	JSONObject result = new JSONObject();
    	
    	try {
    		result.put("errcode", ResponseErrorCode.SUCCESS.getCode());
    		result.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());
    		String columnName = "";
    		ModuleActRow row = moduleActRowService.get(new ModuleActRow(rowId));
    		if(type.equalsIgnoreCase(TaskType.CONFIG.name())) {
    			row.setConfigDate(new Date());
    			row.setConfigStatus(ModuleActRowStatus.COMPLETED.getAbbr());
				columnName = TASK_ACTIVITY_ID;
    		} else if(type.equalsIgnoreCase(TaskType.CHECK.name())) {
    			row.setCheckDate(new Date());
    			row.setCheckStatus(ModuleActRowStatus.COMPLETED.getAbbr());
    			columnName = TASK_TEST_ACCOUNT;
    		}
    		moduleActRowService.save(row);

			saveTaskActivityOrAccount(columnName, row.getActId(), row.getId(), row.getVersion(), inputVal);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errcode", ResponseErrorCode.SYS_ERROR.getCode());
    		result.put("errmsg", ResponseErrorCode.SYS_ERROR.getDesc());
		}
    	return ResponseMessage.success(result);
    }

    private void saveTaskActivityOrAccount(String columnName, String actId, String rowId, Integer currentVersion, String inputVal){
		if(StringUtils.isNotBlank(columnName)){
			ModuleActColumn mac = moduleActColumnService.getByActIdAndNameAndVersion(actId, columnName, currentVersion);
			if(mac != null){
				ModuleActValue mav = moduleActValueService.getByColumnIdAndRowId(mac.getId(), rowId);

				if(mav != null){
					mav.setValue(inputVal);
					moduleActValueService.save(mav);
				}
			}
		}
	}
}