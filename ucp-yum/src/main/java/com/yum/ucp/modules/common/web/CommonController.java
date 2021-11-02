package com.yum.ucp.modules.common.web;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.FtpUtils;
import com.yum.ucp.modules.activity.entity.QaReport;
import com.yum.ucp.modules.caseConfig.entity.DscCaseConfig;
import com.yum.ucp.modules.caseConfig.service.DscCaseConfigService;
import com.yum.ucp.modules.common.entity.*;
import com.yum.ucp.modules.common.entity.File;
import com.yum.ucp.modules.sys.entity.Attach;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.service.AttachService;
import com.yum.ucp.modules.sys.utils.*;
import com.yum.ucp.modules.task.entity.DscTaskLock;
import com.yum.ucp.modules.task.service.DscTaskLockService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value = "${adminPath}/common")
public class CommonController extends DscBaseController {

    @Autowired
    private DscTaskLockService dscTaskLockService;

    @Autowired
    private DscCaseConfigService dscCaseConfigService;

    @Autowired
    private AttachService attachService;

    @RequestMapping(value = "updateIssueStatus", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateIssueStatus(String key, Integer statusId, Boolean isDeal) throws Exception {
        if (null != isDeal && isDeal) {
            DscTaskLock lock = dscTaskLockService.getByCode(key);
            if (!lock.getCreateBy().getId().equals(UserUtils.getUser().getId())) {
                return ResponseMessage.error(ReadConfig.getErrorValue("lock_case"));
            }
        }
        JiraRestClient restClient = getClient();
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        TransitionInput tinput = new TransitionInput(statusId);
        restClient.getIssueClient().transition(issue, tinput);
        return ResponseMessage.success();
    }

    @RequestMapping("uploadAttachment")
    @ResponseBody
    public ResponseMessage uploadAttachment(String key, @RequestParam("file") MultipartFile file) throws
            Exception {
        String fileName = file.getOriginalFilename();
        JiraRestClient restClient = getClient();
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        restClient.getIssueClient().addAttachment(issue.getAttachmentsUri(), file.getInputStream(), fileName).claim();
        Issue newIssue = restClient.getIssueClient().getIssue(key).claim();
        Iterator<Attachment> attachments = newIssue.getAttachments().iterator();
        while (attachments.hasNext()) {
            Attachment attachment = attachments.next();
            if (fileName.equals(attachment.getFilename())) {
                File f = new File(attachment.getContentUri().toString(), fileName, attachment.getSelf().toString());
                return ResponseMessage.success(f);
            }
        }

        return ResponseMessage.success();
    }

    @RequestMapping(value = "deleteAttachment")
    @ResponseBody
    public ResponseMessage deleteAttachment(String uri) throws
            Exception {
        if (StringUtils.isNoneBlank(uri)) {
            JiraRestClient restClient = getClient();
            URI attachmentsUri = new URI(uri);
            restClient.getIssueClient().deleteAttachment(attachmentsUri);
            return ResponseMessage.success();
        }
        return ResponseMessage.error("删除失败");
    }

    @RequestMapping(value = "downloadAttachment")
    public void downloadAttachment(String uri, String fileName, HttpServletResponse response) throws IOException {
        JiraRestClient restClient = getClient();
        InputStream ins = null;
        BufferedInputStream bins = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        try {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));// 设置文件名
            ins = restClient.getIssueClient().getAttachment(new URI(uri)).get();
            bins = new BufferedInputStream(ins);
            outs = response.getOutputStream();
            bouts = new BufferedOutputStream(outs);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = bins.read(buffer, 0, 1024)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }

