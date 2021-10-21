<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/user/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/sys/user/list">用户列表</a></li>
	<li><a href="${ctx}/sys/user/form">用户添加</a></li>
</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>姓&nbsp;&nbsp;&nbsp;名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>归属部门</th><th class="sort-column login_name">登录名</th><th class="sort-column name">姓名</th><%--<th>角色</th> --%><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="u">
			<tr>
				<td>${u.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${u.id}">${u.loginName}</a></td>
				<td>${u.name}</td>
					<%--<td>${user.phone}</td>
                    <td>${user.mobile}</td>
                    <td>${user.roleNames}</td> --%>
				 <td>
    				<a href="${ctx}/sys/user/form?id=${u.id}">查看</a>
					 <a href="${ctx}/sys/user/jira?id=${u.id}">Jira账号</a>
					 <a href="${ctx}/sys/user/workCodeList?id=${u.id}">工号列表</a>
					 <a href="${ctx}/sys/user/delete?id=${u.id}">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>