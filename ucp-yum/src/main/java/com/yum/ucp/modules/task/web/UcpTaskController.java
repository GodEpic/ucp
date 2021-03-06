/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.constants.ActivityConstants;
import com.yum.ucp.common.persistence.BaseEntity;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.entity.CouponImage;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.CouponImageService;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.entity.Role;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.utils.DictUtils;
import com.yum.ucp.modules.sys.utils.MessageUtils;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.sys.utils.SessionUtils;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.entity.UcpTaskReceive;
import com.yum.ucp.modules.task.pojo.UcpTaskVO;
import com.yum.ucp.modules.task.service.ModuleActRowService;
import com.yum.ucp.modules.task.service.UcpTaskReceiveService;
import com.yum.ucp.modules.task.service.UcpTaskService;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import com.yum.ucp.modules.work.service.DscWorkCodeService;

/**
 * ????????????Controller
 * @author tony
 * @version 2019-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/task/ucpTask")
public class UcpTaskController extends DscBaseController {

	@Autowired
	private UcpTaskService ucpTaskService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ModuleActRowService moduleActRowService;
	@Autowired
	private UcpTaskReceiveService ucpTaskReceiveService;
	@Autowired
	private CouponImageService couponImageService;
	@Autowired
	private DscWorkCodeService dscWorkCodeService;

	private static final String TASK_ISSUE_TYPE_ID = ReadConfig.getUcpFieldValue("taskIssueTypeId");
    private static final String CONFIG_STATUS_UNCLAIMED = ReadConfig.getUcpFieldValue("config_status_unclaimed");
    private static final String VERIFY_STATUS_UNCLAIMED = ReadConfig.getUcpFieldValue("verify_status_unclaimed");
	private static final String CONFIG_STATUS_UNFINISHED = ReadConfig.getUcpFieldValue("config_status_unfinished");
	private static final String VERIFY_STATUS_UNFINISHED = ReadConfig.getUcpFieldValue("verify_status_unfinished");
	private static final String CONFIG_STATUS_COMPLETED = ReadConfig.getUcpFieldValue("config_status_completed");
	private static final String VERIFY_STATUS_COMPLETED = ReadConfig.getUcpFieldValue("verify_status_completed");
    private static final String TASK_JIRA_STATUS_SENIOR = ReadConfig.getUcpFieldValue("task_jira_status_senior");
    private static final String TASK_JIRA_STATUS_L1 = ReadConfig.getUcpFieldValue("task_jira_status_l1");
    private static final String TASK_JIRA_STATUS_L2 = ReadConfig.getUcpFieldValue("task_jira_status_l2");
	private static final String TASK_JIRA_STATUS_DONE = ReadConfig.getUcpFieldValue("task_jira_status_done");
	private static final String ACTIVITY_JIRA_STATUS_QA = ReadConfig.getUcpFieldValue("activity_jira_status_qa");

	private static final String  L2_TO_DONE = ReadConfig.getUcpFieldValue("L2_TO_DONE");
	private static final String  SENIOR_TO_L1L2 = ReadConfig.getUcpFieldValue("SENIOR_TO_L1L2");
	private static final String  L1_TO_QA = ReadConfig.getUcpFieldValue("L1_TO_QA");
	private static final String  SENIOR_TO_L1 = ReadConfig.getUcpFieldValue("SENIOR_TO_L1");
	
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
	
	public void list(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
		ucpTaskVO.setJiraStatusL1(TASK_JIRA_STATUS_L1);
		ucpTaskVO.setJiraStatusL2(TASK_JIRA_STATUS_L2);
		User user = UserUtils.getUser();
		if(user.getBrand()!=null && !"".equals(user.getBrand())) {
			ucpTaskVO.setBrand(user.getBrand());
        }
		Page<UcpTaskVO> page = ucpTaskService.findTaskPage(new Page<UcpTaskVO>(request, response), ucpTaskVO);
		model.addAttribute("page", page);
		model.addAttribute("activityName", ucpTaskVO.getActivityName());
		model.addAttribute("configStatus", ucpTaskVO.getConfigStatus());
		model.addAttribute("verifyStatus", ucpTaskVO.getVerifyStatus());
		model.addAttribute("searchTxt",ucpTaskVO.getSearchTxt());
		
		model.addAttribute("CONFIG_STATUS_UNCLAIMED", CONFIG_STATUS_UNCLAIMED);
		// ????????????jira??????-??????????????????
		model.addAttribute("SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN", SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN);
		// ??????jira??????-DONE
		model.addAttribute("ACTIVITY_JIRA_STATUS_DONE", ACTIVITY_JIRA_STATUS_DONE);
	}

    @RequestMapping("dealListL1")
    public String dealListL1(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
//        ucpTaskVO.setJiraStatus(TASK_JIRA_STATUS_L1);
		if(ucpTaskVO.getConfigStatus() == null) {
			ucpTaskVO.setConfigStatus(CONFIG_STATUS_UNCLAIMED);
		}
        list(ucpTaskVO, request, response, model);
        return "ucp/task/config/taskConfigList";
    }

    @RequestMapping("dealListL2")
    public String dealListL2(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
        ucpTaskVO.setJiraStatus(TASK_JIRA_STATUS_L2);
		//ucpTaskVO.setJiraStatusReceive(TASK_JIRA_STATUS_L2);
		// ??????????????????????????????????????????????????????
		if (VERIFY_STATUS_UNFINISHED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setType("1");
			// ????????????jira??????-??????????????????
			ucpTaskVO.setActivityJiraStatus(SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		} else if (VERIFY_STATUS_COMPLETED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setType("1");
			// ??????jira??????-??????
			ucpTaskVO.setActivityJiraStatus(ACTIVITY_JIRA_STATUS_DONE);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		}
		// ?????????????????????????????????????????????
        if(ucpTaskVO.getVerifyStatus() == null) {
			ucpTaskVO.setVerifyStatus(VERIFY_STATUS_UNCLAIMED);
		}
		// ?????????????????????????????????jira???????????????DONE
		else if(VERIFY_STATUS_COMPLETED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setJiraStatus(null);
		}
		// ???????????????????????????????????????
		else if(StringUtils.isBlank(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setJiraStatus(null);
			ucpTaskVO.setActivityJiraStatus(null);
			ucpTaskVO.setType("1");
			ucpTaskVO.setConfigStatus(CONFIG_STATUS_COMPLETED);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		}
        list(ucpTaskVO, request, response, model);
        return "ucp/task/check/taskCheckList";
    }
    
    
    @RequestMapping("dealListL3")
    public String dealListL3(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
        ucpTaskVO.setJiraStatus(TASK_JIRA_STATUS_L2);
		ucpTaskVO.setType("1");
		ucpTaskVO.setVerifyStatus("13023");
		// ??????jira??????-??????
		ucpTaskVO.setActivityJiraStatus(ACTIVITY_JIRA_STATUS_DONE);
		// ????????????????????????????????????
		ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		ucpTaskVO.setJiraStatus(null);
        list(ucpTaskVO, request, response, model);
        return "ucp/task/check/taskConfigList";
    }

	@RequestMapping("dealKeyValueListL2")
	public String dealKeyValueListL2(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
		ucpTaskVO.setJiraStatus(TASK_JIRA_STATUS_L2);
		// ?????????????????????????????????????????????
		if(ucpTaskVO.getVerifyStatus() == null) {
			ucpTaskVO.setVerifyStatus(VERIFY_STATUS_UNFINISHED);
		}
		// ??????????????????????????????????????????????????????
		if (VERIFY_STATUS_UNFINISHED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setType(ActivityConstants.ActivityType.KEY_VALUE.getType());
			// ????????????jira??????-??????????????????
			ucpTaskVO.setActivityJiraStatus(SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		} else if (VERIFY_STATUS_COMPLETED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setType(ActivityConstants.ActivityType.KEY_VALUE.getType());
			// ??????jira??????-??????
			ucpTaskVO.setActivityJiraStatus(ACTIVITY_JIRA_STATUS_DONE);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		}
		// ?????????????????????????????????jira???????????????DONE
		else if(VERIFY_STATUS_COMPLETED.equals(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setJiraStatus(null);
		}
		// ???????????????????????????????????????
		else if(StringUtils.isBlank(ucpTaskVO.getVerifyStatus())) {
			ucpTaskVO.setJiraStatus(null);
			ucpTaskVO.setActivityJiraStatus(null);
			ucpTaskVO.setType(ActivityConstants.ActivityType.KEY_VALUE.getType());
			ucpTaskVO.setConfigStatus(CONFIG_STATUS_COMPLETED);
			// ????????????????????????????????????
			ucpTaskVO.setReceive(SessionUtils.getPhoneAgent());
		}
		list(ucpTaskVO, request, response, model);
		return "ucp/task/check/taskKeyValueCheckList";
	}

    @RequestMapping("allList")
    public String allList(UcpTaskVO ucpTaskVO, HttpServletRequest request, HttpServletResponse response, Model model) {
        list(ucpTaskVO, request, response, model);
        return "ucp/task/allCaseList";
    }

	@RequestMapping(value = "form")
	public String form(UcpTask ucpTask, Model model) {
		model.addAttribute("ucpTask", ucpTask);
		return "modules/task/ucpTaskForm";
	}

	@RequestMapping(value = "save")
	public String save(UcpTask ucpTask, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ucpTask)){
			return form(ucpTask, model);
		}
		ucpTaskService.save(ucpTask);
		addMessage(redirectAttributes, "??????????????????");
		return "redirect:"+Global.getAdminPath()+"/task/ucpTask/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(UcpTask ucpTask, RedirectAttributes redirectAttributes) {
		// ???????????????????????????????????????
		boolean canDelete = false;
		List<Dict> deletePermissionPersonList = DictUtils.getDictList("deletePermissionPersons");
		for (Dict dict : deletePermissionPersonList) {
			if (dict.getValue().equals(SessionUtils.getUserLoginName())) {
				canDelete = true;
				break;
			}
		}
		// ??????????????????????????????
		String configStatus = ucpTask.getConfigStatus();
		if (CONFIG_STATUS_UNCLAIMED.equals(configStatus) || canDelete) {
			String key = ucpTask.getJiraNo();
			final IssueRestClient client = getClient().getIssueClient();
			try {
				if (StringUtils.isNoneBlank(key)) {
					client.deleteIssue(key, false);
				}
				ucpTaskService.delete(ucpTask);
				addMessage(redirectAttributes, "??????????????????");
			} catch (Exception e) {
				logger.error("jira??????????????????:", e);
				addErrorMessage(redirectAttributes, "??????????????????");
			}
		} else {
			addErrorMessage(redirectAttributes, "??????????????????????????????");
		}
		return "redirect:" + adminPath + "/task/ucpTask/allList?repage&actId=" + ucpTask.getActId();
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAndCreateTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage saveAndCreateTask(Activity activity) throws Exception {
	    // ???????????????????????????????????????????????????????????????
		String actId = activity.getId();

		Integer version = ucpTaskService.getCurrentVersion(actId, "0");
		Integer countRow = moduleActRowService.countByActIdAndVersion(new ModuleActRow(actId, version));
		List<CouponImage> couponImageList = couponImageService.findUnsentList(new CouponImage(actId, null));
        if(countRow < 1 && (couponImageList != null && couponImageList.size() < 1)) {
			return ResponseMessage.error("?????????????????????????????????????????????????????????");
		}
		String parentJiraNo = activity.getJiraNo();
		BasicIssue promise;
		String projectKey = UCP;
		final IssueRestClient client = getClient().getIssueClient();
		try {
			IssueType issueType = getIssueType(TASK_ISSUE_TYPE_ID);
			if (null != issueType) {
				IssueInputBuilder builder = new IssueInputBuilder(projectKey, issueType.getId());
//					Field[] fields = ucpTask.getClass().getFields();
//					buildCase(fields, ucpTask, builder);
				builder.setSummary(activity.getSummary());
//				builder.setFieldValue(ReadConfig.getUcpFieldValue("configStatus"), ComplexIssueInputFieldValue.with("id", CONFIG_STATUS_UNCLAIMED));
//				builder.setFieldValue(ReadConfig.getUcpFieldValue("verifyStatus"), ComplexIssueInputFieldValue.with("id", VERIFY_STATUS_UNCLAIMED));
				builder.setParent(parentJiraNo);
				final IssueInput input = builder.build();
				promise = client.createIssue(input).claim();
				String key = promise.getKey();
				//?????????????????????????????????????????????????????????
				updateIssueStatus(activity.getJiraNo(), SENIOR_TO_L1L2);
				//???????????????????????????????????????L1??????
				updateIssueStatus(key, SENIOR_TO_L1);
				// ??????????????????????????????
				UcpTask ucpTask = new UcpTask();
				ucpTask.setJiraNo(key);
				ucpTask.setParentJiraNo(parentJiraNo);
				ucpTask.setActId(activity.getId());
				ucpTask.setConfigStatus(CONFIG_STATUS_UNCLAIMED);
				ucpTask.setVerifyStatus(VERIFY_STATUS_UNCLAIMED);
				ucpTask.setJiraStatus(TASK_JIRA_STATUS_L1);
				ucpTask.setVersion(version);
				ucpTaskService.save(ucpTask);
				// ?????????????????????ID
				couponImageService.updateTaskId(new CouponImage(actId, ucpTaskService.getByJiraNo(key).getId()));

				if(StringUtils.isEmpty(activity.getStatus())){
					Activity nActivity=activityService.get( activity.getId());
					activity.setStatus(nActivity.getStatus());
				}
				// ???????????????????????????
				activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
				activity.setJiraStatus(ACTIVITY_JIRA_STATUS_L1);
				activityService.save(activity);
			}

		} catch (RestClientException e) {
			logger.error("??????????????????", e);
			return ResponseMessage.error(MessageUtils.returnErrors(e.getErrorCollections(), findAllFields()));
		}
		return ResponseMessage.success("??????????????????", activity);
	}

	/**
	 * ???????????????????????????
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	private ResponseMessage saveAndCreateUrgentTask(Activity activity) throws Exception {
		// ?????????????????????????????????????????????????????????
		updateIssueStatus(activity.getJiraNo(), SENIOR_TO_ASSIGN);
		// ???????????????????????????
		activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
		activity.setJiraStatus(SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN);
		DscWorkCode dscWorkCode = dscWorkCodeService.getByWorkCode(activity.getReceiveUser());
		String roleType = "";
		if (dscWorkCode != null) {
			roleType = dscWorkCode.getRoleType();
		}
		if (ROLE_TYPE_QA.equals(roleType)) {
			activity.setStatus(ACTIVITY_STATUS_TESTING);
		} else {
			activity.setStatus(ACTIVITY_STATUS_CONFIGING);
		}
		activityService.save(activity);
		return ResponseMessage.success("??????????????????", activity);
	}

	/**
	 * ???????????????????????????????????????
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "createTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage createTask(String id) throws Exception {
		Activity activity = activityService.get(id);
		if(ActivityConstants.ActivityType.URGENT.getType().equals(activity.getType())
				|| ActivityConstants.ActivityType.KEY_VALUE.getType().equals(activity.getType())){
			return saveAndCreateUrgentTask(activity);
		}
		return saveAndCreateTask(activity);
	}

	/**
	 * ???????????????????????????????????????L1?????????
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "release", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage release(Activity activity) throws Exception {
		final IssueRestClient client = getClient().getIssueClient();
		Issue issue = client.getIssue(activity.getJiraNo()).claim();
		//??????????????????
		TransitionInput tinput = new TransitionInput(Integer.parseInt(SENIOR_TO_L1));
		client.transition(issue, tinput);
		return ResponseMessage.success(true);
	}

	/**
	 * ????????????
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private boolean receive(UcpTask task, String id, String jiraStatus, RedirectAttributes redirectAttributes) throws Exception {
		// ??????????????????
		UcpTaskReceive ucpTaskReceive = new UcpTaskReceive();
		ucpTaskReceive.setTaskId(id);
		ucpTaskReceive.setJiraStatus(jiraStatus);
		// ????????????????????????
		Integer receiveCount = ucpTaskReceiveService.countReceiveByTaskIdAndJiraStatus(ucpTaskReceive);
		if(receiveCount > 0) {
			addErrorMessage(redirectAttributes, "?????????????????????");
			return false;
		}
		// ????????????????????????????????????
		Integer lastVersion = task.getVersion() - 1;
		if (lastVersion > 0) {
			UcpTask lastTask = new UcpTask();
			lastTask.setActId(task.getActId());
			lastTask.setVersion(lastVersion);
			lastTask = ucpTaskService.getByActIdAndVersion(lastTask);
			if(lastTask.getDelFlag().equals(UcpModule.DEL_FLAG_NORMAL)) {
				String configStatus = lastTask.getConfigStatus();
				String verifyStatus = lastTask.getVerifyStatus();
				// jira?????????L1?????????????????????????????????????????????????????????????????????????????????
				if (TASK_JIRA_STATUS_L1.equals(jiraStatus) && !CONFIG_STATUS_COMPLETED.equals(configStatus)) {
					addErrorMessage(redirectAttributes, "??????????????????????????????");
					return false;
				}
				// jira?????????L2?????????????????????????????????????????????????????????????????????????????????
				if (TASK_JIRA_STATUS_L2.equals(jiraStatus) && !VERIFY_STATUS_COMPLETED.equals(verifyStatus)) {
					addErrorMessage(redirectAttributes, "??????????????????????????????");
					return false;
				}
			}
		}

		ucpTaskReceive.setReceive(SessionUtils.getPhoneAgent());
		ucpTaskReceive.setReceiveTime(new Date());
		ucpTaskReceiveService.save(ucpTaskReceive);
		addMessage(redirectAttributes, "????????????");
		return true;
	}

	/**
	 * ????????????L1
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "receiveL1")
	public String receiveL1(String id, RedirectAttributes redirectAttributes) throws Exception {
		UcpTask task = ucpTaskService.get(id);
		boolean isSuccess = receive(task, id, TASK_JIRA_STATUS_L1, redirectAttributes);
		if(isSuccess) {
			// ????????????????????????????????????????????????????????????????????????
			if(task == null) {
				addErrorMessage(redirectAttributes, "???????????????");
			} else {
				if(CONFIG_STATUS_UNCLAIMED.equals(task.getConfigStatus())) {
					task.setConfigStatus(CONFIG_STATUS_UNFINISHED);
					ucpTaskService.save(task);
				}
				// ??????????????????????????????????????????????????????
				Activity activity = activityService.get(task.getActId());
				if(ACTIVITY_STATUS_UNCLAIMED.equals(activity.getStatus())) {
					activity.setStatus(ACTIVITY_STATUS_CONFIGING);
					activityService.save(activity);
				}
			}

		}
		return "redirect:" + adminPath + "/task/ucpTask/dealListL1?configStatus=" + CONFIG_STATUS_UNCLAIMED + "&repage";
	}



	/**
	 * ????????????L1
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "withdrawL1")
	public String withdrawL1(String id, RedirectAttributes redirectAttributes) throws Exception {
		UcpTask task = ucpTaskService.get(id);
		UcpTaskReceive del = ucpTaskReceiveService.getByTaskId(id);
		del.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		ucpTaskReceiveService.delete(del);
			task.setConfigStatus(CONFIG_STATUS_UNCLAIMED);
			ucpTaskService.save(task);
		Activity activity = activityService.get(task.getActId());
		activity.setStatus(ACTIVITY_STATUS_UNCLAIMED);
		activityService.save(activity);


		return "redirect:" + adminPath + "/task/ucpTask/dealListL1?configStatus=" + CONFIG_STATUS_UNCLAIMED + "&repage";
	}
	/**
	 * ????????????L2
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "receiveL2")
	public String receiveL2(String id, RedirectAttributes redirectAttributes) throws Exception {
		UcpTask task = ucpTaskService.get(id);
		boolean isSuccess = receive(task, id, TASK_JIRA_STATUS_L2, redirectAttributes);
		if(isSuccess) {
			// ????????????????????????????????????????????????????????????????????????
			if(VERIFY_STATUS_UNCLAIMED.equals(task.getVerifyStatus())) {
				task.setVerifyStatus(VERIFY_STATUS_UNFINISHED);
				ucpTaskService.save(task);
			}
		}
		return "redirect:" + adminPath + "/task/ucpTask/dealListL2?verifyStatus=" + VERIFY_STATUS_UNCLAIMED + "&repage";
	}

	@RequestMapping(value = "sendToQA")
	public String sendToQA(UcpTask task, RedirectAttributes redirectAttributes, Model model) {
		// ??????????????????????????????????????????
		boolean canSendToQA = true;
		List<UcpTask> taskList = ucpTaskService.findList(task);
		for (UcpTask ucpTask : taskList) {
			if(VERIFY_STATUS_COMPLETED.equals(ucpTask.getVerifyStatus())) {
				updateIssueStatus(ucpTask.getJiraNo(), L2_TO_DONE);
				ucpTask.setJiraStatus(TASK_JIRA_STATUS_DONE);
				ucpTaskService.save(ucpTask);
			} else {
				canSendToQA = false;
				addErrorMessage(redirectAttributes, "????????????????????????????????????????????????????????????????????????QA");
				break;
			}
		}
		if(canSendToQA) {
			// ?????????????????????QA
			Activity activity = activityService.get(task.getActId());
			updateIssueStatus(activity.getJiraNo(), L1_TO_QA);
			activity.setJiraStatus(ACTIVITY_JIRA_STATUS_QA);
			activity.setStatus(ACTIVITY_STATUS_NOT_TESTED);
			activity.setL2Submitter(SessionUtils.getPhoneAgent());
			activityService.save(activity);
			addMessage(redirectAttributes, "????????????");
		}
//		model.addAttribute("verifyStatus", VERIFY_STATUS_COMPLETED);
		return "redirect:" + adminPath + "/task/ucpTask/dealListL2?verifyStatus=" + VERIFY_STATUS_COMPLETED + "&repage";
	}
	
	@RequestMapping("configConfirm")
	@ResponseBody
	public ResponseMessage config(String actId) {
		
		User user = UserUtils.getUser();
		
		if(!"??????????????????".equals(user.getName())){
			return ResponseMessage.error("????????????????????????");
		}
		
		Activity activity = activityService.get(actId);
		activity.setConfiger(user.getId());
		activity.setConfigdate(new Date());
		activityService.save(activity);
		
		return ResponseMessage.success("????????????");
	}

/*	public void buildCase(Field[] fields, UcpTask ucpTask, IssueInputBuilder builder) throws Exception {
		for (Field field : fields) {
			if (null != field.get(ucpTask) && org.apache.commons.lang3.StringUtils.isNotBlank(ReadConfig.getUcpFieldValue(field.getName()))) {
				SelectAnnotation selectAnnotation = field.getAnnotation(SelectAnnotation.class);
				SelectOneAnnotation selectOneAnnotation = field.getAnnotation(SelectOneAnnotation.class);
				IgnoreAnnotation ignoreAnnotation = field.getAnnotation(IgnoreAnnotation.class);
				if (null == ignoreAnnotation) {
					if (null != selectAnnotation) {
						builder.setFieldValue(ReadConfig.getUcpFieldValue(field.getName()), ComplexIssueInputFieldValue.with("id", field.get(ucpTask)));
					} else if (null != selectOneAnnotation) {
						Map<String, Object> parent = new HashMap<>(16);
						parent.put("id", field.get(ucpTask));
						FieldInput parentField = new FieldInput(ReadConfig.getUcpFieldValue(field.getName()), new ComplexIssueInputFieldValue(parent));
						builder.setFieldInput(parentField);
					} else {
						builder.setFieldValue(ReadConfig.getUcpFieldValue(field.getName()), field.get(ucpTask));
					}
				}
			}
		}
	}*/

/*    private void updateTask(UcpTask ucpTask) {
        try {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(TASK_ISSUE_TYPE_ID)) {
                final IssueRestClient client = getClient().getIssueClient();
                IssueInputBuilder builder = new IssueInputBuilder(UCP, Long.parseLong(TASK_ISSUE_TYPE_ID));
                Field[] fields = ucpTask.getClass().getFields();
                buildCase(fields, ucpTask, builder);
                try {
                    final IssueInput input = builder.build();
                    client.updateIssue(ucpTask.getJiraNo(), input).claim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("issueTypeId????????????");
            }
        } catch (Exception e) {
            logger.error("jira????????????:" + e.getMessage());
        }

    }*/
}