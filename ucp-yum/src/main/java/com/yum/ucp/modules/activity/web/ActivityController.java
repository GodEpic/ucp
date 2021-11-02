package com.yum.ucp.modules.activity.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.*;
import com.yum.ucp.common.annotation.IgnoreAnnotation;
import com.yum.ucp.common.annotation.SelectAnnotation;
import com.yum.ucp.common.annotation.SelectOneAnnotation;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.constants.ActivityConstants;
import com.yum.ucp.common.constants.ActivityImportConstants;
import com.yum.ucp.common.constants.ActivityImportConstants.ImportType;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.FtpUtils;
import com.yum.ucp.common.utils.JedisUtils;
import com.yum.ucp.modules.activity.entity.*;
import com.yum.ucp.modules.activity.pojo.ActivityVO;
import com.yum.ucp.modules.activity.pojo.NotifyActivityVO;
import com.yum.ucp.modules.activity.service.*;
import com.yum.ucp.modules.activity.utils.CouponImageUtils;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.enumerate.NotifyActivityStatus;
import com.yum.ucp.modules.sys.entity.Attach;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.service.AttachService;
import com.yum.ucp.modules.sys.utils.*;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.service.ModuleActColumnService;
import com.yum.ucp.modules.task.service.ModuleActRowService;
import com.yum.ucp.modules.task.service.UcpModuleService;
import com.yum.ucp.modules.task.service.UcpTaskService;
import com.yum.ucp.modules.task.utils.ActivityHandleDataUtils;
import com.yum.ucp.modules.task.utils.ImportDataUtils;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import com.yum.ucp.modules.work.service.DscWorkCodeService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${adminPath}/activity")
public class ActivityController extends DscBaseController {
    private final static String ROLE_L2 = "2";
    private final static String ROLE_QA = "5";

    Logger log= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UcpTaskService ucpTaskService;

    @Autowired
    private ModuleActRowService moduleActRowService;

    @Autowired
    private ModuleActColumnService moduleActColumnService;

    @Autowired
    private UcpModuleService ucpModuleService;

    @Autowired
    private CouponImageService couponImageService;

    @Autowired
    private DscWorkCodeService workCodeService;
    @Autowired
    private UcpActFileService actFileService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private NotifyActivityService notifyActivityService;

    @Autowired
    private NotifyFileService notifyFileService;

    private static final String ACTIVITY_ISSUETYPE_ID = ReadConfig.getUcpFieldValue("activityIssueTypeId");
    private static final String URGENT_ACTIVITY_ISSUETYPE_ID = ReadConfig.getUcpFieldValue("urgentActivityIssueTypeId");

