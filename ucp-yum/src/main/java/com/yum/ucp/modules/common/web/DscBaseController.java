/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yum.ucp.modules.common.web;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yum.ucp.common.utils.JiraRestClientObjListener;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.common.entity.CommonName;
import com.yum.ucp.modules.common.entity.IssueResponse;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class DscBaseController extends BaseController {
    private final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

    protected static final String UCP = ReadConfig.getSsoValue("UCP");
    /**
     * 默认优先级P2
     */
    protected static final String P2 = ReadConfig.getUcpFieldValue("P2");
    /**
     * 优先级Highest
     */
    protected static final String P0 = ReadConfig.getUcpFieldValue("P0");
    /**
     * 发布状态-已发布
     */
    protected static final String RELEASE_STATUS_RELEASED = ReadConfig.getUcpFieldValue("release_status_released");
    /**
     * 活动JIRA状态-资深员工创建
     */
    protected static final String ACTIVITY_JIRA_STATUS_SENIOR = ReadConfig.getUcpFieldValue("activity_jira_status_senior");
    /**
     * 活动JIRA状态-一线员工配置
     */
    protected static final String ACTIVITY_JIRA_STATUS_L1 = ReadConfig.getUcpFieldValue("activity_jira_status_L1");
    /**
     * 发布状态-未发布
     */
    protected static final String RELEASE_STATUS_UNRELEASED = ReadConfig.getUcpFieldValue("release_status_unreleased");
    /**
     * 活动状态-未认领
     */
    protected static final String ACTIVITY_STATUS_UNCLAIMED = ReadConfig.getUcpFieldValue("activity_status_unclaimed");
    /**
     * 活动状态-配置中
     */
    protected static final String ACTIVITY_STATUS_CONFIGING = ReadConfig.getUcpFieldValue("activity_status_configing");
    /**
     * 活动状态-未测试
     */
    protected static final String ACTIVITY_STATUS_NOT_TESTED = ReadConfig.getUcpFieldValue("activity_status_not_tested");
    /**
     * 活动状态-测试中
     */
    protected static final String ACTIVITY_STATUS_TESTING = ReadConfig.getUcpFieldValue("activity_status_testing");
    /**
     * 活动状态-测试不通过
     */
    protected static final String ACTIVITY_STATUS_TEST_NOT_PASS = ReadConfig.getUcpFieldValue("activity_status_test_not_pass");
    /**
     * 活动状态-测试通过
     */
    protected static final String ACTIVITY_STATUS_TEST_PASS = ReadConfig.getUcpFieldValue("activity_status_test_pass");

    /**
     * 活动jira状态-QA校验
     */
    protected static final String ACTIVITY_JIRA_STATUS_QA = ReadConfig.getUcpFieldValue("activity_jira_status_qa");

    /**
     * 活动jira状态-DONE
     */
    protected static final String ACTIVITY_JIRA_STATUS_DONE = ReadConfig.getUcpFieldValue("activity_jira_status_done");

    /**
     * 紧急活动jira状态-指定人员处理
     */
    protected static final String SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN = ReadConfig.getUcpFieldValue("special_activity_jira_status_assign");
    /**
     * 活动状态-测试已反馈
     */
    protected static final String ACTIVITY_STATUS_TEST_FEEDBACK = ReadConfig.getUcpFieldValue("activity_status_test_feedback");

    /**
     * 资深员工反馈到QA
     */
    protected static final String SENIOR_TO_QA = ReadConfig.getUcpFieldValue("SENIOR_TO_QA");

    /**
     * QA退回到资深员工
     */
    protected static final String QA_TO_SENIOR = ReadConfig.getUcpFieldValue("QA_TO_SENIOR");

    /**
     * QA测试通过
     */
    protected static final String QA_TO_DONE = ReadConfig.getUcpFieldValue("QA_TO_DONE");

    /**
     * 紧急活动资深员工分配任务
     */
    protected static final String SENIOR_TO_ASSIGN = ReadConfig.getUcpFieldValue("SENIOR_TO_ASSIGN");

    protected static final String ASSIGN_TO_DONE = ReadConfig.getUcpFieldValue("ASSIGN_TO_DONE");

    protected static final String ROLE_TYPE_SENIOR = "3";
    protected static final String ROLE_TYPE_QA = "5";
    protected static final String ROLE_TYPE_ADMIN = "6";

    public static JiraRestClient recilent = null;

    public static JiraRestClient recilentTask = null;

    public static Cache<String, JiraRestClient> cacheRecilent = CacheBuilder.newBuilder().concurrencyLevel(16).initialCapacity(200).maximumSize(1600)
            .expireAfterWrite(600L, TimeUnit.SECONDS).removalListener(new JiraRestClientObjListener()).build();

    public static Cache<String, JiraRestClient> cacheRecilentTask = CacheBuilder.newBuilder().concurrencyLevel(16).initialCapacity(200).maximumSize(1600)
            .expireAfterWrite(600L, TimeUnit.SECONDS).removalListener(new JiraRestClientObjListener()).build();

    public String getUserName() throws Exception {
        return SessionUtils.getUserName();
    }

    public String getJiraUserName() throws Exception {
        return SessionUtils.getJiraUserName();
    }

    public String getProjectKey(String issueTypeId) {
        return menuService.getProjectKey(issueTypeId);
    }

    public JiraRestClient getClient() {
//        if(null==  UserUtils.getCache(UserUtils.getUser().getId())){

        URI jiraServerUri = null;
        String url = ReadConfig.getSsoValue("url");
        try {
            jiraServerUri = new URI(url);
        } catch (URISyntaxException e) {
            logger.error("jira server url has error,error message:{}", e.getMessage());
        }
        String jiraUserName = SessionUtils.getJiraUserName();
        String jiraPassword = SessionUtils.getJiraPassword();
        String key = new StringBuilder().append(url).append(jiraUserName).append(jiraPassword).toString();
        recilent = cacheRecilent.getIfPresent(key);
        if (recilent == null) {
            recilent = factory.createWithBasicHttpAuthentication(jiraServerUri, jiraUserName, jiraPassword);

            cacheRecilent.put(key, recilent);
        }
//        UserUtils.putCache(UserUtils.getUser().getId(),recilent);
//        }
        return recilent;
    }

    /**
     * 更新jira状态
     * @param jiraNo
     * @param status
     */
    public void updateIssueStatus(String jiraNo, String status) {
        //更新流程状态
        final IssueRestClient client = getClient().getIssueClient();
        Issue issue = client.getIssue(jiraNo).claim();
        TransitionInput tinput = new TransitionInput(Integer.parseInt(status));
        client.transition(issue, tinput);
    }


    //获取勾选的IssueType
    public List<IssueType> getIssueTypes(String projectKey) throws Exception {
        List<Long> ids = SessionUtils.getMenuIds();
        List<IssueType> issueTypes = new ArrayList<IssueType>();
        if (null == ids || ids.isEmpty()) {
            return issueTypes;
        }
        JiraRestClient restClient = getClient();
        Iterator<IssueType> issueTypeIterator;
        if (StringUtils.isNotBlank(projectKey)) {
            Project project = restClient.getProjectClient().getProject(projectKey).claim();
            issueTypeIterator = project.getIssueTypes().iterator();

        } else {
            issueTypeIterator = restClient.getMetadataClient().getIssueTypes().claim().iterator();
        }
        while (issueTypeIterator.hasNext()) {
            IssueType issueType = issueTypeIterator.next();
            if (ids.contains(issueType.getId())) {
                issueTypes.add(issueType);
            }
        }
        return issueTypes;
    }

    //获取勾选的IssueType
    public IssueType getIssueType(String id) throws Exception {
        IssueType type = null;
        Menu menu = menuService.get(id);
        if (null == menu) {
            return type;
        }
        JiraRestClient restClient = getClient();
        Iterable<IssueType> iter = restClient.getMetadataClient()
                .getIssueTypes().claim();
        /*
             * uat 上phdi的project id 和电子发票的case id 一样，导致此逻辑与预期不一致。
             * 当电子发票case id 传入时， menuService.get(id)，得到的并不是电子发票menu,而是phdi的menu
             *
             * 经调查发现，电子发票传入的issueTypeId 就是uat上的issueTypeId
             * 故，如果menu的target为空时，可判断为id冲突，直接用入参的id
         */
        if (StringUtils.isEmpty(menu.getTarget())) {
            menu.setTarget(id);
        }

        for (IssueType issueType : iter) {
            if (issueType.getId().intValue() == Integer.parseInt(menu.getTarget())) {
                logger.info("jira issueType==>{}", issueType.toString());
                type = issueType;
                break;
            }
        }

        return type;
    }

    //电子发票获取勾选的IssueType
    public IssueType getInvoiceIssueType(String target) throws Exception {
        JiraRestClient restClient = getClient();
        Iterable<IssueType> iter = restClient.getMetadataClient()
                .getIssueTypes().claim();
        for (IssueType issueType : iter) {
            if (issueType.getId().intValue() == Integer.parseInt(target)) {
                return issueType;
            }
        }
        return null;
    }

    //summary
    public String getSummary(String userName, String phone, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(userName).append(phone).append(type);
        return sb.toString();
    }

    public String getSummaryForInvoice(String userName, String phone, String type, String questionOne) {
        StringBuilder sb = new StringBuilder();
        sb.append(userName).append(phone).append(type).append(questionOne);
        return sb.toString();
    }

    //获取勾选的IssueTypeIds
    public List<Long> getIssueTypeIds(String projectKey) throws Exception {
        List<Long> ids = new ArrayList<Long>();
        List<IssueType> issueTypes = getIssueTypes(projectKey);
        for (IssueType issueType : issueTypes) {
            ids.add(issueType.getId());
        }
        return ids;
    }

    /**
     * L2处理
     *
     * @return
     * @throws Exception
     */
    public List<String> getStatus() throws Exception {
        JiraRestClient restClient = getClient();
        Iterable<Status> iter = restClient.getMetadataClient()
                .getStatuses().claim();
        List<String> lists = new ArrayList<String>();
        for (Status issueType : iter) {
            //TODO:状态
            if (issueType.getId().toString().equals(ReadConfig.getUcpFieldValue("VERIFICATION_L2_DEAL_INFO"))
                    || issueType.getId().toString().equals(ReadConfig.getUcpFieldValue("KGLOD_L2_DEAL_INFO"))) {
                lists.add(issueType.getName());
            }
        }
        return lists;
    }

    /**
     * L1处理
     *
     * @return
     * @throws Exception
     */
    public List<String> getL1Status() throws Exception {
        JiraRestClient restClient = getClient();
        Iterable<Status> iter = restClient.getMetadataClient()
                .getStatuses().claim();
        List<String> lists = new ArrayList<String>();
        for (Status issueType : iter) {
            if (issueType.getId().toString().equals(ReadConfig.getUcpFieldValue("RECORD_INFO"))) {
                lists.add(issueType.getName());
                break;
            }
        }
        return lists;
    }

    public String jql(List<String> projectKeys, List<Long> type, String text, String creator, String assignee, List<String> status,
                      String priority, String summary, String startDate, String endDate, Boolean priorityOrder) {
        return this.jql(projectKeys, type, text, creator, assignee, status, priority, summary, startDate, endDate, null, priorityOrder);
    }

    /**
     * 拼装sql
     *
     * @param projectKeys
     * @param type
     * @param text
     * @param creator
     * @param assignee
     * @param status
     * @param priority
     * @param summary
     * @param startDate
     * @param endDate
     * @param extJql
     * @param priorityOrder
     * @return
     */
    public String jql(List<String> projectKeys, List<Long> type, String text, String creator, String assignee, List<String> status,
                      String priority, String summary, String startDate, String endDate, String extJql, Boolean priorityOrder) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        StringBuilder strBuilder = new StringBuilder();
        if (null != projectKeys && !projectKeys.isEmpty()) {
            strBuilder.append("project in (");
            int projectKeysSize = projectKeys.size();
            int index = 0;
            for (String str : projectKeys) {
                if (index != projectKeysSize - 1) {
                    strBuilder.append(str).append(",");
                } else {
                    strBuilder.append(str).append(")");
                }
                index++;
            }
        }
        if (null != type && !type.isEmpty()) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and type in (");
            } else {
                strBuilder.append("  type in (");
            }
            int index = 0;
            int typeSize = type.size();
            for (Long str : type) {
                if (index != typeSize - 1) {
                    strBuilder.append(str).append(",");
                } else {
                    strBuilder.append(str).append(")");
                }
                index++;
            }
        }
        if (StringUtils.isNotBlank(text)) {
            Matcher m1 = p.matcher(text);
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  text ~ '").append(m1.replaceAll("").trim()).append("'");
            } else {
                strBuilder.append("   text ~ '").append(m1.replaceAll("").trim()).append("'");
            }

        }
