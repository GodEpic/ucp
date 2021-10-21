]
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>功能管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/func/dscLinkFunction">功能管理</a></li>
        <li><a href="${ctx}/func/dscLinkFunction/form">功能管理添加</a></li>
</ul>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <tr>
        <th>名称</th>
        <th>编码</th>
        <th>类型</th>
        <th> 外链地址 </th>
        <th> 外链参数</th>
        <th>外链图标</th>
        <th> 知识库参数 </th>
            <th>操作</th>
     </tr>
    <c:forEach items="${page.list}" var="funcLink">
        <tr>
            <td><a href="${ctx}/func/dscLinkFunction/form?id=${funcLink.id}">${funcLink.funTitle}</a></td>
            <td><a href="${ctx}/func/dscLinkFunction/form?id=${funcLink.id}">${funcLink.funCode}</a></td>
            <td><c:if test="${funcLink.funType==0}">接口模块</c:if>
                <c:if test="${funcLink.funType==1}">外链系统 </c:if>
                <c:if test="${funcLink.funType==2}"> 知识库</c:if></td>
            <td> ${funcLink.linkUrl} </td>
            <td> ${funcLink.linkParams} </td>
            <td> ${funcLink.linkImg} </td>
            <td> ${funcLink.repoParams} </td>
            <td><a href="${ctx}/func/dscLinkFunction/form?id=${funcLink.id}">修改</a>
            <a href="${ctx}/func/dscLinkFunction/delete?id=${funcLink.id}" onclick="return confirm('确认要删除该外连吗？', this.href)">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>
<div class="pagination">${page}</div>
</body>
</html>