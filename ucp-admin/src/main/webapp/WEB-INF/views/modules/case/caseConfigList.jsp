]
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>case管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/case/dscCaseConfig">case管理列表</a></li>
        <li><a href="${ctx}/case/dscCaseConfig/form">case管理添加</a></li>
</ul>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <tr>
        <th>case编码</th>
        <th>备注</th>
        <th>查看地址</th>
        <th>L1处理地址</th>
        <th>L2处理地址</th>
        <th>状态</th>
            <th>操作</th>
     </tr>
    <c:forEach items="${page.list}" var="config">
        <tr>
            <td><a href="${ctx}/case/dscCaseConfig/form?id=${config.id}">${config.caseCode}</a></td>
            <td> ${config.remarks} </td>
            <td> ${config.viewPage} </td>
            <td> ${config.lv1Page} </td>
            <td> ${config.lv2Page} </td>
            <td><c:if test="${(config.caseType eq '0')}">紧急</c:if><c:if test="${(config.caseType eq '1')}">非紧急</c:if></td>
            <td><a href="${ctx}/case/dscCaseConfig/form?id=${config.id}">修改</a>
            <a href="${ctx}/case/dscCaseConfig/delete?id=${config.id}" onclick="return confirm('确认要删除该配置吗？', this.href)">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>