//        if (StringUtils.isNotBlank(phoneNumber)) {
//            if(stringBuffer.toString().length()>0){
//                stringBuffer.append(CommonConstants.CALL_PHONE + " ~ " + phoneNumber);
//            }else{
//                stringBuffer.append(" and  " + CommonConstants.CALL_PHONE + " ~ " + phoneNumber);
//            }
//
//        }
        if (StringUtils.isNotBlank(creator)) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  creator =").append(creator);
            } else {
                strBuilder.append("    creator =").append(creator);
            }
        }
        if (StringUtils.isNotBlank(assignee)) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  assignee in (").append(assignee).append(")");
            } else {
                strBuilder.append("   assignee in (").append(assignee).append(")");
            }
        }
        if (StringUtils.isNotBlank(priority)) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  priority =").append(priority);
            } else {
                strBuilder.append("   priority =").append(priority);
            }

        }
        if (StringUtils.isNotBlank(summary)) {
            Matcher m2 = p.matcher(summary);
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  summary ~").append(m2.replaceAll("").trim());
            } else {
                strBuilder.append("   summary ~").append(m2.replaceAll("").trim());
            }

        }
        if (StringUtils.isNotBlank(startDate)) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and  created >='").append(startDate).append("'");
            } else {
                strBuilder.append("  created >='").append(startDate).append("'");
            }

        }
        if (StringUtils.isNotBlank(endDate)) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and created<='").append(endDate).append("'");
                ;
            } else {
                strBuilder.append("  created<='").append(endDate).append("'");
                ;
            }
        }
        if (null != status && !status.isEmpty()) {
            if (strBuilder.toString().length() > 0) {
                strBuilder.append(" and status in (");
            } else {
                strBuilder.append("  status in (");
            }
            int index = 0;
            int statusSize = status.size();
            for (String str : status) {
                if (index != statusSize - 1) {
                    strBuilder.append(str).append(",");
                } else {
                    strBuilder.append(str).append(")");
                }
                index++;
            }
        }
        if (StringUtils.isNotBlank(extJql)) {
            strBuilder.append(" ").append(extJql).append(" ");
        }
        if (priorityOrder) {
//            String fieldName = ReadConfig.getKfcFieldValue("DEAL_L2_NAME");
//            strBuilder.append(" AND (").append(fieldName).append(" is EMPTY OR ")
//                    .append(fieldName).append(" ~ ").append(SessionUtils.getJiraUserName())
//                    .append(") ORDER BY priority DESC, created DESC,").append(fieldName).append(" DESC");
            strBuilder.append(" ORDER BY priority DESC, created DESC");
        } else {
            strBuilder.append(" order by created desc");
        }
        return strBuilder.toString();

    }

    public String getClientId() throws Exception {
        return ReadConfig.getSsoValue("client_id");
    }

    public String getClientSecret() throws Exception {
        return ReadConfig.getSsoValue("client_secret");
    }

    public String getCallback() throws Exception {
        return ReadConfig.getSsoValue("callback");
    }

    //获取所有字段
    public Map<String, String> findAllFields() {
//        Map<String, String> maps = new HashMap<String, String>();
//        try
//        {
//            MetadataRestClient metadataRestClient = getClient().getMetadataClient();
//            Iterator<Field> fields = metadataRestClient.getFields().get().iterator();
//            while (fields.hasNext())
//            {
//                Field field = fields.next();
//                maps.put(field.getId(), field.getName());
//            }
//        } catch (InterruptedException | ExecutionException e)
//        {
//            logger.error("action has error,error message:{}", e.getMessage());
//        }
//        return maps;
        return null;
    }

    //获取优先级
    public List<Priority> findPriorities() {
        List<Priority> priorities = new ArrayList<Priority>();
        Iterator<Priority> priorityIterable = getClient().getMetadataClient().getPriorities().claim().iterator();
        while (priorityIterable.hasNext()) {
            priorities.add(priorityIterable.next());
        }
        return priorities;
    }

    //升级返回数据
    public IssueResponse findGradeData(String key, String projectKey) {
        logger.info("准备case【key={},projectKey={}】进行升级。", key, projectKey);
        Long roleId = getRoleId();
        ProjectRole projectRole = null;
        boolean flag = false;
        //流程状态
        URI commentUri = null;
        List<Transition> transitionList = new ArrayList<>();
        Transition transi = new Transition("请选择", -1, null);
        transitionList.add(transi);
        List<RoleActor> roleActorList = new ArrayList<>();
        final IssueRestClient client = getClient().getIssueClient();
        List<IssueRestClient.Expandos> expand = new ArrayList<>();
        expand.add(IssueRestClient.Expandos.TRANSITIONS);
        Issue issue = client.getIssue(key, expand).claim();
        logger.info("升级case的内容为【key={},projectKey={}，issue={}】。", key, projectKey, issue);
        Iterator<Transition> transitions = getClient().getIssueClient().getTransitions(issue).claim().iterator();
        while (transitions.hasNext()) {
            Transition transition = transitions.next();
            if (transition.getFields().iterator().hasNext()) {
                try {
                    commentUri = new URI(ReadConfig.getSsoValue("url") + "rest/api/2/project/" + projectKey);
                    projectRole = getClient().getProjectRolesRestClient().getRole(commentUri, roleId).claim();
                    Iterator<RoleActor> roleActorIterator = projectRole.getActors().iterator();
                    while (roleActorIterator.hasNext()) {
                        RoleActor roleActor = roleActorIterator.next();
                        roleActorList.add(new RoleActor(Long.parseLong(String.valueOf(transition.getId())), null, null, transition.getName() + "-" + roleActor.getDisplayName(), null));

                    }
                } catch (URISyntaxException e) {
                    logger.error("jira Issue Response has error,error message:{}", e.getMessage());
                }
                continue;
            }

            if (!StringUtils.isNotBlank(projectKey)) {
                logger.error("jira Issue Response has not projectKey");
                continue;
            }

            if (projectKey.equals(ReadConfig.getSsoValue("UCP"))) {
//                if (transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("CONSULT_L1_TICKET_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("CONSULT_L2_TICKET_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("CONSULT_L1_CARD_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("CONSULT_L2_CARD_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("VERIFICATION_L2_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("CARD_REBACK_L2_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("TICKET_REBACK_L1_DONE"))
//                        && transition.getId() != Integer.parseInt(ReadConfig.getKfcFieldValue("TICKET_REBACK_L2_DONE"))) {
//                    transitionList.add(transition);
//
//                }
            }

        }
        return new IssueResponse(issue.getKey(), transitionList, roleActorList);
    }

    public List<Comment> findComments(Iterable<Comment> comments) {
        List<Comment> commentList = new ArrayList<Comment>();
        if (null == comments) {
            return commentList;
        }
        Iterator<Comment> commentIterator = comments.iterator();
        while (commentIterator.hasNext()) {
            commentList.add(commentIterator.next());
        }
        Collections.sort(commentList, new Comparator<Comment>() {
            @Override
            public int compare(Comment arg0, Comment arg1) {
                if (arg0.getCreationDate().compareTo(arg1.getCreationDate()) >= 1) {
                    return -1;
                } else if (arg0.getCreationDate().compareTo(arg1.getCreationDate()) <= -1) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });
        return commentList;
    }

    public List<Attachment> findAttachments(Iterable<Attachment> attachments) {
        List<Attachment> attachmentList = new ArrayList<Attachment>();
        if (null == attachments) {
            return attachmentList;
        }
        Iterator<Attachment> commentIterator = attachments.iterator();
        while (commentIterator.hasNext()) {
            Attachment attachment = commentIterator.next();
            if (SessionUtils.getJiraUserName().equals(attachment.getAuthor().getName())) {
                attachment.setIsDelete(true);
            }
            attachmentList.add(attachment);
        }

        Collections.sort(attachmentList, new Comparator<Attachment>() {
            @Override
            public int compare(Attachment amt1, Attachment amt2) {
                if (amt1.getCreationDate().compareTo(amt2.getCreationDate()) >= 1) {
                    return -1;
                } else if (amt1.getCreationDate().compareTo(amt2.getCreationDate()) <= -1) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return attachmentList;
    }

    public List<ChangelogGroup> findChangelogGroups(Iterable<ChangelogGroup> changeLogs) {
        List<ChangelogGroup> changelogGroupList = new ArrayList<ChangelogGroup>();
        if (null == changeLogs) {
            return changelogGroupList;
        }
        Iterator<ChangelogGroup> changelogGroups = changeLogs.iterator();
        while (changelogGroups.hasNext()) {
            ChangelogGroup changelogGroup = changelogGroups.next();
            changelogGroupList.add(changelogGroup);
        }
        Collections.sort(changelogGroupList, new Comparator<ChangelogGroup>() {
            @Override
            public int compare(ChangelogGroup arg0, ChangelogGroup arg1) {
                if (arg0.getCreated().compareTo(arg1.getCreated()) >= 1) {
                    return -1;
                } else if (arg0.getCreated().compareTo(arg1.getCreated()) <= -1) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });
        return changelogGroupList;
    }

    /**
     * 活动相关状态
     *
     * @param model
     */
    protected void activityStatus(Model model) {
        //未测试
        model.addAttribute("actStatusNotTested", ACTIVITY_STATUS_NOT_TESTED);
        //测试中
        model.addAttribute("actStatusTesting", ACTIVITY_STATUS_TESTING);
        //测试不通过
        model.addAttribute("actStatusNotPass", ACTIVITY_STATUS_TEST_NOT_PASS);
        //测试通过
        model.addAttribute("actStatusPass", ACTIVITY_STATUS_TEST_PASS);
        //已反馈
        model.addAttribute("actStatusTestFeedBack", ACTIVITY_STATUS_TEST_FEEDBACK);

        //资深员工反馈到QA
        model.addAttribute("seniorToQa", SENIOR_TO_QA);
        //QA退回到资深员工
        model.addAttribute("qaToSenior", QA_TO_SENIOR);
    }

    protected Long getRoleId() {
        return Long.parseLong(ReadConfig.getUcpFieldValue("ROLE_ID"));
    }

    protected Long getPriorityId() {
        return Long.parseLong(ReadConfig.getUcpFieldValue("PRIORITY_HIGH"));
    }
}
