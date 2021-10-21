package com.yum.ucp.modules.sso;

import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.security.SystemAuthorizingRealm;
import com.yum.ucp.modules.sys.service.SystemService;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.secure.client.model.UserInfo;
import com.yum.secure.exceptions.OAuthApiException;
import com.yum.secure.model.Token;
import com.yum.secure.oauth.OAuthService;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/sso")
public class CallbackController extends DscBaseController {
    @Autowired
    private SystemService systemService;
    @RequestMapping("/callBack")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OAuthService service = null;
        String callback = getCallback();
        service = new OAuthService(getClientId(), getClientSecret(), callback);
        String code = request.getParameter("code");
        Token accessToken = service.getAccessToken(code);
        try {
            UserInfo user_ = new UserInfo(accessToken);
            UserInfo user = user_.requestUserInfo();
            if (user != null) {
                User userInfo = systemService.getUserByLoginName(user.getYumPSID());
                if(null!=userInfo){
                    SystemAuthorizingRealm.Principal principal=new SystemAuthorizingRealm.Principal(userInfo,false,accessToken);
                    PrincipalCollection principals = new SimplePrincipalCollection(principal, "MobileRealm");
                    WebSubject.Builder builder = new WebSubject.Builder(request, response);
                    builder.principals(principals);
                    builder.authenticated(true);
                    WebSubject subject = builder.buildWebSubject();
                    ThreadContext.bind(subject);
                    return new ModelAndView("redirect:/menu");
                }else{
                    ModelAndView mav=  new ModelAndView("ucp/error");
                    mav.addObject("ssoUrl", ReadConfig.getSsoValue("sso_url"));
                    return mav;
                }
            }
        } catch (OAuthApiException e) {
            return new ModelAndView("redirect:/authorization/index");
        }
        return new ModelAndView("redirect:/authorization/index");
    }
}
