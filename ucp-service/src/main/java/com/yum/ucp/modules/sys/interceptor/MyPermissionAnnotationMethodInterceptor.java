package com.yum.ucp.modules.sys.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;

/**
 * Created by Administrator on 2016/10/17.
 */
public class MyPermissionAnnotationMethodInterceptor  extends AuthorizingAnnotationMethodInterceptor {    public MyPermissionAnnotationMethodInterceptor() {        super(new PermissionAnnotationHandler());    }    public MyPermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
    super(new MyPermissionAnnotationHandler(), resolver);    }
    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {            AuthorizingAnnotationHandler handler = (AuthorizingAnnotationHandler) getHandler();
            if(handler instanceof MyPermissionAnnotationHandler) {
                ((MyPermissionAnnotationHandler) handler).assertAuthorized(mi);
            } else {                handler.assertAuthorized(getAnnotation(mi));
            }        }        catch(AuthorizationException ae) {    throw ae;
        }
    }
}