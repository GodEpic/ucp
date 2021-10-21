package com.yum.ucp.modules.task.web;

import java.util.List;

import com.yum.ucp.modules.activity.entity.CouponImage;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import com.yum.ucp.modules.task.entity.ModuleActColumn;
import com.yum.ucp.modules.task.service.ModuleActColumnService;
import com.yum.ucp.modules.task.service.UcpTaskReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.constants.TaskConstants.TaskType;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.CouponImageService;
import com.yum.ucp.modules.activity.service.UcpActFileService;
import com.yum.ucp.modules.activity.utils.CouponImageUtils;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.service.ModuleActRowService;
import com.yum.ucp.modules.task.service.UcpTaskService;

/**
 * L1任务配置
 * @author Zachary
 * @version 2019-07-30
 */
@Controller
@RequestMapping(value = "${adminPath}/task/config")
public class TaskConfigController extends DscBaseController{
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private UcpTaskService ucpTaskService;
	@Autowired
	private UcpTaskReceiveService ucpTaskReceiveService;

	@Autowired
	private ModuleActRowService moduleActRowService;

	@Autowired
	private CouponImageService couponImageService;
	
	@Autowired
    private UcpActFileService actFileService;
	
	private static final String CONFIG_STATUS_COMPLETED = ReadConfig.getUcpFieldValue("config_status_completed");
	private static final String TASK_JIRA_STATUS_L2 = ReadConfig.getUcpFieldValue("task_jira_status_l2");
	
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
	public String configDetail(UcpTask task, Model model) {
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
		model.addAttribute("modRowLists", modRowLists);
		model.addAttribute("act", activityService.get(task.getActId()));
		model.addAttribute("task", task);
		model.addAttribute("taskReceive", ucpTaskReceiveService.getByTaskId(task.getId()));
		model.addAttribute("configType", TaskType.CONFIG.toString());
		model.addAttribute("couponImages", couponImageService.findListByActIdAndTaskId(new CouponImage(task.getActId(), task.getId())));
		
		model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));
		return "ucp/task/config/taskConfigDetail";
	}
	
	@RequestMapping(value = "updateTaskStatus", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateTaskStatus(UcpTask task) {
		Integer countRow = moduleActRowService.countByActIdAndVersion(new ModuleActRow(task.getActId(), task.getVersion(), "0", null));
		if(countRow > 0 ) {
			return ResponseMessage.error(ResponseErrorCode.ERROR_CONFIG_UNFINISHED.getCode(), ResponseErrorCode.ERROR_CONFIG_UNFINISHED.getDesc());
		}
    	final IssueRestClient client = getClient().getIssueClient();
    	try {
    		//更新流程状态
			updateIssueStatus(task.getJiraNo(), TASK_JIRA_STATUS_L2);
			// 保存数据到本地数据库
			task.setConfigStatus(CONFIG_STATUS_COMPLETED);
			task.setJiraStatus(TASK_JIRA_STATUS_L2);
			ucpTaskService.save(task);
		} catch (Exception e) {
			e.printStackTrace();
    		return ResponseMessage.error(ResponseErrorCode.SYS_ERROR.getCode(), ResponseErrorCode.SYS_ERROR.getDesc());
		}
    	return ResponseMessage.success(ResponseErrorCode.SUCCESS.getCode(), ResponseErrorCode.SUCCESS.getDesc());
    }
}
