<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "${not empty dict.id ? dict.id : '0'}";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
                            isSelect: getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, row.isSelect)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/dict/list?id=${dict.id}&parentIds=${dict.parentIds}">字典列表</a></li>
		<%--<shiro:hasPermission name="sys:dict:edit">--%>
			<li><a href="${ctx}/sys/dict/form?parent.id=${dict.id}">字典添加</a></li>
		<%--</shiro:hasPermission>--%>
	</ul>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>字典名称</th><th>值</th><th>排序</th><th>描述</th><th>是否默认</th><%--<shiro:hasPermission name="sys:dict:edit">--%><th>操作</th><%--</shiro:hasPermission>--%></tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/dict/form?id={{row.id}}">{{row.label}}</a></td>
			<td>{{row.value}}</td>
			<td>{{row.sort}}</td>
			<td>{{row.description}}</td>
			<td>{{dict.isSelect}}</td>
			<%--<shiro:hasPermission name="sys:dict:edit">--%>
			<td>
				<a href="${ctx}/sys/dict/form?id={{row.id}}">修改</a>
				<a href="${ctx}/sys/dict/delete?id={{row.id}}" onclick="return confirmx('要删除该字典及所有子项吗？', this.href)">删除</a>
				<a href="${ctx}/sys/dict/form?parent.id={{row.id}}">添加下级字典</a>
			</td>
			<%--</shiro:hasPermission>--%>
		</tr>
	</script>
</body>
</html>