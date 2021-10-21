]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/role/">角色列表</a></li>
		　<li><a href="${ctx}/sys/role/form">角色添加</a></li>　
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>角色名称</th><th>角色类型</th>　<th>操作</th>　</tr>
		<c:forEach items="${page.list}" var="role">
			<tr>
				<td><a href="${ctx}/sys/role/form?id=${role.id}">${role.name}</a></td>
				<td><c:if test="${role.roleType==1}">Level1</c:if><c:if test="${role.roleType==2}">Level2</c:if></td>
			　<td>
					<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
						<a href="${ctx}/sys/role/form?id=${role.id}">修改</a>
					</c:if>
					<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)">删除</a>
				</td>　
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>