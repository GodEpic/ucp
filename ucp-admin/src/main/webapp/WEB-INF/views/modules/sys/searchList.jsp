]
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>信息查询管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/menu/findParentMenus">流程列表</a></li>
    <li><a href="${ctx}/sys/menu/form">流程添加</a></li>
</ul>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <tr>
        <th>业务线</th>
        <th>操作</th>
        　
    </tr>
    <c:forEach items="${list}" var="menu">
        <tr>
            <td><a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></td>
            　
            <td>
                <a href="${ctx}/sys/menu/form?id=${menu.id}">配置</a>
            </td>
            　
        </tr>
    </c:forEach>
</table>
</body>
</html>