package com.yum.ucp.modules.index;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.google.common.collect.Lists;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.security.shiro.session.SessionDAO;
import com.yum.ucp.common.utils.CookieUtils;
import com.yum.ucp.modules.common.entity.CommonSelect;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.func.service.DscLinkFunctionService;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.entity.DscUserJira;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.security.FormAuthenticationFilter;
import com.yum.ucp.modules.sys.security.SystemAuthorizingRealm;
import com.yum.ucp.modules.sys.service.DictService;
import com.yum.ucp.modules.sys.service.DscHotLineConfigService;
import com.yum.ucp.modules.sys.service.DscUserJiraService;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.sys.utils.SessionUtils;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import com.yum.ucp.modules.work.service.DscWorkCodeService;
import com.yum.ucp.modules.task.service.DscExportTaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/")
public class IndexController extends DscBaseController {

    @Autowired
    private DscLinkFunctionService dscLinkFunctionService;
    @Autowired
    private DscExportTaskService dscExportTaskService;
    @Autowired
    private DscUserJiraService dscUserJiraService;
    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private DscWorkCodeService dscWorkCodeService;

    @Autowired
    private DscHotLineConfigService dscHotLineConfigService;

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        //获取SSO地址
        model.addAttribute("sso_url", ReadConfig.getSsoValue("sso_url"));

