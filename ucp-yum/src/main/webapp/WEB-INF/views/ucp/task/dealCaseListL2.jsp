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
            <form:form id="searchForm" modelAttribute="ucpTaskVO" action="${ctx}/task/ucpTask/" method="post" class="form-search">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>活动名：</label>
                        <input type="text" name="activityName" value="${activityName}">
                    </li>
                </div>
                <input type="submit" id="search" onclick="return page();" value="">
            </form:form>
        </div>
    </div>

    <div class="sec no-border">
        <div class="sec-tbl">
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>优先级</th>
                    <th>任务发送时间</th>
                    <th>活动名</th>
                    <th>任务名</th>
                    <th>券数量</th>
                    <th>检查情况</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr>
                        <td> ${ status.index + 1}</td>
                        <td>${fns:getDictLabel(issue.priority, 'priority', '')}</td>
                        <td><fmt:formatDate value="${issue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>${issue.activityName}</td>
                        <td>${issue.activityName}-${issue.version}</td>
                        <td>${issue.couponCount}</td>
                        <td>${fns:getDictLabel(issue.verifyStatus, 'verify_status', '')}</td>
                        <td>
                            <a target="_blank" href="${ctx}/task/ucpTask/form?id=${issue.id}">处理</a>&nbsp;
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">${page}</div>
        </div>
    </div>
</div>

</body>
</html>
<script>
    $(".form_datetime").datepicker({format: 'yyyy-mm-dd'});
    var lis = $('nav li');
    lis.removeClass();
    $("#deal_case").attr("class", "current");
    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/task/ucpTask/dealListL2?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
        $("#searchForm").submit();
        return false;
    }
    setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/task/ucpTask/list?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);
</script>

