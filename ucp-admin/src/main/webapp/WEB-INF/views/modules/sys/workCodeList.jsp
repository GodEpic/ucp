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
		<li class="active"><a href="${ctx}/sys/user/workCodeList?id=${user.id}">工号列表</a></li>
		<c:if test="${page.count < 1}"><li><a href="${ctx}/sys/user/workCodeForm?id=${user.id}">工号新增</a></li></c:if>
	</ul>
	<form:form id="displayForm" modelAttribute="user" action="${ctx}/sys/user/workCodeList?id=${user.id}" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li>
				<label>登录名：</label>
				<label  htmlEscape="false" class="input-medium">${u.loginName}</label>
			</li>
			<li class="clearfix"></li>
			<li>
				<label>姓&nbsp;&nbsp;&nbsp;名：</label>
				<label htmlEscape="false" class="input-medium">${u.name}</label>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>工号</th>
				<th>类型</th>
				<th>操作</th>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workCode">
			<tr>
				<td>${workCode.workCode}</td>
				<%--<td><c:if test="${(workCode.roleType eq '0')}">管理角色</c:if><c:if test="${(workCode.roleType eq '1')}">lv1</c:if><c:if test="${(workCode.roleType eq '2')}">lv2</c:if></td>--%>
				<td>${fns:getDictLabel(workCode.roleType, 'role_type', '')}</td>
				<td>
					<a href="${ctx}/sys/user/workCodeEdit?id=${workCode.id}&userId=${workCode.user.id}">编辑</a>
					<a href="${ctx}/sys/user/deleteWorkCode?id=${workCode.id}" onclick="return confirm('确认要删除该工号吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>