package com.yum.ucp.modules.sys.filter;

import com.yum.ucp.modules.sys.security.SystemAuthorizingRealm;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.secure.client.model.UserInfo;
import com.yum.secure.exceptions.OAuthApiException;
import com.yum.secure.model.Token;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 2014/8/17.
 *
 * @author 罗海峰
 * @since 0.1.0
 */
public class LoginFilter implements Filter {
    ApplicationContext appContext;
    ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
        appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        String path = hsr.getRequestURI();
        
        //静态文件不需要进行处理
        String[] suffixes = {".css", ".js", "$", ".jpg", ".gif", ".png", ".ttf", ".woff", ".ico", ".xls"};
        for (String suffix : suffixes) {
            if (path.contains(suffix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (path.contains("/authorization/index") || path.contains("/ucp/sso/callBack") || path.contains("/authorization/index") || path.contains("/logout")
              ) {
            //不拦截
            chain.doFilter(request, response);
            return;
        }
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        if (null != principal) {
            Token token = principal.getCode();
            try {
                UserInfo user_ = new UserInfo(token);
                UserInfo user = user_.requestUserInfo();
                if(null==user){
                    UserUtils.getSubject().logout();
                    ((HttpServletResponse) response).sendRedirect("/ucp/authorization/index");
                    return;
                }
            } catch (OAuthApiException e) {
                UserUtils.getSubject().logout();
                ((HttpServletResponse) response).sendRedirect("/ucp/authorization/index");
                return;
            }
        }
        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
    }
}
