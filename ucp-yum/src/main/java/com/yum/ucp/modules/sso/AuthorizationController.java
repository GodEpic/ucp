package com.yum.ucp.modules.sso;

import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.secure.oauth.OAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
@RequestMapping(value = "${adminPath}/authorization")
public class AuthorizationController extends DscBaseController {

	@RequestMapping("index")
	public void index(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String requestContextPath=request.getRequestURL().toString();
		String contextPath=request.getContextPath();
		requestContextPath=requestContextPath.substring(0, requestContextPath.indexOf(contextPath)+contextPath.length());
		 System.out.println(requestContextPath);

		String callback =getCallback();
		OAuthService service =new  OAuthService(getClientId(),getClientSecret(),callback);
		System.out.println("Authorization Url :"+service.getAuthorizationUrl());
		response.sendRedirect(service.getAuthorizationUrl());
	}
}