    @ModelAttribute
    public Activity get(@RequestParam(required = false) String id) {
        Activity entity = null;
        if (com.yum.ucp.common.utils.StringUtils.isNotBlank(id)) {
            entity = activityService.get(id);
        }
        if (entity == null) {
            entity = new Activity();
        }
        return entity;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage save(Activity activity, String couponImages, String actFiles ) throws Exception {
        try {
            // TODO:保存活动前判断优先级，如果是Highest，查询是否已经存在没完成的任务，如果已存在则不能保存
            if(StringUtils.isBlank(activity.getId())&&activityService.isActivityExisting(activity.getNotifyActivityNo())){
                logger.error("重复提交，直接返回。");
                return ResponseMessage.error("重复提交，请关闭页面，刷新接收活动列表再试。");
            }
            saveActivity(activity, ACTIVITY_ISSUETYPE_ID, P2);

            if (activity != null) {
                saveCouponImages(couponImages, activity.getId());
            }

            if (activity != null && StringUtils.isNotBlank(activity.getId()) && StringUtils.isNotBlank(actFiles)) {
                updateActFile2(actFiles, activity.getId());
            }
        } catch (RestClientException e) {
            logger.error("保存失败", e);
            return ResponseMessage.error(MessageUtils.returnErrors(e.getErrorCollections(), findAllFields()));
        }
        return ResponseMessage.success("保存成功", activity);
    }

    /**
     * 更新活动附件信息(不动updateActFile方法，但感觉原方法写得有问题）
     *
     * @param actFiles
     * @param id
     */
    private void updateActFile2(String actFiles, String id) {
        JSONArray cpArray = JSONArray.parseArray(HtmlUtils.htmlUnescape(actFiles));
        List<UcpActFile> dbActFiles = actFileService.findByActId(id);
        Map<String, UcpActFile> dbActFileDatas = dbActFiles.stream().collect(Collectors.toMap(UcpActFile::getId, Function.identity()));
        if (cpArray != null && cpArray.size() > 0) {
            for (int i = 0; i < cpArray.size(); i++) {
                JSONObject cpObject = cpArray.getJSONObject(i);
                String rowId = cpObject.getString("rowId");
                String remarks = cpObject.getString("remarks");
                UcpActFile actFile;
                if (dbActFileDatas.containsKey(rowId)) {
                    actFile = dbActFileDatas.get(rowId);
                    dbActFileDatas.remove(rowId);
                } else {
                    actFile = actFileService.get(rowId);
                }
                if (actFile == null) {
                    continue;
                }
                actFile.setId(rowId);
                actFile.setActId(id);
                actFile.setDescription(remarks);
                actFile.setContent(cpObject.toJSONString());
                actFile.setIsNewRecord(false);
                actFileService.save(actFile);
            }
        }
        if(dbActFileDatas != null && dbActFileDatas.size() > 0) {
        	// 删除不存在的
            for (UcpActFile actFile : dbActFileDatas.values()) {
                actFileService.delete(actFile);
            }
        }

    }

    /**
     * 创建紧急活动
     *
     * @return
     */
    @RequestMapping(value = "saveUrgent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage saveUrgent(Activity activity, String actFiles) throws Exception {
        try {

            if(StringUtils.isBlank(activity.getId())&&activityService.isActivityExisting(activity.getNotifyActivityNo())){
                logger.error("重复提交，直接返回。");
                return ResponseMessage.error("重复提交，请关闭页面，刷新接收活动列表再试。");
            }
            // activity.setType("1");
            saveActivity(activity, URGENT_ACTIVITY_ISSUETYPE_ID, P0);
            if (activity != null && StringUtils.isNotBlank(activity.getId()) && StringUtils.isNotBlank(actFiles)) {
                updateActFile(actFiles, activity.getId());
            }
        } catch (RestClientException e) {
            return ResponseMessage.error(MessageUtils.returnErrors(e.getErrorCollections(), findAllFields()));
        }
        return ResponseMessage.success("保存成功", activity);
    }

    /**
     * 更新紧急活动附件信息
     *
     * @param actFiles
     * @param id
     */
    private void updateActFile(String actFiles, String id) {
        JSONArray cpArray = JSONArray.parseArray(HtmlUtils.htmlUnescape(actFiles));
        List<UcpActFile> dbActFiles = actFileService.findByActId(id);
        Map<String, UcpActFile> dbActFileDatas = dbActFiles.stream().collect(Collectors.toMap(UcpActFile::getId, Function.identity()));
        if (cpArray != null && cpArray.size() > 0) {
            for (int i = 0; i < cpArray.size(); i++) {
                JSONObject cpObject = cpArray.getJSONObject(i);
                String rowId = cpObject.getString("rowId");
                String remarks = cpObject.getString("remarks");
                UcpActFile actFile;
                if (dbActFileDatas.containsKey(rowId)) {
                    actFile = dbActFileDatas.get(rowId);
                    dbActFileDatas.remove(rowId);
                } else {
                    actFile = actFileService.get(rowId);
                }
                if (actFile == null) {
                    continue;
                }
                actFile.setId(rowId);
                actFile.setActId(id);
                actFile.setDescription(remarks);
                actFile.setContent(cpObject.toJSONString());
                actFile.setIsNewRecord(false);
                actFileService.save(actFile);
            }
            // 删除不存在的
            for (UcpActFile actFile : dbActFileDatas.values()) {
                actFileService.delete(actFile);
            }
        }
    }

    private void saveCouponImages(String couponImages, String actId) {
        JSONArray cpArray = JSONArray.parseArray(HtmlUtils.htmlUnescape(couponImages));
        if (cpArray != null && cpArray.size() > 0) {
            for (int i = 0; i < cpArray.size(); i++) {
                JSONObject cpObject = cpArray.getJSONObject(i);
                String cpId = cpObject.getString("cpId");
                CouponImage cp = null;
                if (StringUtils.isNotBlank(cpId) && !cpId.equals("undefined")) {
                    cp = couponImageService.get(cpId);
                    cp.setCouponLink(cpObject.getString("couponLink"));
                    cp.setCouponDesc(cpObject.getString("couponDesc"));
                } else {
                    cp = new CouponImage(actId, cpObject.getString("couponLink"), cpObject.getString("couponDesc"));
                }
                couponImageService.save(cp);
            }
        }
    }

    @RequestMapping(value = "importExcelData", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage importExcelData(Activity activity, @RequestParam(value = "importFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject result = new JSONObject();

        if(StringUtils.isBlank(activity.getId())&&activityService.isActivityExisting(activity.getNotifyActivityNo())){
            logger.error("importExcelData 重复提交，直接返回。");
            result.put("errcode", ResponseErrorCode.ERROR_IMPORT_DUPLICATE.getCode());
            result.put("errmsg", ResponseErrorCode.ERROR_IMPORT_DUPLICATE.getDesc());
            return ResponseMessage.success(result);
        }


        String fileName = file.getOriginalFilename(); //获取文件名
        String fileExt = FilenameUtils.getExtension(fileName);

        if (!ActivityImportConstants.IMPORT_EXT_ALLOW.contains(fileExt.toLowerCase())) {
            result.put("errcode", ResponseErrorCode.ERROR_IMPORT_EXT.getCode());
            result.put("errmsg", ResponseErrorCode.ERROR_IMPORT_EXT.getDesc());
            return ResponseMessage.success(result);
        }
        Integer version = 0;
        try {
            JSONObject importResult = ImportDataUtils.importColumnDataExcel(file.getInputStream());

            if (importResult.getString("errcode").contentEquals(ResponseErrorCode.SUCCESS.getCode())) {

                if (!activity.getCouponCount().equals(importResult.getInteger("dataSize"))) {
                    result.put("errcode", ResponseErrorCode.ERROR_IMPORT_COUPONT_COUNT.getCode());
                    result.put("errmsg", ResponseErrorCode.ERROR_IMPORT_COUPONT_COUNT.getDesc());
                    return ResponseMessage.success(result);
                }

                activity = saveActivity(activity, ACTIVITY_ISSUETYPE_ID , P2);
                version = ucpTaskService.getCurrentVersion(activity.getId(), UcpModule.DEL_FLAG_NORMAL);
                if (StringUtils.isBlank(activity.getId())) {
                    result.put("errcode", ResponseErrorCode.ERROR_ACTIVITY_SAVE.getCode());
                    result.put("errmsg", ResponseErrorCode.ERROR_ACTIVITY_SAVE.getDesc());
                    return ResponseMessage.success(result);
                }

                ActivityHandleDataUtils utils = new ActivityHandleDataUtils();
                JSONObject handleResult = utils.handleImportData(importResult, activity.getId(), version);

                result.put("errcode", handleResult.getString("errcode"));
                result.put("errmsg", handleResult.getString("errmsg"));
                result.put("dataSize", importResult.get("dataSize"));
                result.put("actId", activity.getId());
                result.put("jiraNo", activity.getJiraNo());
                result.put("jiraStatus", activity.getJiraStatus());
                result.put("releaseStatus", activity.getReleaseStatus());
                result.put("priority", activity.getPriority());
                result.put("status", activity.getStatus());
            } else {
                result.put("errcode", importResult.getString("errcode"));
                result.put("errmsg", importResult.getString("errmsg").replaceAll("\n", "<br/>"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("errcode", ResponseErrorCode.SYS_ERROR.getCode());
            result.put("errmsg", ResponseErrorCode.SYS_ERROR.getDesc());
        }

        try {
            JSONObject uploadFile = FtpUtils.uploadSignalFile(file,activity.getNotifyActivityNo());
            saveAttach(uploadFile, fileName, activity.getId(), version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseMessage.success(result);
    }

    @RequestMapping(value = "importFileData", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage importFileData(Activity activity, @RequestParam(value = "importFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject result = new JSONObject();

        if(StringUtils.isBlank(activity.getId())&&activityService.isActivityExisting(activity.getNotifyActivityNo())){
            logger.error("importFileData 重复提交，直接返回。");
            result.put("errcode", ResponseErrorCode.ERROR_IMPORT_DUPLICATE.getCode());
            result.put("errmsg", ResponseErrorCode.ERROR_IMPORT_DUPLICATE.getDesc());
            return ResponseMessage.success(result);
        }
        String fileName = file.getOriginalFilename(); //获取文件名
        Integer version = 0;
        try {
//        	JSONObject importResult = new JSONObject();
//        	importResult.put("errcode", ResponseErrorCode.SUCCESS.getCode());
//        	importResult.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());
            JSONObject handleResult = new JSONObject();
    		handleResult.put("errcode", ResponseErrorCode.SUCCESS.getCode());
    		handleResult.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());
            result.put("errcode", handleResult.getString("errcode"));
            result.put("errmsg", handleResult.getString("errmsg"));
            result.put("dataSize", 0);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("errcode", ResponseErrorCode.SYS_ERROR.getCode());
            result.put("errmsg", ResponseErrorCode.SYS_ERROR.getDesc());
        }

        try {
            JSONObject uploadFile = FtpUtils.uploadSignalFile(file);
            saveAttach(uploadFile, fileName, activity.getId(), version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseMessage.success(result);
    }

    private void saveAttach(JSONObject uploadFile, String fileName, String actId, Integer version) {
        Attach attach = new Attach();
        String fileTitle = fileName.substring(0, fileName.lastIndexOf("."));
        attach.setClassName(Activity.class.getName());
        attach.setClassPk(actId);
        attach.setOriginalFileName(fileTitle + "_" + version + "_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "." + uploadFile.getString("ext"));
        attach.setFileSize(uploadFile.getString("fileSize"));
        attach.setExtension(uploadFile.getString("ext"));
        attach.setFileName(uploadFile.getString("fileName"));
        attach.setFilePath(uploadFile.getString("filePath"));
        attach.setType("importExcelData");

        attachService.save(attach);
    }

    private Activity saveActivity(Activity act, String issueTypeId, String priority) throws Exception {
        String key = act.getJiraNo();
        BasicIssue promise;
        String projectKey = UCP;
        final IssueRestClient client = getClient().getIssueClient();

        if (!StringUtils.isNoneBlank(key)) {
            IssueType issueType = getIssueType(issueTypeId);
            if (null != issueType) {
                IssueInputBuilder builder = new IssueInputBuilder(projectKey, issueType.getId());
                Field[] fields = act.getClass().getFields();
                buildCase(fields, act, builder);
                builder.setSummary(act.getSummary());
                builder.setDescription(act.getDescription());
                if (StringUtils.isBlank(act.getPriority())) {
                    builder.setPriorityId(Long.parseLong(priority));
                    act.setPriority(priority);
                } else {
                    builder.setPriorityId(Long.parseLong(act.getPriority()));
                }
                final IssueInput input = builder.build();
                promise = client.createIssue(input).claim();
                key = promise.getKey();
                act.setJiraNo(key);
                act.setReleaseStatus(RELEASE_STATUS_UNRELEASED);
                act.setStatus(ACTIVITY_STATUS_UNCLAIMED);
                act.setJiraStatus(ACTIVITY_JIRA_STATUS_SENIOR);
            }
        } else {
            updateCase(act, issueTypeId);
        }
        // 保存数据到本地数据库
        activityService.saveActivity(act);
        return act;
    }

    /**
     * 预览导入的券信息
     *
     * @param actId
     * @param model
     * @return
     */
    @RequestMapping("prewImportData")
    public String prewImportData(String actId, Model model) {
        prewData(actId, model);
        return "ucp/activity/prewImportData";
    }

    private void prewData(String actId, Model model){
        Integer version = moduleActRowService.getModuleRowLastVersionByActId(actId);

        model.addAttribute("sysLists", ucpModuleService.findSysByActIdAndVersion(actId, version, UcpModule.DEL_FLAG_NORMAL));
        model.addAttribute("modLists", ucpModuleService.findModByActIdAndVersion(actId, version, UcpModule.DEL_FLAG_NORMAL));
        model.addAttribute("rowLists", moduleActRowService.findListByActIdAndVersion(new ModuleActRow(actId, version)));
        model.addAttribute("columnLists", moduleActColumnService.findListByActIdAndVersion(actId, version, UcpModule.DEL_FLAG_NORMAL));
        model.addAttribute("dateType", ImportType.DATE.getValue());
    }

    /**
     * 获取导入状态
     *
     * @return
     */
    @RequestMapping(value = "getImportStatus", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage getImportStatus(String actId){
        String importStatus = JedisUtils.get("thread_" + actId);
        if(StringUtils.isBlank(importStatus) || StringUtils.isNotBlank(importStatus) && importStatus.equals("1")){
            return ResponseMessage.success(0);
        }
        return ResponseMessage.error(-1);
    }

    /**
     * 活动详情页预览导入的券信息
     *
     * @param actId
     * @param model
     * @return
     */
    @RequestMapping("prewDetailImportData")
    public String prewDetailImportData(String actId, Model model) {
        prewData(actId, model);
        return "ucp/activity/prewImportData";
    }

    /**
     * 活动列表-查询所有未发布的活动
     *
     * @param activity
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("dealList")
    public String dealList(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 原逻辑，包含紧急活动
        activity.setType(ActivityConstants.ActivityType.URGENT.getType());
        if (activity.getReleaseStatus() == null) {
            activity.setReleaseStatus(RELEASE_STATUS_UNRELEASED);
        }
        /*// 如果是已发布，则状态不包含已测试通过的
        if (RELEASE_STATUS_RELEASED.equals(activity.getReleaseStatus())) {
            activity.setInStatus(false);
            activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
        }
        // 已完成
        if (ACTIVITY_STATUS_TEST_PASS.equals(activity.getReleaseStatus())) {
            model.addAttribute("releaseStatus", ACTIVITY_STATUS_TEST_PASS);
            activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
            activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
        }*/
        Page<ActivityVO> page = new Page<>(request, response);
        // 活动优先级降序Highest、High到Medium,上架时间升序（上架时间由近到远）
        page.setOrderBy("b.priority, b.\"launchTime\", b.id");
        list(activity, page, model);
        activityStatus(model);
        return "ucp/activity/dealCaseList";
    }



    /**
     * 活动列表-查询所有未发布的活动
     *
     * @param activity
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("activityList")
    public String activityList(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {

    	User user = UserUtils.getUser();

    	if(user.getBrand()!=null && !"".equals(user.getBrand())) {
    		if("13004".equals(user.getBrand())) {
    			activity.setBrand("KFC");
    		}
    		if("13005".equals(user.getBrand())) {
    			activity.setBrand("PH");
    		}
        }

    	if("Irene".equals(user.getName())) {
    		activity.setBrand("PH");
    	}

    	if("Sylvia".equals(user.getName())) {
    		activity.setBrand("KFC");
    	}

    	// 原逻辑，包含紧急活动
        activity.setType(ActivityConstants.ActivityType.URGENT.getType());
        if (activity.getReleaseStatus() == null) {
            activity.setReleaseStatus(RELEASE_STATUS_UNRELEASED);
        }

        Page<NotifyActivity> page = new Page<>(request, response);
        // 活动优先级降序Highest、High到Medium,上架时间升序（上架时间由近到远）
        NotifyActivity query=new NotifyActivity();
        query.setStatus(activity.getStatus());
        query.setSearchTxt(activity.getSearchTxt());
        query.setBrand(activity.getBrand());

        Page<NotifyActivity> pageList = notifyActivityService.findPage(page,query);
        if(pageList!=null) {
            for (NotifyActivity notifyActivity : pageList.getList()) {
                notifyActivity.setStatusValue(NotifyActivityStatus.getDescByStatus(notifyActivity.getStatus()));
                List<NotifyFile> notifyFileList=notifyFileService.findByNotifyId( notifyActivity.getId());
                notifyActivity.setNotifyFileList(notifyFileList);
            }


        }
        model.addAttribute("page", page);
        model.addAttribute("priorities", DictUtils.getDictList("priorities"));
        if (!model.containsAttribute("releaseStatus")) {
            model.addAttribute("releaseStatus", activity.getReleaseStatus());
        }
        model.addAttribute("status", activity.getStatus());
        model.addAttribute("summary", activity.getSummary());
        model.addAttribute("searchTxt",activity.getSearchTxt());
        model.addAttribute("RELEASE_STATUS_UNRELEASED", RELEASE_STATUS_UNRELEASED);
        model.addAttribute("RELEASE_STATUS_RELEASED", RELEASE_STATUS_RELEASED);
        return "ucp/activity/activityList";
    }

    @RequestMapping("dealKeyValueList")
    public String dealKeyValueList(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {
        activity.setType(ActivityConstants.ActivityType.KEY_VALUE.getType());
        if (activity.getReleaseStatus() == null) {
            activity.setReleaseStatus(RELEASE_STATUS_UNRELEASED);
        }
        Page<ActivityVO> page = new Page<>(request, response);
        // 活动优先级降序Highest、High到Medium,上架时间升序（上架时间由近到远）
        page.setOrderBy("b.priority, b.\"launchTime\", b.id");
        list(activity, page, model);
        activityStatus(model);
        return "ucp/activity/dealKeyValueCaseList";
    }

    @RequestMapping("dealListQA")
    public String dealListQA(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 原逻辑，包含紧急活动
        activity.setType(ActivityConstants.ActivityType.URGENT.getType());
        if (activity.getStatus() == null) {
            activity.setStatus(ACTIVITY_STATUS_NOT_TESTED);
        }
        Page<ActivityVO> page = new Page<>(request, response);
        page.setOrderBy("b.priority, b.\"testFinishedTime\", b.\"recommendedTestTime\", b.id");
        list(activity, page, model);
        activityStatus(model);

        return "ucp/activity/dealCaseListQA";
    }

    @RequestMapping("dealKeyValueListQA")
    public String dealKeyValueListQA(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 只查键值活动
        activity.setType(ActivityConstants.ActivityType.KEY_VALUE.getType());
        if (activity.getStatus() == null) {
            activity.setStatus(ACTIVITY_STATUS_TESTING);
        }
        Page<ActivityVO> page = new Page<>(request, response);
        page.setOrderBy("b.priority, b.\"testFinishedTime\", b.\"recommendedTestTime\", b.id");
        list(activity, page, model);
        activityStatus(model);

        return "ucp/activity/dealKeyValueCaseListQA";
    }

    @RequestMapping("allList")
    public String allList(ActivityVO activity, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 原逻辑，包含紧急活动
        activity.setType(ActivityConstants.ActivityType.URGENT.getType());
        Page<ActivityVO> page = new Page<>(request, response);
        // 活动优先级降序Highest、High到Medium,上架时间升序（上架时间由近到远）
        page.setOrderBy("b.priority, b.\"launchTime\", b.id");
        // 已发布的才显示到台账中,默认已发布
        if (activity.getReleaseStatus() == null) {
            activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
        }
        list(activity, page, model);
        activityStatus(model);
        return "ucp/activity/allCaseList";
    }

    private void list(ActivityVO activity, Page<ActivityVO> p, Model model) {
        String roleType = SessionUtils.getRoleType();
        // 未测试的QA可以看到所有的，其余状态QA只能看到自己的
        if(!ACTIVITY_STATUS_NOT_TESTED.equals(activity.getStatus()) && ROLE_TYPE_QA.equals(roleType)) {
            activity.setReceiveUser(SessionUtils.getPhoneAgent());
        }
        // 资深员工只能看到自己的
        else if(ROLE_TYPE_SENIOR.equals(roleType)) {
            activity.setCreateBy(UserUtils.getUser());
        }
        if(!ROLE_TYPE_ADMIN.equals(roleType) && !ROLE_TYPE_QA.equals(roleType)) {
            activity.setCreateBy(UserUtils.getUser());
        }
        // 如果是已发布，则状态不包含已测试通过的
        if (RELEASE_STATUS_RELEASED.equals(activity.getReleaseStatus())) {
            activity.setInStatus(false);
            activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
        }
        // 已完成
        if (ACTIVITY_STATUS_TEST_PASS.equals(activity.getReleaseStatus())) {
            model.addAttribute("releaseStatus", ACTIVITY_STATUS_TEST_PASS);
            activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
            activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
            // 已完成的按照上架时间降序，优先级降序排列
            p.setOrderBy("b.\"launchTime\" desc, b.priority, b.\"createDate\", b.id");
        }

        User user = UserUtils.getUser();
		if(user.getBrand()!=null && !"".equals(user.getBrand())) {
			activity.setBrand(user.getBrand());
        }

        Page<ActivityVO> page = activityService.findActivityPage(p, activity);
        model.addAttribute("page", page);
        model.addAttribute("priorities", DictUtils.getDictList("priorities"));
        if (!model.containsAttribute("releaseStatus")) {
            model.addAttribute("releaseStatus", activity.getReleaseStatus());
        }
        model.addAttribute("status", activity.getStatus());
        model.addAttribute("summary", activity.getSummary());
        model.addAttribute("RELEASE_STATUS_UNRELEASED", RELEASE_STATUS_UNRELEASED);
        model.addAttribute("RELEASE_STATUS_RELEASED", RELEASE_STATUS_RELEASED);
    }

    /**
     * 活动认领QA
     *
     * @param id
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "receiveQA")
    public String receiveQA(String id, RedirectAttributes redirectAttributes) throws Exception {
        // TODO:
        // 查询是否已被领取
        Activity activity = activityService.get(id);
        if (activity.getReceiveUser() != null) {
            addErrorMessage(redirectAttributes, "该活动已被其他人认领");
        } else {
            // 保存领取记录
            activity.setReceiveUser(SessionUtils.getPhoneAgent());
            activity.setStatus(ACTIVITY_STATUS_TESTING);
            activityService.save(activity);
            addMessage(redirectAttributes, "认领成功");
        }
        return "redirect:" + adminPath + "/activity/dealListQA?status=" + ACTIVITY_STATUS_NOT_TESTED + "&repage";
    }

    @RequestMapping("/form")
    public String activityDetail(Activity activity, Model model,String notivifyId) throws Exception {

        if(StringUtils.isNotEmpty(notivifyId)) {
            NotifyActivity notifyActivity = notifyActivityService.get(notivifyId);
            if (notifyActivity != null) {
                activity.setActivityName(notifyActivity.getActivityName());
            }
        }

        if(StringUtils.isEmpty(notivifyId)) {
           List<NotifyActivity> notifyActivityList = notifyActivityService.findByActivityId(activity.getNotifyActivityNo());
            if (!CollectionUtils.isEmpty(notifyActivityList)) {
                activity.setNotifyActivityId(notifyActivityList.get(0).getId());
            }
        }

        if(StringUtils.isEmpty(activity.getNotifyActivityNo())) {
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        	activity.setNotifyActivityNo("UCP"+dtf.format(LocalDateTime.now()));
        }

        model.addAttribute("activity", activity);

        if(StringUtils.isNotBlank(activity.getStatus()) && (activity.getStatus().equals(ACTIVITY_STATUS_NOT_TESTED) ||
                activity.getStatus().equals(ACTIVITY_STATUS_TESTING) ||
                activity.getStatus().equals(ACTIVITY_STATUS_TEST_FEEDBACK) ||
                activity.getStatus().equals(ACTIVITY_STATUS_TEST_PASS))){
            model.addAttribute("couponImageList", couponImageService.findListByActId(activity.getId()));

            Integer version = moduleActRowService.getModuleRowLastVersionByActId(activity.getId());
            List<ModuleActRow> lists = moduleActRowService.findListByActIdAndVersion(new ModuleActRow(activity.getId(), version));
            model.addAttribute("couponCount", lists != null ? lists.size() : 0);
            return "ucp/activity/detail";
        }

        model.addAttribute("releaseStatusReleased", RELEASE_STATUS_RELEASED);
        // 普通活动
        JSONArray couponImages = new JSONArray();
        JSONArray actFileData = new JSONArray();
        if (activity != null) {
            couponImages = CouponImageUtils.findByActId(activity.getId());
            List<UcpActFile> lists = actFileService.findByActId(activity.getId());
            if (!CollectionUtils.isEmpty(lists)) {
                lists.forEach(actFile -> {
                    JSONObject content = JSONObject.parseObject(actFile.getContent());
                    actFileData.add(content);
                });
            }
        }
        model.addAttribute("couponImages", couponImages);
        model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));

        Integer version = moduleActRowService.getModuleRowLastVersionByActId(activity.getId());
        List<ModuleActRow> lists = moduleActRowService.findListByActIdAndVersion(new ModuleActRow(activity.getId(), version));
        model.addAttribute("couponCount", lists != null ? lists.size() : 0);

        return "ucp/activity/activityCase";
    }

    @RequestMapping("/urgentForm")
    public String urgentActivityDetail(Activity activity, Model model) throws Exception {
        urgentDataModel(activity, model);
        return "ucp/activity/urgentActivityCase";
    }

    @RequestMapping("/urgentDetail")
    public String urgentDetail(Activity activity, Model model) throws Exception {
        urgentDataModel(activity, model);
        return "ucp/activity/urgentDetail";
    }

    private void urgentDataModel(Activity activity, Model model){
        model.addAttribute("activity", activity);

        JSONArray actFileData = new JSONArray();
        if (activity != null) {
            List<UcpActFile> lists = actFileService.findByActId(activity.getId());
            if (!CollectionUtils.isEmpty(lists)) {
                lists.forEach(actFile -> {
                    JSONObject content = JSONObject.parseObject(actFile.getContent());
                    actFileData.add(content);
                });
            }
        }
        model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));
        model.addAttribute("assignList", getAllAssigners());
        model.addAttribute("SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN", SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN);
    }

    /**
     * 跳转至活动详情页面
     *
     * @param activity
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(Activity activity, Model model) throws Exception {
        model.addAttribute("activity", activity);
        model.addAttribute("couponImageList", couponImageService.findListByActId(activity.getId()));

        Integer version = moduleActRowService.getModuleRowLastVersionByActId(activity.getId());
        List<ModuleActRow> lists2 = moduleActRowService.findListByActIdAndVersion(new ModuleActRow(activity.getId(), version));
        model.addAttribute("couponCount", lists2 != null ? lists2.size() : 0);


        JSONArray actFileData = new JSONArray();
        if (activity != null && !StringUtils.isEmpty(activity.getId())) {
            List<UcpActFile> lists = actFileService.findByActId(activity.getId());
            if (!CollectionUtils.isEmpty(lists)) {
                lists.forEach(actFile -> {
                    JSONObject content = JSONObject.parseObject(actFile.getContent());
                    actFileData.add(content);
                });
            }
        }
        model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));


        return "ucp/activity/detail";
    }

    @RequestMapping(value = "delete")
    public String delete(Activity activity, RedirectAttributes redirectAttributes) {
        // 查询当前账号是否有删除权限
        boolean canDelete = false;
        List<Dict> deletePermissionPersonList = DictUtils.getDictList("deletePermissionPersons");
        for (Dict dict : deletePermissionPersonList) {
            if (dict.getValue().equals(SessionUtils.getUserLoginName())) {
                canDelete = true;
                break;
            }
        }
        // 已经发送子任务的不能删除
        String releaseStatus = activity.getReleaseStatus();
        if (RELEASE_STATUS_RELEASED.equals(releaseStatus) && !canDelete) {
            addErrorMessage(redirectAttributes, "活动已发布，无法删除");
        } else {
            String key = activity.getJiraNo();
            final IssueRestClient client = getClient().getIssueClient();
            try {
                if (StringUtils.isNoneBlank(key)) {
                    client.deleteIssue(key, true);
                }
                UcpTask task = new UcpTask();
                task.setActId(activity.getId());
                List<UcpTask> taskList = ucpTaskService.findList(task);
                for (UcpTask ucpTask : taskList) {
                    ucpTaskService.delete(ucpTask);
                }
                activityService.delete(activity);
                notifyActivityService.reduction(activity);
                addMessage(redirectAttributes, "删除活动成功");
            } catch (Exception e) {
                logger.error("jira活动删除失败:", e);
                addErrorMessage(redirectAttributes, "删除活动失败");
            }
        }
        return "redirect:" + adminPath + "/activity/dealList?repage&releaseStatus=" + RELEASE_STATUS_RELEASED;
    }

/*    @RequestMapping(value = "endCase", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage endCase(Activity activity) throws Exception {
        // 更新数据到jira
        updateCase(activity, ACTIVITY_ISSUETYPE_ID);
        String key = activity.getJiraNo();
        logger.info("activity endCase 入参 【{}】", key);
        final IssueRestClient client = getClient().getIssueClient();
        Issue issue = client.getIssue(key).claim();
        TransitionInput tinput = new TransitionInput(Integer.parseInt(ReadConfig.getUcpFieldValue("CONSULT_L2_TICKET_DONE")));
        client.transition(issue, tinput);
        return ResponseMessage.success(key);
    }*/

    @RequestMapping(value = "specialEndCase", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage specialEndCase(Activity activity) throws Exception {
        String key = activity.getJiraNo();
        logger.info("special activity endCase 入参 【{}】", key);
        // 更新jira状态
        updateIssueStatus(key, ASSIGN_TO_DONE);
        activity.setJiraStatus(ACTIVITY_JIRA_STATUS_DONE);
        activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
        activityService.updateStatus(activity);
        return ResponseMessage.success(key);
    }

    /**
     * 获取紧急活动的接收人
     *
     * @return
     */
    @RequestMapping(value = "getAssigners", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMessage getAllAssigners() {
        // 1. 获取sys_user和work_code两张表的人
        List<DscWorkCode> workCodes = workCodeService.findUserIdsByRoleType(ROLE_L2, ROLE_QA);
        Map<String, String> map = new HashMap<>();
        workCodes.forEach(workCode -> {
            String userId = workCode.getUser().getId();
            String userWorkCode = workCode.getWorkCode();
            map.put(userId, userWorkCode);
        });
        List<User> users = new ArrayList<>();
        if (!CollectionUtils.isEmpty(map)) {
            List<String> userIds = map.keySet().stream().collect(Collectors.toList());
            users = UserUtils.getUsersByIds(userIds);
        }
        users.forEach(user -> user.setNo(map.get(user.getId())));
        return ResponseMessage.success(users);
    }

    public void buildCase(Field[] fields, Activity activity, IssueInputBuilder builder) throws Exception {
        for (Field field : fields) {
            if (null != field.get(activity) && StringUtils.isNotBlank(ReadConfig.getUcpFieldValue(field.getName()))) {
                SelectAnnotation selectAnnotation = field.getAnnotation(SelectAnnotation.class);
                SelectOneAnnotation selectOneAnnotation = field.getAnnotation(SelectOneAnnotation.class);
                IgnoreAnnotation ignoreAnnotation = field.getAnnotation(IgnoreAnnotation.class);
                if (null == ignoreAnnotation) {
                    if (null != selectAnnotation) {
                        builder.setFieldValue(ReadConfig.getUcpFieldValue(field.getName()), ComplexIssueInputFieldValue.with("id", field.get(activity)));
                    } else if (null != selectOneAnnotation) {
                        Map<String, Object> parent = new HashMap<>(16);
                        parent.put("id", field.get(activity));
                        FieldInput parentField = new FieldInput(ReadConfig.getUcpFieldValue(field.getName()), new ComplexIssueInputFieldValue(parent));
                        builder.setFieldInput(parentField);
                    } else {
                        builder.setFieldValue(ReadConfig.getUcpFieldValue(field.getName()), field.get(activity));
                    }
                }
            }
        }
    }

/*    public void updateCase(Activity activity) {
        try {
            if (StringUtils.isNotBlank(activity.getIssueTypeId())) {
                final IssueRestClient client = getClient().getIssueClient();
                IssueInputBuilder builder = new IssueInputBuilder(activity.getProjectKey(), Long.parseLong(activity.getIssueTypeId()));
                Field[] fields = activity.getClass().getFields();
                buildCase(fields, activity, builder);
                try {
                    if (activity.getPriority() != null) {
                        builder.setPriorityId(Long.parseLong(activity.getPriority()));
                    }
                    final IssueInput input = builder.build();
                    client.updateIssue(activity.getKey(), input).claim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("issueTypeId不能为空");
            }
        } catch (Exception e) {
            logger.error("jira更新失败:" + e.getMessage());
        }

    }*/

    private void updateCase(Activity activity, String issueTypeId) {
        try {
            if (StringUtils.isNotBlank(issueTypeId)) {
                final IssueRestClient client = getClient().getIssueClient();
                IssueInputBuilder builder = new IssueInputBuilder(UCP, Long.parseLong(issueTypeId));
                Field[] fields = activity.getClass().getFields();
                buildCase(fields, activity, builder);
                try {
                    if (activity.getPriority() != null) {
                        builder.setPriorityId(Long.parseLong(activity.getPriority()));
                    }
                    builder.setSummary(activity.getSummary());
                    builder.setDescription(activity.getDescription());
                    final IssueInput input = builder.build();
                    client.updateIssue(activity.getJiraNo(), input).claim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("issueTypeId不能为空");
            }
        } catch (Exception e) {
            logger.error("jira更新失败:" + e.getMessage());
        }

    }


    @RequestMapping(value = "revocation", method = RequestMethod.GET)
    public String revocation(String notivifyId){
        try {
            notifyActivityService.revocation(notivifyId);
        }catch (Exception e){
            log.error("revocation error",e);
        }
        return "redirect:" +  adminPath + "/activity/activityList?sort=&sortBy=";


    }


}