        if (logger.isDebugEnabled()) {
            logger.info("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            setWorkCodeInfo(model);
            if(model.containsAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM)) {
                return "ucp/login";
            }
            return "redirect:" + adminPath + "/index";
        }
        return "ucp/login";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            setWorkCodeInfo(model);
            if(model.containsAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM)) {
                return "ucp/login";
            }
            return "redirect:" + adminPath + "/index";
        }
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()) {
            logger.info("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }
        return "ucp/login";
    }

    private void setWorkCodeInfo(Model model) {
        User user = UserUtils.getUser();
        if (null != user) {
            SessionUtils.setUserName(user.getName());
            SessionUtils.setUserLoginName(user.getLoginName());
            List<Dict> list = dscWorkCodeService.findRoleTypesByUserId(user.getId());
            if (null == list || list.size() == 0) {
                model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, "工号未设置，请联系管理员");
                return;
            }
            Dict roleTypeDict = list.get(0);
            String roleType = roleTypeDict.getValue();
            SessionUtils.setRoleType(roleType);
            List<DscWorkCode> workCodes = UserUtils.getWorkCodes(roleType);
            if(null != workCodes && workCodes.size() > 0) {
                DscWorkCode dscWorkCode = workCodes.get(0);
                SessionUtils.setPhoneAgent(dscWorkCode.getWorkCode());
            }
            DscUserJira dscUserJira = dscUserJiraService.get(user.getId());
            if(dscUserJira == null) {
                model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, "JIRA登录账号未配置，请联系管理员");
                return;
            }
            SessionUtils.setJiraUserName(dscUserJira.getLoginName());
            SessionUtils.setJiraPassword(dscUserJira.getPassword());
            SessionUtils.setUserName(user.getName());
            try
            {
                getClient().getSessionClient().getCurrentSession().claim();
            } catch (RestClientException e)
            {
                String statusCode = e.getStatusCode().get().toString();
                if (StringUtils.equals("403", statusCode) || StringUtils.equals("401", statusCode))
                {
                    UserUtils.getSubject().logout();
                    model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, "JIRA登录失败，密码或用户名错误");
                }
            }
        }
    }

    /*@RequestMapping(value = "menu")
    public String menu(Model model) {
        User user = UserUtils.getUser();
        if (null != user) {
            SessionUtils.setUserName(user.getName());
//            int count = 0;
            //获取该账号下配置的所有工号类型
            List<Dict> list = dscWorkCodeService.findRoleTypesByUserId(user.getId());
//            if (null != list && list.size() > 0)
//            {
//                if (list.size() > 1)
//                {
//                    count = 3;
//                } else
//                {
//                    count = Integer.parseInt(list.get(0));
//                }
//            }
            model.addAttribute("roleTypeList", list);
        }
        return "ucp/menu";
    }*/

    @RequestMapping("menuZtree")
    @ResponseBody
    public ResponseMessage findWorkCodes(String roleType) throws Exception {
        SessionUtils.setRoleType(roleType);
        List<DscWorkCode> workCodes = UserUtils.getWorkCodes(roleType);
        /*for (DscWorkCode dscWorkCode : workCodes)
        {d
            List<String> roleNames = dscWorkCodeService.findRoleNamesByCode(dscWorkCode.getId());
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : roleNames)
            {
                stringBuffer.append(str).append(",");
            }
            if (stringBuffer.toString().length() > 0)
            {
                dscWorkCode.setRoleName(stringBuffer.substring(0, stringBuffer.lastIndexOf(",")));
            } else
            {
                dscWorkCode.setRoleName("");
            }

        }*/
        return ResponseMessage.success(workCodes);
    }

    @RequestMapping("menus")
    @ResponseBody
    public ResponseMessage menus(HttpServletRequest request, @RequestParam("workCode") String workCode,@RequestParam("roleType") String roleType,@RequestParam("roleName") String roleName) throws Exception
    {
        SessionUtils.setIsClick(true);
        List<Long> ids = new ArrayList<Long>();
        List<String> issueTypesName = new ArrayList<String>();
        List<String> projectIds = new ArrayList<String>();
        List<String> projectKeys = new ArrayList<String>();
        List<Menu> issueTypes = new ArrayList<Menu>();
//        List<Menu> parentMenus = menuService.findByWorkCode(workCode, UserUtils.getUser().getId());
//        List<String> strs = dscWorkCodeService.findRoleAnnounceByCodeRoleType(workCode,roleType,roleName);
        List<Menu> parentMenus = menuService.findMenus();
        List<String> strs = Lists.newArrayList();
        if(strs.size()>0){
            SessionUtils.setAnnounce(strs.get(0));
        }
        if (null == parentMenus || parentMenus.isEmpty())
        {
            return ResponseMessage.error("工号无业务线授权信息");
        }
        for (Menu str : parentMenus)
        {
            List<Menu> menus = menuService.findMenusByParentId(str.getId(), 2);
            List<Menu> menuList = new ArrayList<Menu>();
            for (Menu m : menus)
            {
                if (StringUtils.isBlank(m.getTarget()))
                {
                    continue;
                }
                long issueTypeId = Long.parseLong(m.getTarget());
                if (ids.contains(issueTypeId))
                {
                    continue;
                }
                issueTypes.add(m);
                ids.add(issueTypeId);
                issueTypesName.add(m.getName());
                menuList.add(m);
            }
            projectIds.add(str.getId());
            projectKeys.add(str.getHref());
            str.setMenus(menuList);
        }

        Collections.sort(issueTypes, new Comparator<Menu>()
        {
            @Override
            public int compare(Menu o1, Menu o2)
            {
                if (o1.getSort() > o2.getSort())
                {
                    return 1;
                } else if (o2.getSort() > o1.getSort())
                {
                    return -1;
                } else
                {
                    return 0;
                }

            }
        });
        for(int i=0;i<parentMenus.size();i++){
            if("MESSAGE".equals(parentMenus.get(i).getHref())){
                parentMenus.remove(i);
                projectKeys.remove(i);
                projectIds.remove(i);
                SessionUtils.setMsgPermission("true");
                break;
            }
        }
        SessionUtils.setIssueTypes(issueTypes);
        SessionUtils.setMenus(parentMenus);
        SessionUtils.setMenuIds(ids);
        SessionUtils.setPhoneAgent(workCode);
        SessionUtils.setProjectIds(projectIds);
        SessionUtils.setProjectKeys(projectKeys);
        User user = UserUtils.getUser();
        if (null != user)
        {
            DscUserJira dscUserJira = dscUserJiraService.get(user.getId());
            SessionUtils.setJiraUserName(dscUserJira.getLoginName());
            SessionUtils.setJiraPassword(dscUserJira.getPassword());
            SessionUtils.setUserName(user.getName());
            SessionUtils.setUserLoginName(user.getLoginName());
            try
            {
                getClient().getSessionClient().getCurrentSession().claim();
            } catch (RestClientException e)
            {
                String statusCode = e.getStatusCode().get().toString();
                if (StringUtils.equals("403", statusCode) || StringUtils.equals("401", statusCode))
                {
                    UserUtils.getSubject().logout();
                    return ResponseMessage.error("JIRA登录失败，密码或用户名错误");
                }
            }
        }
        return ResponseMessage.success();
    }

    @RequestMapping("index")
    public String index() throws Exception {
        return "ucp/index";
    }

    @RequestMapping("logout")
    public String logout(Model model) throws Exception {
        UserUtils.getSubject().logout();
        model.addAttribute("ssoUrl", ReadConfig.getSsoValue("sso_url"));
        return "ucp/logout";
    }

    @RequestMapping("top")
    public String top(Model model) throws Exception {
        //信息查询 如果没有配置值  则隐藏
//        List<DscLinkFunction> dscLinkFunction = dscLinkFunctionService.findListByMenuId(SessionUtils.getProjectIds());
//        if (dscLinkFunction.size() > 0) {
//            mav.addObject("searchInfo", "1");
//        }
        User currentUser = UserUtils.getUser();
        if (currentUser.getRoleList().size() > 0) {
            model.addAttribute("announce", SessionUtils.getAnnounce());
        }
        return "include/top";
    }

    @RequestMapping("footer")
    public String footer() throws Exception {
        return "include/footer";
    }

    @RequestMapping("caseForm")
    public String caseForm(Model model) throws Exception {
        String hotLine = SessionUtils.getHotLine();
        if (StringUtils.isNotBlank(hotLine)) {
            List<String> strs = new ArrayList<>();
            List<Menu> parents = menuService.findByWorkCode(SessionUtils.getPhoneAgent(), UserUtils.getUser().getId());
            for (Menu m : parents) {
                strs.add(m.getName());
            }
            List<Menu> parentMenus = new ArrayList<>();
            List<Menu> childrenMenus = new ArrayList<>();
            List<Menu> menus = menuService.findMenusByHotLine(hotLine);
            for (Menu m : menus) {
                if (StringUtils.isNotBlank(m.getParentIds())) {
                    childrenMenus.add(m);
                } else if (strs.contains(m.getName())) {
                    parentMenus.add(m);
                }
            }
            for (Menu m : parentMenus) {
                m.setMenus(childrenMenus);
            }
            model.addAttribute("issueTypes", parentMenus);
        } else {
            model.addAttribute("issueTypes", SessionUtils.getMenus());
        }
        return "ucp/case";
    }

    @RequestMapping("ucp/activityCase")
    public String activityCase() throws Exception {
        return "ucp/activity/activityCase";
    }

    /**
     * 根据项目key获取
     *
     * @param projectKey
     * @return
     * @throws Exception
     */
    @RequestMapping("findIssueTypesByProject")
    @ResponseBody
    public ResponseMessage findIssueTypesByProject(String projectKey) throws Exception {
        List<Menu> select = new ArrayList<Menu>();
        if (StringUtils.isNoneBlank(projectKey)) {
            select = menuService.findMenusByParentId(projectKey, 2);
        } else {
            select = SessionUtils.getIssueTypes();
        }
        return ResponseMessage.success(select);
    }

    /**
     * 根据项目key,issueType过滤status
     *
     * @param projectKey
     * @param issueTypeId
     * @param request
     * @return
     */
    @RequestMapping("findStatusByProject")
    @ResponseBody
    public ResponseMessage findStatusByProject(String projectKey, String issueTypeId, HttpServletRequest request) {
        String strs = "";
        StringBuffer parentIds = new StringBuffer();
        if (StringUtils.isNotBlank(projectKey)) {
            parentIds.append(projectKey).append(",");
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
        return ResponseMessage.success(commonSelectLists);
    }

    @RequestMapping("getDescription")
    @ResponseBody
    public String getDescription(String id, String parentId) {
        if (StringUtils.isNotEmpty(id)) {
            if (StringUtils.isNotEmpty(dictService.findByValue(id, parentId))) {
                return dictService.findByValue(id, parentId);
            }
        }
        return null;
    }

}
