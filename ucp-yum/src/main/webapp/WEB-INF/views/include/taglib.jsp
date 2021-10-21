<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fnc" uri="/WEB-INF/tlds/fnc.tld" %>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags/sys" %>
<%@ taglib prefix="act" tagdir="/WEB-INF/tags/act" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}/"/>
<c:set var="web" value="/WEB-INF/views/ucp/"/>
<c:set var="ctxStatic" value="${static.resource.url}"/>
<c:set var="userName" value="${sessionScope.userName}"/>
<c:set var="jiraName" value="${sessionScope.jiraUserName}"/>
<c:set var="groupId" value="${sessionScope.groupId}"/>
<c:set var="roleType" value="${sessionScope.roleType}"/>
<c:set var="agent" value="${sessionScope.phoneAgent}"/>
<c:set var="pass" value="${sessionScope.phonePass}"/>
<c:set var="exten" value="${sessionScope.phoneExten}"/>
<c:set var="phoneUrl" value="${sessionScope.phoneUrl}"/>
<c:set var="phoneUrl2" value="${sessionScope.phoneUrl2}"/>
<c:set var="currUserId" value="${fns:getUser().id}"/>