            bouts.flush();
        } catch (InterruptedException | ExecutionException | URISyntaxException | IOException e) {
            logger.error("下载case附件【fielName=" + fileName + "，url=" + uri + "】出现异常，异常信息：", e);
        } finally {
            if (null != ins) {
                ins.close();
            }
            if (null != bins) {
                bins.close();
            }
            if (null != outs) {
                outs.close();
            }
            if (null != bouts) {
                bouts.close();
            }
        }

    }

    @RequestMapping(value = "viewPic")
    @ResponseBody
    public String viewPic(String uri, String fileName, HttpServletResponse response) throws IOException {
        JiraRestClient restClient = getClient();
        InputStream ins = null;
        BufferedInputStream bins = null;
        //OutputStream outs = null;
        BufferedOutputStream bouts = null;
        ByteArrayOutputStream baos = null;
        try {
            response.setContentType("image/jpeg;charset=UTF-8");
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ins = restClient.getIssueClient().getAttachment(new URI(uri)).get();
            bins = new BufferedInputStream(ins);
            // 创建一个新的 byte 数组输出流，它具有指定大小的缓冲区容量
            baos = new ByteArrayOutputStream();
            //outs = response.getOutputStream();
            bouts = new BufferedOutputStream(baos);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = bins.read(buffer, 0, 1024)) != -1) {
                bouts.write(buffer, 0, bytesRead);

            }
            bouts.flush();
            byte[] bytes = baos.toByteArray();
            return Base64.encodeBase64String(bytes);
        } catch (InterruptedException | ExecutionException | URISyntaxException | IOException e) {
            logger.error("预览case附件【fielName=" + fileName + "，url=" + uri + "】出现异常，异常信息：", e);
            return null;
        } finally {
            if (null != ins) {
                ins.close();
            }
            if (null != bins) {
                bins.close();
            }
            if (null != baos) {
                baos.close();
            }
            if (null != bouts) {
                bouts.close();
            }
        }
    }

    @RequestMapping("addComment")
    @ResponseBody
    public ResponseMessage addComment(String comment, String key) throws Exception {
        /*JiraRestClient restClient = getClient();
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        Comment com = Comment.valueOf(comment);
        restClient.getIssueClient().addComment(issue.getCommentsUri(), com);
        return ResponseMessage.success();*/
        JSONObject result = new JSONObject();

        result.put("errcode", ResponseErrorCode.SYS_ERROR.getCode());
        result.put("errmsg", ResponseErrorCode.SYS_ERROR.getDesc());

        try {
            JiraRestClient restClient = getClient();
            Issue issue = restClient.getIssueClient().getIssue(key).claim();
            Comment com = Comment.valueOf(comment);
            restClient.getIssueClient().addComment(issue.getCommentsUri(), com);
            result.put("errcode", ResponseErrorCode.SUCCESS.getCode());
            result.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());
            result.put("commonUri", com.getSelf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.success(result);
    }

    @RequestMapping(value = "deleteComment")
    @ResponseBody
    public ResponseMessage deleteComment(String uri) throws Exception {
        if (StringUtils.isNoneBlank(uri)) {
            JiraRestClient restClient = getClient();
            URI commentUri = new URI(uri);
            restClient.getIssueClient().deleteComment(commentUri);
            return ResponseMessage.success();
        }
        return ResponseMessage.error("删除失败");

    }

    /**
     * 催办
     *
     * @param key
     * @param comment
     * @return
     */
    @RequestMapping(value = "press", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage press(String key, String comment) {
        Long priorityId = getPriorityId();
        logger.info("发起case催办功能，case key={},催办消息={},催办优先级={}", key, comment, priorityId);
        JiraRestClient restClient = getClient();
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        logger.info("更新被催办case的优先级，case key={},当前case信息={}", key, issue);
        try {
            Comment com = Comment.valueOf(comment);
            getClient().getIssueClient().addComment(issue.getCommentsUri(), com);
        } catch (RestClientException e) {
            return ResponseMessage.error(e.getMessage());
        } finally {
            logger.info("更新被催办case的优先级，BEGIN");
            gradePress(priorityId, key);
        }
        return ResponseMessage.success();
    }

    public void gradePress(Long priorityId, String key) {
        JiraRestClient restClient = getClient();
        Issue issue = restClient.getIssueClient().getIssue(key).claim();
        IssueInputBuilder builder = new IssueInputBuilder(issue.getProject().getKey(), issue.getIssueType().getId());
        builder.setPriorityId(priorityId);
        final IssueInput input = builder.build();
        final IssueRestClient client = getClient().getIssueClient();
        client.updateIssue(key, input);
        logger.info("更新被催办case的优先级，END");
    }


    @RequestMapping(value = "updateComment")
    @ResponseBody
    public ResponseMessage updateComment(String uri, String comment) throws Exception {
        JiraRestClient restClient = getClient();
        URI commentUri = new URI(uri);
        Comment com = Comment.valueOf(comment);
        restClient.getIssueClient().updateComment(commentUri, com);
        return ResponseMessage.success();
    }

    /**
     * 我的case(单个项目中我创建的所有case)
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("myProjectCases")
    public ModelAndView myProjectCases(String projectId, String issueTypeId, String text, String pageNo, String startDate, String endDate, String preorder) throws Exception {
        if (StringUtils.isBlank(pageNo)) {
            pageNo = "0";
        }
        ModelAndView mav = new ModelAndView("dsc/mycase");
        List<CommonFind> commonFinds = new ArrayList<CommonFind>();
        List<String> projectIds = new ArrayList<String>();
        if (StringUtils.isBlank(projectId)) {
            projectIds = SessionUtils.getProjectIds();
        } else {
            projectIds.add(projectId);
        }
        List<Long> issueTypeIds = new ArrayList<Long>();
        if (StringUtils.isBlank(issueTypeId)) {
            issueTypeIds = getIssueTypeIds(projectId);
        } else {
            issueTypeIds.add(Long.parseLong(issueTypeId));
        }

        String sql = "";
        if (StringUtils.isNotEmpty(text)) {
            Matcher m1 = Global.PATTERN.matcher(text);
            //解决报错问题
            text = java.net.URLDecoder.decode(m1.replaceAll("").trim(), "UTF-8");
        }
        String endDate1 = endDate;
        endDate1 = StringUtils.isEmpty(endDate1) ? endDate1 : DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endDate1, "yyyy-MM-dd"), 1), "yyyy-MM-dd");
        sql = getSearchL1CaseJql(projectIds, issueTypeIds, text, getJiraUserName(), startDate, endDate1, false, preorder);

        logger.info("CommonController-myProjectCase查询待处理case的jql={}", sql);
        SearchResult s = getClient().getSearchClient().searchJql(sql, pageSize, (Integer.parseInt(pageNo) - 1) * pageSize, null).claim();
        logger.debug("CommonController-myProjectCase查询待处理case的结果为={}", s);
        for (Issue issue : s.getIssues()) {
            CommonFind commonFind = new CommonFind();
            commonFind.setSummary(issue.getSummary());
            commonFind.setCreationDate(issue.getCreationDate());
            if (null != issue.getPriority()) {
                commonFind.setPriority(issue.getPriority().getName());
            }
            commonFind.setKey(issue.getKey());
            if (null != issue.getStatus()) {
                commonFind.setJiraStatus(issue.getStatus().getName());
            }
            DscCaseConfig dscCaseConfig = getPageUrl(issue.getIssueType().getId(), issue.getProject().getId());
            if (null != dscCaseConfig) {
                commonFind.setLv1Page(dscCaseConfig.getLv1Page());
                commonFind.setLv2Page(dscCaseConfig.getLv2Page());
                commonFind.setViewPage(dscCaseConfig.getViewPage());
            }
            if (null != issue.getIssueType()) {
                commonFind.setType(issue.getIssueType().getName());
            }
            commonFinds.add(commonFind);
        }
        Page<CommonFind> page = new Page<CommonFind>();
        // 执行分页查询
        page.setPageSize(s.getMaxResults());
        page.setPageNo(Integer.parseInt(pageNo));
        page.setCount(s.getTotal());
        page.setList(commonFinds);
        mav.addObject("page", page);
        mav.addObject("text", text);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        addFilter(projectId, issueTypeId, mav);
        return mav;
    }

    /**
     * LV1首页case
     *
     * @param text
     * @param resPhone
     * @param pageNo
     * @return
     * @throws Exception
     */
    @RequestMapping("indexCases")
    @ResponseBody
    public ResponseMessage indexCases(String text, String resPhone, String pageNo) throws Exception {
        if (StringUtils.isBlank(pageNo)) {
            pageNo = "0";
        }
        List<CommonFind> kBonusRecords = new ArrayList<>();
        String sql = "";
        String extJql = null;
        if (StringUtils.isNotBlank(resPhone)) {
            String hotLine = SessionUtils.getHotLine();
            List<Menu> menus = menuService.findMenusByHotLine(hotLine);
            for (Menu m : menus) {
                if (!StringUtils.equalsIgnoreCase(ReadConfig.getSsoValue("REPAIR"), m.getHref())) {
                    continue;
                }
//                DscRestaurant dscRestaurant = dscRestaurantService.getByPhone(resPhone);
//                if (null != dscRestaurant) {
//                    extJql = " or text ~ " + dscRestaurant.getCode();
//                }
                break;
            }
        }

        if (StringUtils.isNotBlank(text)) {
            Matcher m1 = Global.PATTERN.matcher(text);
            //解决报错问题
            text = java.net.URLDecoder.decode(m1.replaceAll("").trim(), "UTF-8");
        } else {
            text = null;
        }
        sql = jql(SessionUtils.getProjectIds(), getIssueTypeIds(null), text, null, null, null, null, null, null, null, extJql, false);
        SearchResult s = getClient().getSearchClient().searchJql(sql, indexPageSize, (Integer.parseInt(pageNo) - 1) * indexPageSize, null).claim();
        for (Issue issue : s.getIssues()) {
            CommonFind kBonusRecord = new CommonFind();
            kBonusRecord.setSummary(issue.getSummary());
            kBonusRecord.setCreator(issue.getReporter().getName());
            kBonusRecord.setCreationDate(issue.getCreationDate());
            kBonusRecord.setKey(issue.getKey());
            if (null != issue.getIssueType()) {
                kBonusRecord.setType(issue.getIssueType().getName());
            }
            if (null != issue.getStatus()) {
                kBonusRecord.setJiraStatus(issue.getStatus().getName());
            }
            DscCaseConfig dscCaseConfig = getPageUrl(issue.getIssueType().getId(), issue.getProject().getId());
            if (null != dscCaseConfig) {
                kBonusRecord.setLv1Page(dscCaseConfig.getLv1Page());
                kBonusRecord.setViewPage(dscCaseConfig.getViewPage());
            }

            kBonusRecords.add(kBonusRecord);
        }
        Page<CommonFind> page = new Page<>();
        // 执行分页查询
        page.setPageSize(s.getMaxResults());
        page.setPageNo(Integer.parseInt(pageNo));
        page.setCount(s.getTotal());
        page.setList(kBonusRecords);

        return ResponseMessage.success(page);
    }

    /**
     * L2待处理的紧急case
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("myDealCases")
    public ModelAndView myDealCases(String projectKey, String issueTypeId, String text, String pageNo, String priority, String summary, String startDate, String endDate, String sort, String sortBy) throws Exception {
//        JiraRestClient restClient = getClient();
        String status = "";
        ModelAndView mav = new ModelAndView("ucp/dealCaseList");
        if (StringUtils.isBlank(pageNo)) {
            pageNo = "0";
        }
        List<CommonFind> kBonusRecords = new ArrayList<CommonFind>();
        List<String> projectKeys = new ArrayList<String>();
        if (StringUtils.isBlank(projectKey)) {
            projectKeys = SessionUtils.getProjectKeys();
        } else {
            projectKeys.add(projectKey);
        }
        List<Long> issueTypeIds = new ArrayList<Long>();
        if (StringUtils.isBlank(issueTypeId)) {
            issueTypeIds = getIssueTypeIds(projectKey);
        } else {
            issueTypeIds.add(Long.parseLong(issueTypeId));
        }

        String sql = "";
        if (StringUtils.isNotEmpty(text)) {
            Matcher m1 = Global.PATTERN.matcher(text);
            //解决报错问题
            text = java.net.URLDecoder.decode(m1.replaceAll("").trim(), "UTF-8");
            sql = jql(projectKeys, issueTypeIds, text, null, null, getStatus(), null, null, null, null, true);
        } else {
            sql = jql(projectKeys, issueTypeIds, null, null, null, getStatus(), priority, summary, startDate, endDate, true);
        }
//        for (int i = 0; i < projectKeys.size(); i++) {
//            if (projectKeys.get(i).equals(ReadConfig.getShyFieldValue("project"))) {
//                status = "monitor";
//                mav.addObject("monitor", "monitor");
//                if (StringUtils.isNotEmpty(sort)) {
//                    if ("1".equals(sort)) {
//                        sql = sql.replace("ORDER BY priority DESC, created DESC", "order by " + sortBy + " asc");
//                    } else {
//                        sql = sql.replace("ORDER BY priority DESC, created DESC", "order by " + sortBy + " desc");
//                    }
//                    mav.addObject("sort", sort);
//                    mav.addObject("sortBy", sortBy);
//                }
//            }
//        }
        logger.info("待处理case的查询语句：{}", sql);
        SearchResult s = getClient().getSearchClient().searchJql(sql, pageSize, (Integer.parseInt(pageNo) - 1) * pageSize, null).claim();

        for (Issue issue : s.getIssues()) {
            CommonFind kBonusRecord = new CommonFind();
            kBonusRecord.setSummary(issue.getSummary());
            kBonusRecord.setCreationDate(issue.getCreationDate());
            kBonusRecord.setKey(issue.getKey());
            if (null != issue.getStatus()) {
                kBonusRecord.setJiraStatus(issue.getStatus().getName());
            }
//            DscCaseConfig dscCaseConfig = getPageUrl(issue.getIssueType().getId(), issue.getProject().getId());
//            if (null != dscCaseConfig) {
//                kBonusRecord.setLv2Page(dscCaseConfig.getLv2Page());
//            }
            if (null != issue.getPriority()) {
                kBonusRecord.setPriority(issue.getPriority().getName());
            }
            if (StringUtils.isEmpty(status)) {
                dscTaskLockService.updateLockStatus(issue.getKey());
                DscTaskLock dscTaskLock = dscTaskLockService.getByCode(issue.getKey());
                if (null != dscTaskLock) {
                    kBonusRecord.setLockUserName(dscTaskLock.getUserName());
                    if (!dscTaskLock.getUserName().equals(SessionUtils.getUserName())) {
                        kBonusRecord.setIsLock(true);
                    }
                    kBonusRecord.setStatus(dscTaskLock.getLockStatus());
                }
            } else {
                Iterable<IssueField> ie = issue.getFields();
                for (IssueField issueField : ie) {
                    if ("锁定人".equals(issueField.getName()) && issueField.getValue() != null) {
                        kBonusRecord.setLockUserName(issueField.getValue().toString());
                        kBonusRecord.setLockUser(issueField.getValue());
                        if (!issueField.getValue().toString().equals(SessionUtils.getUserName())) {
                            kBonusRecord.setIsLock(true);
                        }
                    }
                }
            }


            if (null != issue.getPriority() && null != issue.getPriority().getId()) {
                if (issue.getPriority().getId().toString().equals(ReadConfig.getUcpFieldValue("PRIORITY_HIGH"))) {
                    kBonusRecord.setIsColor(true);
                }
            }
            kBonusRecords.add(kBonusRecord);
        }
        // 执行分页查询
        Page<CommonFind> page = new Page<CommonFind>();
        // 执行分页查询
        page.setPageSize(s.getMaxResults());
        page.setPageNo(Integer.parseInt(pageNo));
        page.setCount(s.getTotal());
        page.setList(kBonusRecords);
        page.setPageSize(s.getMaxResults());
        mav.addObject("page", page);
        mav.addObject("priorities", findPriorities());
        mav.addObject("priority", priority);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("text", text);
        addFilter(projectKey, issueTypeId, mav);
        return mav;
    }

    /**
     * 所有case
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("allCases")
    public ModelAndView allCases(HttpServletRequest request, String projectId, String issueTypeId, String text, String pageNo, String priority, String summary, String status, String startDate, String endDate, String creator, String sort, String sortBy, String preorder, String assignee) throws Exception {
        if (StringUtils.isBlank(pageNo)) {
            pageNo = "0";
        }
        ModelAndView mav = new ModelAndView("dsc/allCases");
        List<CommonFind> commonFinds = new ArrayList<CommonFind>();
        String sql = "";
        List<String> statusList = new ArrayList<String>();
        if (StringUtils.isNotBlank(status)) {
            statusList.add(status);
        }
        List<String> projectIds = new ArrayList<String>();
        if (StringUtils.isBlank(projectId)) {
            projectIds = SessionUtils.getProjectIds();
        } else {
            projectIds.add(projectId);
        }
        List<Long> issueTypeIds = new ArrayList<Long>();
        if (StringUtils.isBlank(issueTypeId)) {
            issueTypeIds = getIssueTypeIds(projectId);
        } else {
            issueTypeIds.add(Long.parseLong(issueTypeId));
        }

        String endDate1 = endDate;
        endDate1 = StringUtils.isEmpty(endDate1) ? endDate1 : DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endDate1, "yyyy-MM-dd"), 1), "yyyy-MM-dd");
        if (StringUtils.isNotEmpty(text)) {
            Matcher m1 = Global.PATTERN.matcher(text);
            //解决报错问题
            text = java.net.URLDecoder.decode(m1.replaceAll("").trim(), "UTF-8");
            sql = jql(projectIds, issueTypeIds, text, creator, assignee, null, null, null, null, null, false);
        } else {
            sql = jql(projectIds, issueTypeIds, null, creator, assignee, statusList, priority, summary, startDate, endDate1, false);
        }
        SearchResult s = getClient().getSearchClient().searchJql(sql, pageSize, (Integer.parseInt(pageNo) - 1) * pageSize, null).claim();
        for (Issue issue : s.getIssues()) {
            CommonFind commonFind = new CommonFind();
            commonFind.setSummary(issue.getSummary());
            if (issue.getAssignee() != null && StringUtils.isNotEmpty(issue.getAssignee().getName())) {
                commonFind.setAssignee(issue.getAssignee().getName());
            }
            commonFind.setCreationDate(issue.getCreationDate());
            if (null != issue.getPriority()) {
                commonFind.setPriority(issue.getPriority().getName());
            }
            commonFind.setKey(issue.getKey());
            if (null != issue.getStatus()) {
                commonFind.setJiraStatus(issue.getStatus().getName());
            }
            DscCaseConfig dscCaseConfig = getPageUrl(issue.getIssueType().getId(), issue.getProject().getId());
            if (null != dscCaseConfig) {
                commonFind.setLv1Page(dscCaseConfig.getLv1Page());
                commonFind.setLv2Page(dscCaseConfig.getLv2Page());
                commonFind.setViewPage(dscCaseConfig.getViewPage());
            }
            DscTaskLock lock = dscTaskLockService.getByCode(issue.getKey());
            if (null != lock) {
                commonFind.setLockUserName(lock.getUserName());
            }
            if (null != issue.getIssueType()) {
                commonFind.setType(issue.getIssueType().getName());
            }
//            Iterable<IssueField> ie = issue.getFields();
            commonFinds.add(commonFind);
        }
        Page<CommonFind> page = new Page<CommonFind>();
        // 执行分页查询
        page.setPageSize(s.getMaxResults());
        page.setPageNo(Integer.parseInt(pageNo));
        page.setCount(s.getTotal());
        page.setList(commonFinds);
        mav.addObject("page", page);
        mav.addObject("assignee", assignee);
        mav.addObject("creator", creator);
        mav.addObject("statues", findCommonSelects(projectId, issueTypeId, request));
        mav.addObject("priorities", findPriorities());
        mav.addObject("priority", priority);
        mav.addObject("status", status);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("text", text);
        //过滤条件
        addFilter(projectId, issueTypeId, mav);
        // todo
        findCommonSelects(projectId, issueTypeId, request);
        mav.addObject("status", status);
        return mav;
    }

    public DscCaseConfig getPageUrl(Long issueId, Long projectId) {
        List<DscCaseConfig> dscCaseConfigs = dscCaseConfigService.findList(new DscCaseConfig());
        for (DscCaseConfig dscCaseConfig : dscCaseConfigs) {
            if (dscCaseConfig.getCaseCode().equals(issueId.toString()) && dscCaseConfig.getProjectId().equals(String.valueOf(projectId))) {
                return dscCaseConfig;
            }
        }
        return null;
    }

    @RequestMapping(value = "caseGrade", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage caseGrade(String key, String statusId) throws Exception {
        final IssueRestClient client = getClient().getIssueClient();
        Issue issue = client.getIssue(key).claim();
        //更新流程状态
        TransitionInput tinput = new TransitionInput(Integer.parseInt(statusId));
        client.transition(issue, tinput);
        return ResponseMessage.success(true);
    }

    /**
     * 通用创建jira数据方法
     *
     * @param commonName
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "grade", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage grade(CommonName commonName, HttpServletRequest request) throws Exception {
        logger.info("通用 grade 入参 【{}】", JSONObject.toJSON(commonName).toString());
        String key = commonName.getKey();
        BasicIssue promise;
        String projectKey = commonName.getProjectKey();
        if (StringUtils.isBlank(projectKey)) {
            projectKey = getProjectKey(commonName.getIssueTypeId());
        }
        final IssueRestClient client = getClient().getIssueClient();
        try {
            if (!StringUtils.isNoneBlank(key)) {
                IssueType issueType = getIssueType(commonName.getIssueTypeId());
                if (null != issueType) {
                    IssueInputBuilder builder = new IssueInputBuilder(projectKey, issueType.getId());
//                    Field[] fields = commonName.getClass().getFields();
                    //createCase(fields,commonName,builder);
                    builder.setSummary(issueType.getName());
                    final IssueInput input = builder.build();
                    promise = client.createIssue(input).claim();
                    key = promise.getKey();
                }
            }

        } catch (RestClientException e) {
            return ResponseMessage.error(MessageUtils.returnErrors(e.getErrorCollections(), findAllFields()));
        }
        return ResponseMessage.success(new IssueResponse(key, null, null));
    }


    public void addFilter(String projectId, String issueTypeId, ModelAndView mav) {
        //所有项目
        List<Menu> menus = SessionUtils.getMenus();
        mav.addObject("projects", menus);
        mav.addObject("projectId", projectId);
        //所有issueType
        if (StringUtils.isBlank(projectId)) {
            mav.addObject("issueTypes", SessionUtils.getIssueTypes());
        } else {
            for (Menu menu : menus) {
                if (projectId.equals(menu.getId())) {
                    mav.addObject("issueTypes", menu.getMenus());
                    break;
                }
            }
        }
        mav.addObject("issueTypeId", issueTypeId);
    }


    //根据项目key,issueType过滤status
    @SuppressWarnings("unused")
    public List<CommonSelect> findCommonSelects(String projectId, String issueTypeId, HttpServletRequest request) {
        ServletContext context = request.getSession().getServletContext();
        String strs = "";
        StringBuffer parentIds = new StringBuffer();
        if (StringUtils.isNotBlank(projectId)) {
            parentIds.append(projectId).append(",");
        }
        if (StringUtils.isNotBlank(issueTypeId)) {
            parentIds.append(issueTypeId).append(",");
        }
        if (parentIds.toString().length() > 0) {
            strs = parentIds.substring(0, parentIds.lastIndexOf(","));
        }
        List<Menu> menus = menuService.findMenusByParentIds(strs, 3);
        List<CommonSelect> commonSelectLists = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (Menu menu : menus) {
            if (!list.contains(menu.getHref())) {
                commonSelectLists.add(new CommonSelect(menu.getHref(), menu.getName(), null));
            }
            list.add(menu.getHref());
        }
        return commonSelectLists;
    }


    /**
     * 获取jira的附件
     *
     * @param jiraID
     * @param request
     * @return
     */
    @RequestMapping("getIssueAttachments")
    @ResponseBody
    public ResponseMessage getIssueAttachments(String jiraID, HttpServletRequest request) {
        logger.info("Begin:CommonController.getIssueAttachments() jiraID=" + jiraID);

        Issue issue = getClient().getIssueClient().getIssue(jiraID).claim();
        List<Attachment> attachmentList = findAttachments(issue.getAttachments());

        logger.info("End:CommonController.getIssueAttachments() attachmentList=" + attachmentList);

        return ResponseMessage.success(attachmentList);
    }

    private String getSearchL1CaseJql(List<String> projectKeys, List<Long> sueTypeIds, String text, String jiraUserName, String startDate, String endDate, boolean priorityOrder, String preorder) throws Exception {
        StringBuilder extJql = new StringBuilder();
        extJql.append("and ((creator=").append(jiraUserName).append(")");
        return this.jql(projectKeys, sueTypeIds, text, null, null, null, null, null, startDate, endDate, extJql.toString(), priorityOrder);
    }

    @SuppressWarnings("unused")
    private Map<String, String> getL1StatusMap() throws Exception {
        JiraRestClient restClient = getClient();
        Iterable<Status> iter = restClient.getMetadataClient()
                .getStatuses().claim();
        Map<String, String> map = new HashMap<>();
        for (Status status : iter) {
            map.put(status.getId().toString(), status.getName());
        }
        return map;
    }

    @RequestMapping("uploadFtpAttach")
    @ResponseBody
    public ResponseMessage uploadFtpAttach(@RequestParam("file") MultipartFile file) {
        try {
            JSONObject object = FtpUtils.uploadSignalFile(file);
            if(object != null && !object.isEmpty()) {
                object.put("errcode", ResponseErrorCode.SUCCESS.getCode());
                return ResponseMessage.success(object);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return ResponseMessage.error(-1);
    }


    @RequestMapping("uploadFtpAttachActivityNo")
    @ResponseBody
    public ResponseMessage uploadFtpAttachActivityNo(@RequestParam("file") MultipartFile file,String activityNo) {
        try {
            JSONObject object = FtpUtils.uploadSignalFile(file,activityNo);
            if (object != null && !object.isEmpty()) {
                object.put("errcode", ResponseErrorCode.SUCCESS.getCode());
                return ResponseMessage.success(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        return ResponseMessage.error(-1);
    }

    @RequestMapping("delFtpAttach")
    @ResponseBody
    public ResponseMessage delFtpAttach(String filePath) {
        try {
            if(StringUtils.isNotBlank(filePath)) {
                Attach attach = attachService.getByFilePath(filePath);
                if(attach != null){
                    attachService.deleteByFilePath(attach);
                }
            }

            if(FtpUtils.delFtpFile(filePath)) {
                return ResponseMessage.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.error(-1);
    }

    @RequestMapping("downloadFtpAttach")
    public void downloadFtpAttach(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            String fileName = request.getParameter("fileName");
            Attach attach = attachService.getByFileName(fileName);
            if(attach != null){
                String originalFileName = attach.getOriginalFileName();
                String baseDir = attach.getFilePath().replaceAll(fileName, "").replaceAll(Global.getConfig(Global.FTP_PATH_URL), "");
                downloadAttach(response, baseDir, originalFileName, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("downloadUrgentFtpAttach")
    public void downloadUrgentFtpAttach(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");

            String baseDir = request.getParameter("baseDir");
            String fileName = request.getParameter("fileName");
            String originalFileName = request.getParameter("originalFileName");
            //logger.info("################1=" + fileName);
            //logger.info("################2=" + new String(fileName.getBytes(), "ISO8859-1"));
            //logger.info("################3=" + new String(fileName.getBytes(), "GBK"));
            downloadAttach(response, baseDir, originalFileName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadAttach(HttpServletResponse response, String baseDir, String originalFileName, String fileName) throws Exception{
        FTPClient ftpClient = new FTPClient();
        FtpUtils.connectFtp(ftpClient);

        ftpClient.enterLocalPassiveMode();
        logger.info("CommonController.downloadAttach--- before change working directory:" + ftpClient.printWorkingDirectory());
        logger.info("CommonController.downloadAttach--- baseDir:" + baseDir);
//        logger.info("################1=" + originalFileName);
//        logger.info("################2=" + URLEncoder.encode(originalFileName, "UTF-8"));
//        logger.info("################3=" + URLEncoder.encode(originalFileName, "ISO8859-1"));
//        logger.info("################4=" + URLEncoder.encode(originalFileName, "GBK"));
        ftpClient.changeWorkingDirectory(baseDir);
        logger.info("CommonController.downloadAttach--- after change working directory:" + ftpClient.printWorkingDirectory());
        FileOutputStream fos = null;
        response.setContentType("application/form-data");
        response.setCharacterEncoding("utf-8");
        // 将文件的命名以及格式写进信息头设置中
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(originalFileName, "UTF-8"));
        try {
            ftpClient.setBufferSize(1024);
            // 设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 将ftp上的文件传递给response对象，返回前端。
            ftpClient.retrieveFile(fileName, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            FtpUtils.closeFtp(ftpClient);
        }
    }

    @RequestMapping("downloadZipAttach")
    public void downloadZipAttach(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");

            String zipFileName = request.getParameter("fileName");
            String type = request.getParameter("type");
            String reportId = request.getParameter("reportId");

            zipFileName = new String(zipFileName.getBytes("ISO-8859-1"), "utf-8");

            String downloadFilename = zipFileName + ".zip";//文件的名称

            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFilename, "UTF-8"));// 设置在下载框默认显示的文件名

            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            List<Attach> lists = attachService.findListByClassAndType(new Attach(QaReport.class.getName(), reportId, type));

            if(lists != null && !lists.isEmpty()){
                for(Attach ath : lists){
                    URL url = new URL(ath.getFilePath());
                    zos.putNextEntry(new ZipEntry(ath.getOriginalFileName()));
                    InputStream fis = url.openConnection().getInputStream();
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
