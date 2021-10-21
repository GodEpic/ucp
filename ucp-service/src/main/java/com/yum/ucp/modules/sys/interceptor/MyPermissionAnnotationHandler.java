package com.yum.ucp.modules.sys.interceptor;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * Created by Administrator on 2016/10/17.
 */
    public class MyPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

    public MyPermissionAnnotationHandler() {
        super(RequiresPermissions.class);
    }

    protected String[] getAnnotationValue(Annotation a) {
        RequiresPermissions rpAnnotation = (RequiresPermissions) a;
        return rpAnnotation.value();
    }

    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        RequiresPermissions methodAnnotation = mi.getMethod().getAnnotation(RequiresPermissions.class);
        String[] methodPerms = methodAnnotation.value();
        Subject subject = getSubject();
        if (methodPerms.length == 1)  {
                RequiresPermissions classAnnotation = mi.getThis().getClass().getAnnotation(RequiresPermissions.class);
        if (null != classAnnotation) {
            String[] classPerms = classAnnotation.value();
            subject.checkPermission(classPerms[0] + methodPerms[0]);
        } else {
            subject.checkPermission(methodPerms[0]);
        }
        return;
        }
        if (Logical.AND.equals(methodAnnotation.logical())) {
            getSubject().checkPermissions(methodPerms);
            return;
        }
        if (Logical.OR.equals(methodAnnotation.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : methodPerms)
                if (getSubject().isPermitted(permission)) hasAtLeastOnePermission = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOnePermission) getSubject().checkPermission(methodPerms[0]);

        }
    }

    @Override
    public void assertAuthorized(Annotation annotation) throws AuthorizationException {

    }
}