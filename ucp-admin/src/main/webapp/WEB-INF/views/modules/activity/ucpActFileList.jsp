<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>特殊活动附件关联管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/activity/ucpActFile/">特殊活动附件关联列表</a></li>
		<shiro:hasPermission name="activity:ucpActFile:edit"><li><a href="${ctx}/activity/ucpActFile/form">特殊活动附件关联添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ucpActFile" action="${ctx}/activity/ucpActFile/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>备注信息</th>
				<th>更新时间</th>
				<shiro:hasPermission name="activity:ucpActFile:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ucpActFile">
			<tr>
				<td><a href="${ctx}/activity/ucpActFile/form?id=${ucpActFile.id}">
					${ucpActFile.remarks}
				</a></td>
				<td>
					<fmt:formatDate value="${ucpActFile.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="activity:ucpActFile:edit"><td>
    				<a href="${ctx}/activity/ucpActFile/form?id=${ucpActFile.id}">修改</a>
					<a href="${ctx}/activity/ucpActFile/delete?id=${ucpActFile.id}" onclick="return confirmx('确认要删除该特殊活动附件关联吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>