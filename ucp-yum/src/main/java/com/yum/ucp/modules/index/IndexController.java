package com.yum.ucp.modules.index;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.google.common.collect.Lists;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.security.shiro.session.SessionDAO;
import com.yum.ucp.common.utils.CookieUtils;
import com.yum.ucp.modules.common.entity.CommonSelect;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.func.service.DscLinkFunctionService;
import com.yum.ucp.modules.sys.entity.*;
import com.yum.ucp.modules.sys.security.FormAuthenticationFilter;
import com.yum.ucp.modules.sys.security.SystemAuthorizingRealm;
import com.yum.ucp.modules.sys.security.UsernamePasswordToken;
import com.yum.ucp.modules.sys.service.DictService;
import com.yum.ucp.modules.sys.service.DscHotLineConfigService;
import com.yum.ucp.modules.sys.service.DscUserJiraService;
import com.yum.ucp.modules.sys.service.YumUserService;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.sys.utils.SessionUtils;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.task.service.DscExportTaskService;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import com.yum.ucp.modules.work.service.DscWorkCodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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
    @Autowired
    private YumUserService yumUserService;
    @Autowired
    private SystemAuthorizingRealm systemRealm;

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
        if (principal != null && StringUtils.isEmpty(principal.getLoginType())) {
            setWorkCodeInfo(model);
            if (model.containsAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM)) {
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
            if (model.containsAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM)) {
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
            logger.info("login fail, active session size: {}, message: {}, exception: {}", sessionDAO.getActiveSessions(false).size(), message, exception);
        }
        return "ucp/login";
    }

    @RequestMapping(value = "homeTest2", method = RequestMethod.GET)
    public String login3(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params, Model model) {
        HttpURLConnection conn = null;
        try {
            String authorizationUrl = "https://ssotest.hwwt2.com/openapi/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=all&oauth_timestamp=%s";
            authorizationUrl = String.format(authorizationUrl, "1282", "http://172.25.221.188:8089/ucp/home", System.currentTimeMillis());
            URL realUrl = new URL(authorizationUrl);
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.setInstanceFollowRedirects(false);
            //设置请求头
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");//设置参数类型是json格式
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("logType", "base");
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                //BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                //StringBuffer buffer = new StringBuffer();
                //String line = "";
                //while ((line = in.readLine()) != null) {
                //    buffer.append(line);
                //}
                //String result = buffer.toString();
                ////subscriber是观察者，在本代码中可以理解成发送数据给activity
                //subscriber.onNext(result);
            }
        } catch (Exception e) {
        }
        return null;
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
            if (null != workCodes && workCodes.size() > 0) {
                DscWorkCode dscWorkCode = workCodes.get(0);
                SessionUtils.setPhoneAgent(dscWorkCode.getWorkCode());
            }
            DscUserJira dscUserJira = dscUserJiraService.get(user.getId());
            if (dscUserJira == null) {
                model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, "JIRA登录账号未配置，请联系管理员");
                return;
            }
            SessionUtils.setJiraUserName(dscUserJira.getLoginName());
            SessionUtils.setJiraPassword(dscUserJira.getPassword());
            SessionUtils.setUserName(user.getName());
            try {
                getClient().getSessionClient().getCurrentSession().claim();
            } catch (RestClientException e) {
                e.printStackTrace();
                String statusCode = e.getStatusCode().get().toString();
                if (StringUtils.equals("403", statusCode) || StringUtils.equals("401", statusCode)) {
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
    public ResponseMessage menus(HttpServletRequest request, @RequestParam("workCode") String workCode, @RequestParam("roleType") String roleType, @RequestParam("roleName") String roleName) throws Exception {
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
        if (strs.size() > 0) {
            SessionUtils.setAnnounce(strs.get(0));
        }
        if (null == parentMenus || parentMenus.isEmpty()) {
            return ResponseMessage.error("工号无业务线授权信息");
        }
        for (Menu str : parentMenus) {
            List<Menu> menus = menuService.findMenusByParentId(str.getId(), 2);
            List<Menu> menuList = new ArrayList<Menu>();
            for (Menu m : menus) {
                if (StringUtils.isBlank(m.getTarget())) {
                    continue;
                }
                long issueTypeId = Long.parseLong(m.getTarget());
                if (ids.contains(issueTypeId)) {
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

        Collections.sort(issueTypes, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                if (o1.getSort() > o2.getSort()) {
                    return 1;
                } else if (o2.getSort() > o1.getSort()) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        for (int i = 0; i < parentMenus.size(); i++) {
            if ("MESSAGE".equals(parentMenus.get(i).getHref())) {
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
        if (null != user) {
            DscUserJira dscUserJira = dscUserJiraService.get(user.getId());
            SessionUtils.setJiraUserName(dscUserJira.getLoginName());
            SessionUtils.setJiraPassword(dscUserJira.getPassword());
            SessionUtils.setUserName(user.getName());
            SessionUtils.setUserLoginName(user.getLoginName());
            try {
                getClient().getSessionClient().getCurrentSession().claim();
            } catch (RestClientException e) {
                String statusCode = e.getStatusCode().get().toString();
                if (StringUtils.equals("403", statusCode) || StringUtils.equals("401", statusCode)) {
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


    @RequestMapping(value = "ssoIndex", method = RequestMethod.GET)
    public String ssoIndex(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        String ticket = null != params.get("ticket") ? params.get("ticket").toString() : "";//获取tick
        if (StringUtils.isNotBlank(ticket)) {
            try {
                String tickUrl = "http://172.25.216.207/ticket/" + ticket;//ticket访问地址
                String appId = "100000076";//头部参数-appid
                String signKey = "29095D3C7B8BE38F7C9E5E22D0FA13BE46B86613";//头部参数-签名
                Map<String, String> headers = new HashMap<>();
                long timestamp = (int) (System.currentTimeMillis() / 1000);
                String rawSignStr = String.format("{ticket=%s&timestamp=%s}%s", ticket, timestamp, signKey);
                headers.put("appId", appId);
                headers.put("timestamp", String.valueOf(timestamp));
                headers.put("sign", DigestUtils.md5DigestAsHex(rawSignStr.getBytes()));
                String resultTick = HttpRequest.post(tickUrl).headerMap(headers, false).timeout(20000)//超时，毫秒
                        .execute().body();
                logger.info("询问票据Url：" + tickUrl + "---询问票据头部信息:" + JSONUtil.toJsonStr(headers) + "---询问票据返回结果:" + resultTick);
                JSONObject ticketResultJson = JSONUtil.parseObj(resultTick);
                if (null != ticketResultJson.getStr("code") && ticketResultJson.getStr("code").equals("20000") && null != ticketResultJson.get("data")) {
                    JSONObject ticketResultJson_Data = JSONUtil.parseObj(ticketResultJson.getStr("data"));
                    SessionUtils.setNotifyId(ticketResultJson_Data.getStr("name"));
                    SessionUtils.setUserLoginName(ticketResultJson_Data.getStr("psid"));
                    SessionUtils.setUserName(ticketResultJson_Data.getStr("psid"));
                    logger.info("打印相关的票据信息:" + ticketResultJson_Data.getStr("psid") + "---打印相关的名称:" + ticketResultJson_Data.getStr("name"));
                } else {
                    SessionUtils.setNotifyId(null);
                    SessionUtils.setUserLoginName(null);
                    SessionUtils.setUserName(null);
                }
                //开始访问authorize接口获取token
                String authorizationUrl = "https://ssotest.hwwt2.com/openapi/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=all&oauth_timestamp=%s";
                authorizationUrl = String.format(authorizationUrl, Global.getConfig(Global.SSO_OAUTH_TOKEN_CLIENT_ID), Global.getConfig(Global.SSO_OAUTH_TOKEN_CLIENT_REDIRECT_URI), System.currentTimeMillis());
                request.setAttribute("ssoAuthorizeUrl", authorizationUrl);//页面传输相关地址
                logger.info("访问SSO地址authorize:" + authorizationUrl);
                request.getRequestDispatcher("/WEB-INF/views/ucp/ssoLogin.jsp").forward(request, response);//跳转到相关页面
            } catch (ServletException | IOException e) {
                e.printStackTrace();
                logger.debug("ssoIndex跳转方法发生异常", e);
                return "ucp/login";
            }
        }
        return "ucp/login";
    }


    @RequestMapping(value = "home", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        Subject subject = UserUtils.getSubject();
        String yumPsid = ssoLogin(request.getParameter("code"));
        //String yumPsid="102313040";//uat测试账号
        if (!yumPsid.equals(SessionUtils.getUserLoginName())) {
            SessionUtils.setUserLoginName(yumPsid);
            SessionUtils.setUserName(yumPsid);
            SessionUtils.setNotifyId(null);
        }
        YumUser user;
        if (principal != null && principal.getId().equals(yumPsid)) {
            user = yumUserService.get(principal.getId());
            setHomeWorkCodeInfo(user);
            return "redirect:" + adminPath + "/index";
        }
        logger.info("打印SessionUtils下的所有的数据---登录名：" + SessionUtils.getUserName() + "---登录名称" + SessionUtils.getUserLoginName());
        user = yumUserService.get(SessionUtils.getUserName());
        if (user == null) {
            return "ucp/login";
        }
        UsernamePasswordToken up = new UsernamePasswordToken(SessionUtils.getUserName(), "admin".toCharArray(), false, "", "", false);
        up.setLongType(user.getLoginType());
        try {
            subject.login(up);
            setHomeWorkCodeInfo(user);
            System.out.println("登录成功！");
            return "redirect:" + adminPath + "/index";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("登录失败！");
            return "ucp/login";
        }
    }

    public String ssoLogin(String code) {
        try {
            //访问token地址
            String tokenUrlGet = "https://ssotest.hwwt2.com/openapi/oauth/token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s&grant_type=%s&oauth_timestamp=%s";
            tokenUrlGet = String.format(tokenUrlGet, Global.getConfig(Global.SSO_OAUTH_TOKEN_CLIENT_ID), Global.getConfig(Global.SSO_OAUTH_TOKEN_CLIENT_SECRET), Global.getConfig(Global.SSO_OAUTH_TOKEN_CLIENT_REDIRECT_URI), code, "authorization_code", System.currentTimeMillis());
            logger.info("开始访问SSO地址token:" + tokenUrlGet);
            String tokenResult = HttpRequest.get(tokenUrlGet).timeout(20000)//超时，毫秒
                    .execute().body();//执行访问操作

            JSONObject tokenResultJson = JSONUtil.parseObj(tokenResult);
            logger.info("访问结束SSO地址token:" + tokenUrlGet + "---返回结果:" + tokenResult);
            String userInfoUrlGet = "https://ssotest.hwwt2.com/openapi/oauth/userinfo?access_token=%s";
            userInfoUrlGet = String.format(userInfoUrlGet, tokenResultJson.getStr("access_token"));
            logger.info("开始访问SSO地址userInfo:" + userInfoUrlGet);
            String userInfoResult = HttpRequest.get(userInfoUrlGet).timeout(20000)//超时，毫秒
                    .execute().body();
            JSONObject userInfoResultJson = JSONUtil.parseObj(userInfoResult);
            logger.info("访问结束SSO地址userinfo:" + userInfoUrlGet + "---返回结果:" + userInfoResult);
            return userInfoResultJson.getStr("yumPSID");
        } catch (Exception e) {
            return null;
        }
    }

    private void setHomeWorkCodeInfo(YumUser yumUser) {
        if (null != yumUser) {
            SessionUtils.setRoleType(yumUser.getRoleType());
            SessionUtils.setJiraUserName(yumUser.getJira());
            SessionUtils.setJiraPassword(yumUser.getJiraNo());
            SessionUtils.setUserName(yumUser.getUserName());
            SessionUtils.setLoginType(yumUser.getLoginType());
        }
    }
}
