package com.yum.ucp.modules.task.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.constants.ActivityImportConstants;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.entity.CouponImage;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import com.yum.ucp.common.constants.TaskConstants.TaskType;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.CouponImageService;
import com.yum.ucp.modules.activity.service.UcpActFileService;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.task.entity.ModuleActRow;

/**
 * L2任务检查
 * @author Zachary
 * @version 2019-07-30
 */
@Controller
@RequestMapping(value = "${adminPath}/task/check")
public class TaskCheckController extends DscBaseController {
	
	@Autowired
	private ActivityService activityService;

	@Autowired
	private UcpModuleService ucpModuleService;

	@Autowired
	private ModuleActRowService moduleActRowService;

	@Autowired
	private ModuleActColumnService moduleActColumnService;

	@Autowired
	private UcpTaskService ucpTaskService;
	@Autowired
	private UcpTaskReceiveService ucpTaskReceiveService;
	@Autowired
	private CouponImageService couponImageService;
	
	@Autowired
    private UcpActFileService actFileService;

	private static final String VERIFY_STATUS_COMPLETED = ReadConfig.getUcpFieldValue("verify_status_completed");
	private static final String TASK_JIRA_STATUS_DONE = ReadConfig.getUcpFieldValue("task_jira_status_done");
	private static final String TASK_JIRA_STATUS_L1 = ReadConfig.getUcpFieldValue("task_jira_status_l1");

	@ModelAttribute
	public UcpTask get(@RequestParam(required=false) String id) {
		UcpTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ucpTaskService.get(id);
		}
		if (entity == null){
			entity = new UcpTask();
		}
		return entity;
	}

	@RequestMapping(value = {"detail", ""})
	public String checkDetail(UcpTask task, Model model) {
		List<ModuleActRow> modRowLists = moduleActRowService.findListByActIdAndVersion(new ModuleActRow(task.getActId(), task.getVersion()));
		JSONArray actFileData = new JSONArray();
        if (task != null && !StringUtils.isEmpty(task.getActId())) {
            List<UcpActFile> lists = actFileService.findByActId(task.getActId());
            if (!CollectionUtils.isEmpty(lists)) {
                lists.forEach(actFile -> {
                    JSONObject content = JSONObject.parseObject(actFile.getContent());
                    actFileData.add(content);
                });
            }
        }
        model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));
		
		model.addAttribute("modRowLists", modRowLists);
        model.addAttribute("couponCount", modRowLists != null ? modRowLists.size() : 0);
		model.addAttribute("act", activityService.get(task.getActId()));
		model.addAttribute("task", task);
		model.addAttribute("taskReceiveL1", ucpTaskReceiveService.getByTaskIdAndJiraStatus(task.getId(), TASK_JIRA_STATUS_L1));
		model.addAttribute("taskReceive", ucpTaskReceiveService.getByTaskId(task.getId()));
		model.addAttribute("checkType", TaskType.CHECK.toString());
		model.addAttribute("couponImages", couponImageService.findListByActIdAndTaskId(new CouponImage(task.getActId(), task.getId())));
		return "ucp/task/check/taskCheckDetail";
	}

	/**
	 * 预览导入的券信息
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("prewImportData")
	public String prewImportData(UcpTask task, Model model) {
		Integer version =  task.getVersion();

		model.addAttribute("sysLists", ucpModuleService.findSysByActIdAndVersion(task.getActId(), version, UcpModule.DEL_FLAG_NORMAL));
		model.addAttribute("modLists", ucpModuleService.findModByActIdAndVersion(task.getActId(), version, UcpModule.DEL_FLAG_NORMAL));
		model.addAttribute("rowLists", moduleActRowService.findListByActIdAndVersion(new ModuleActRow(task.getActId(), version)));
		model.addAttribute("columnLists", moduleActColumnService.findListByActIdAndVersion(task.getActId(), version, UcpModule.DEL_FLAG_NORMAL));
		model.addAttribute("dateType", ActivityImportConstants.ImportType.DATE.getValue());
		return "ucp/activity/prewImportData";
	}
	
	@RequestMapping(value = "addComment", method = RequestMethod.POST)
    @ResponseBody
	public ResponseMessage addComment(String comments, String taskId, HttpServletRequest request, HttpServletResponse response, Model model) {
		//comments = HtmlUtils.htmlUnescape(comments);
		
		return ResponseMessage.success();
	}

	@RequestMapping(value = "updateTaskStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage updateTaskStatus(UcpTask task) {
		Integer countRow = moduleActRowService.countByActIdAndVersion(new ModuleActRow(task.getActId(), task.getVersion(), null, "0"));
		if(countRow > 0 ) {
			return ResponseMessage.error(ResponseErrorCode.ERROR_CHECK_UNFINISHED.getCode(), ResponseErrorCode.ERROR_CHECK_UNFINISHED.getDesc());
		}
		try {
			// 更新流程状态
			// updateIssueStatus(task.getJiraNo(), TASK_JIRA_STATUS_DONE);
			// 保存数据到本地数据库
			task.setVerifyStatus(VERIFY_STATUS_COMPLETED);
			// task.setJiraStatus(TASK_JIRA_STATUS_DONE);
			ucpTaskService.save(task);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(ResponseErrorCode.SYS_ERROR.getCode(), ResponseErrorCode.SYS_ERROR.getDesc());
		}
		return ResponseMessage.success(ResponseErrorCode.SUCCESS.getCode(), ResponseErrorCode.SUCCESS.getDesc());
	}
}
