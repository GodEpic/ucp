package com.yum.ucp.modules.sys.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInterceptor;
import org.apache.shiro.authz.aop.*;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class MyAop extends AnnotationsAuthorizingMethodInterceptor implements MethodInterceptor {

    public MyAop() {
        List interceptors =
                new ArrayList(5);

        //use a Spring-specific Annotation resolver - Spring's AnnotationUtils is nicer than the
        //raw JDK resolution process.
        AnnotationResolver resolver = new SpringAnnotationResolver();
        //we can re-use the same resolver instance - it does not retain state:
        interceptors.add(new RoleAnnotationMethodInterceptor(resolver));
        interceptors.add(new MyPermissionAnnotationMethodInterceptor(resolver));
        interceptors.add(new AuthenticatedAnnotationMethodInterceptor(resolver));
        interceptors.add(new UserAnnotationMethodInterceptor(resolver));
        interceptors.add(new GuestAnnotationMethodInterceptor(resolver));

        setMethodInterceptors(interceptors);
    }
}