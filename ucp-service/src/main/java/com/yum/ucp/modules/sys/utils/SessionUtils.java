package com.yum.ucp.modules.sys.utils;

import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.yum.ucp.modules.sys.entity.Menu;
import org.apache.shiro.session.Session;

import java.util.List;

/**
 * session工具类
 *
 * @author Administrator
 * @version [版本号, 2016年5月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SessionUtils
{

    public static final String USER_NAME = "userName";
    public static final String USER_LOGIN_NAME = "userLoginName";
    public static final String SSO_SERVICE = "ssoService";
    public static final String JIRA_USER_NAME = "jiraUserName";
    public static final String JIRA_USER_PASSWORD = "jiraUserPassword";
    public static final String TYPE = "type";
    public static final String ROLE_TYPE = "roleType";
    public static final String IS_CLICK = "is_click";
    public static final String MENU_IDS = "menuIds";
    public static final String MENUS = "menus";
    public static final String PROJECT_IDS = "projectIds";
    public static final String PROJECT_KEYS = "projectKeys";
    public static final String ISSUE_TYPES = "issueTypes";
    public static final String GROUP_ID = "groupId";
    public static final String PHONE_AGENT = "phoneAgent";
    public static final String PHONE_PASS = "phonePass";
    public static final String PHONE_EXTEN = "phoneExten";
    public static final String PHONE_URL = "phoneUrl";
    public static final String PHONE_URL2 = "phoneUrl2";
    public static final String FIND_PRIORITIES = "findPriorities";
    public static final String SSO_CODE = "ssoCode";
    public static final String FIND_STATUSES = "findStatues";
    public static final String HOT_LINE = "hotLine";
    public static final String ANNOUNCE = "Announce";
    public static final String MSG_PERMISSION = "msgPermission";
    /**
     * 获取所有优先级
     *
     * @return
     */
    public static String getFindPriorities()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(FIND_PRIORITIES);
    }

    /**
     * 设置所有优先级
     *
     * @param priorities
     */
    public static void setFindPriorities(List<Priority> priorities)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(FIND_PRIORITIES, priorities);
    }

    /**
     * 获取热线
     *
     * @return
     */
    public static String getHotLine()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(HOT_LINE);
    }

    /**
     * 设置热线
     *
     * @param hotLine
     */
    public static void setHotLine(String hotLine)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(HOT_LINE, hotLine);
    }

    /**
     * 获取所有状态
     *
     * @return
     */
    public static String getFindStatus()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(FIND_STATUSES);
    }

    /**
     * 设置所有状态
     *
     * @param statuses
     */
    public static void setFindStatus(List<Status> statuses)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(FIND_STATUSES, statuses);
    }

    /**
     * 清除session
     */
    public static void removeUserName()
    {
        Session session = UserUtils.getSubject().getSession();
        session.removeAttribute(USER_NAME);
    }

    /**
     * 获取登录账号
     *
     * @param userName
     */
    public static void setUserName(String userName)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(USER_NAME, userName);
    }

    /**
     * 获取登录账号
     *
     * @return
     */
    public static String getUserName()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(USER_NAME);
    }

    /**
     * 设置登录账号
     *
     * @param userLoginName
     */
    public static void setUserLoginName(String userLoginName)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(USER_LOGIN_NAME, userLoginName);
    }

    /**
     * 获取登录账号
     *
     * @return
     */
    public static String getUserLoginName()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(USER_LOGIN_NAME);
    }

    /**
     * 获取JIRA登录账号
     *
     * @param password
     */
    public static void setJiraPassword(String password)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(JIRA_USER_PASSWORD, password);
    }

    /**
     * 获取JIRA登录账号
     *
     * @return
     */
    public static String getJiraPassword()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(JIRA_USER_PASSWORD);
    }

    /**
     * 获取JIRA登录账号
     *
     * @param password
     */
    public static void setJiraUserName(String password)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(JIRA_USER_NAME, password);
    }

    /**
     * 获取JIRA登录账号
     *
     * @return
     */
    public static String getJiraUserName()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(JIRA_USER_NAME);
    }

    /**
     * 设置业务类型
     *
     * @param type
     */
    public static void setType(String type)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(TYPE, type);
    }

    /**
     * 获取业务类型
     *
     * @return
     */
    public static String getType()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(TYPE);
    }

    /**
     * 设置角色类型
     *
     * @param roleType
     */
    public static void setRoleType(String roleType)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(ROLE_TYPE, roleType);
    }

    /**
     * 是否点击下一步
     *
     * @param flag
     */
    public static void setIsClick(Boolean flag)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(IS_CLICK, flag);
    }

    /**
     * 是否点击下一步
     * <p>
     */
    public static Boolean getIsClick()
    {
        Session session = UserUtils.getSubject().getSession();
        return (Boolean) session.getAttribute(IS_CLICK);
    }

    /**
     * 获取角色类型
     *
     * @return
     */
    public static String getRoleType()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(ROLE_TYPE);
    }

    /**
     * 设置菜单Id集合
     *
     * @param menuIds
     */
    public static void setMenuIds(List<Long> menuIds)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(MENU_IDS, menuIds);
    }

    /**
     * 设置菜单集合
     *
     * @param menus
     */
    public static void setMenus(List<Menu> menus)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(MENUS, menus);
    }

    /**
     * 获取菜单集合
     * <p>
     */
    public static List<Menu> getMenus()
    {
        Session session = UserUtils.getSubject().getSession();
        return (List<Menu>) session.getAttribute(MENUS);
    }

    /**
     * 设置业务线ID集合
     *
     * @param keys
     */
    public static void setProjectIds(List<String> keys)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PROJECT_IDS, keys);
    }

    /**
     * 获取业务线ID集合
     * <p>
     */
    public static List<String> getProjectIds()
    {
        Session session = UserUtils.getSubject().getSession();
        return (List<String>) session.getAttribute(PROJECT_IDS);
    }

    /**
     * 设置项目key集合
     *
     * @param keys
     */
    public static void setProjectKeys(List<String> keys)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PROJECT_KEYS, keys);
    }

    /**
     * 获取项目key集合
     * <p>
     */
    public static List<String> getProjectKeys()
    {
        Session session = UserUtils.getSubject().getSession();
        return (List<String>) session.getAttribute(PROJECT_KEYS);
    }

    /**
     * 设置IssueType集合
     *
     * @param issueTypes
     */
    public static void setIssueTypes(List<Menu> issueTypes)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(ISSUE_TYPES, issueTypes);
    }

    /**
     * 获取IssueType集合
     * <p>
     */
    public static List<Menu> getIssueTypes()
    {
        Session session = UserUtils.getSubject().getSession();
        return (List<Menu>) session.getAttribute(ISSUE_TYPES);
    }

    /**
     * 获取菜单Id集合
     *
     * @return
     */
    public static List<Long> getMenuIds()
    {
        Session session = UserUtils.getSubject().getSession();
        return (List<Long>) session.getAttribute(MENU_IDS);
    }

    public static void setGroupId(String groupId)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(GROUP_ID, groupId);
    }

    public static String getGroupId()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(GROUP_ID);
    }

    public static void setPhoneAgent(String phoneAgent)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PHONE_AGENT, phoneAgent);
    }

    public static String getPhoneAgent()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(PHONE_AGENT);
    }

    public static void setPhonePass(String phonePass)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PHONE_PASS, phonePass);
    }

    public static String getPhonePass()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(PHONE_PASS);
    }

    public static void setPhoneExten(String phoneExten)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PHONE_EXTEN, phoneExten);
    }

    public static String getPhoneExten()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(PHONE_EXTEN);
    }

    public static void setPhoneUrl(String phoneUrl)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PHONE_URL, phoneUrl);
    }

    public static String getPhoneUrl()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(PHONE_URL);
    }

    public static void setPhoneUrl2(String phoneUrl2)
    {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(PHONE_URL2, phoneUrl2);
    }

    public static String getPhoneUrl2()
    {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(PHONE_URL2);
    }

    public static void setAnnounce(String announce){
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(ANNOUNCE, announce);
    }

    public static String getAnnounce(){
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(ANNOUNCE);
    }

    public static void setMsgPermission(String msgPermission) {
        Session session = UserUtils.getSubject().getSession();
        session.setAttribute(MSG_PERMISSION, msgPermission);
    }

    public static String getMsgPermission() {
        Session session = UserUtils.getSubject().getSession();
        return (String) session.getAttribute(MSG_PERMISSION);
    }
}
