<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/export/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/export/list">导出配置列表</a></li>
		<li><a href="${ctx}/export/form">导出配置添加</a></li>
	</ul>
	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="exportConfig" action="${ctx}/export/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label style="width: auto">导出配置名称：</label><form:input path="name" htmlEscape="false" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column login_name">导出配置名称</th><th class="sort-column name">创建时间</th><th class="sort-column name">更新时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="u">
			<tr>
				<td><a href="${ctx}/export/form?id=${u.id}">${u.name}</a></td>
				<td><fmt:formatDate value="${u.createDate}" type="both" dateStyle="full"/></td>
				<td><fmt:formatDate value="${u.updateDate}" type="both" dateStyle="full"/></td>
				 <td>
					 <a href="${ctx}/export/form?id=${u.id}">查看</a>
					 <a href="${ctx}/export/delete?id=${u.id}">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>