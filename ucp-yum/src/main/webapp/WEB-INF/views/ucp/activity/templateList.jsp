<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html lang="en">
<head lang="en">
    <title>YUM</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>

<div class="tab-con deal_l2">
    <div class="sec">
        <%--<article class="sec-title t3">搜索</article>--%>
        <div class="sec-form">
            <form:form id="searchForm" modelAttribute="template" action="${ctx}/template/" method="post"
                       class="form-search">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>查询关键字：</label>
                        <input type="text" name="searchKey" value="${searchKey}">
                    </li>
                    <li>
                    </li>
                </div>
                <input type="submit" id="search" onclick="return page();" value="">
            </form:form>
        </div>
    </div>
    <div id="messageBox" class="alert alert-${alertClass} ${empty message ? 'hide' : ''}">
        <button data-dismiss="alert" class="close">×</button>
        <label>${message}</label>
    </div>
    <div class="sec no-border">
        <div class="div-head">
            <div class="div-records">模板列表</div>
            <div class="div-create">
                <a class="layui-yum-btn" type="button" target="_blank" href="${ctx}/template/form">创建新模板</a>
            </div>
        </div>
        <div class="sec-tbl sec-tbl-width">
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>模板名称</th>
                    <th>品牌</th>
                    <th>模板文件</th>
                    <th>创建经办人</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="t" varStatus="status">
                    <tr>
                        <td> ${ status.index + 1}</td>
                        <td title="${t.templateName}">${t.templateName}</td>
                        <td>${fns:getDictLabel(t.brand, 'brand', '')}</td>
                        <td><a href="${t.filePath}" title="${t.fileName}">${t.fileName}</a><br></td>
                        <td>${fns:getUserById(t.creator).name}</td>
                        <td><fmt:formatDate value="${t.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <a target="_blank" href="${ctx}/template/detail?id=${t.id}">查看</a>&nbsp;
                            <a target="_blank" href="${ctx}/template/form?id=${t.id}">编辑</a>&nbsp;
                            <a href="javascript:void(0)" onclick="delTemplate('${t.id}')">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">${page}</div>
            <input type="text" class="sort" value="${sort}" style="display: none">
            <input type="text" class="sortBy" value="${sortBy}" style="display: none">
        </div>
    </div>
</div>
<%--<%@include file="/WEB-INF/views/include/footer.jsp" %>--%>

</body>
</html>
<script type="text/javascript">

    $(window.parent.document).find("#deal_case").height($(".tab-con").height() + 50);

    var ctx = '${ctx}';
    $(".form_datetime").datepicker({format: 'yyyy-mm-dd'});
    var lis = $('nav li');
    lis.removeClass();
    $("#deal_case").attr("class", "current");

    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/template/list");
        $("#searchForm").submit();
        return false;
    }

    function sortPage(n) {
        $(".sortBy").val(n);
        if ($(".sort").val() == "1") {
            $(".sort").val("2");
        } else {
            $(".sort").val("1");
        }
        page();
    }

    $("input[name='releaseStatusSelect']").click(function () {
        $("input[name='releaseStatus']").val($(this).val());
        page();
    });

    function delTemplate(templateId) {
        parent.layer.confirm('确认要删除该活动吗？', function (index) {
            window.location.href = "${ctx}/template/delete?id=" + templateId;
            parent.layer.close(index);
        });
    }
</script>
<script src="${ctxStatic}/ucp/task.js?v=2019090413"></script>
