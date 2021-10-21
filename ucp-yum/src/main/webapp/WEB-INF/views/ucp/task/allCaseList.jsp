<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html lang="en">
<head lang="en">
    <title>YUM</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>

<div class="tab-con case-tab-top deal_l2">
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
                <input type="button" id="back" onclick="javascript:history.go(-1);" value="返回">
            </form:form>
        </div>
    </div>
    <div id="messageBox" class="alert alert-${alertClass} ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
        <label>${message}</label>
    </div>
    <div class="sec no-border">
        <div class="sec-tbl sec-tbl-width">
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>任务次数</th>
                    <th>子任务名</th>
                    <th>任务状态</th>
                    <th>L1经办人</th>
                    <th>L2经办人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr>
                        <td> ${ issue.version}</td>
                        <td title="${issue.activityName}-${issue.version}">${issue.activityName}-${issue.version}</td>
                        <td>${fns:getDictLabel(issue.jiraStatus, 'jira_status', '')}</td>
                        <td>${fns:getUserNameByWorkCode(issue.receiveL1)}</td>
                        <td>${fns:getUserNameByWorkCode(issue.receiveL2)}</td>
                        <td>
                            <%--<c:if test="${issue.configStatus eq CONFIG_STATUS_UNCLAIMED}">--%>
                                <a href="javascript:void(0)" onclick="delTask('${issue.id}')">删除</a>
                            <%--</c:if>--%>
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
<script>
    $(".form_datetime").datepicker({format: 'yyyy-mm-dd'});
    var lis = $('nav li');
    lis.removeClass();
    $("#deal_case").attr("class", "current");

    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/task/ucpTask/allList?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
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
    //setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/task/ucpTask/list?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);


    function delTask(taskId){
        parent.layer.confirm('确认要删除该任务吗？', function(index) {
            window.location.href="${ctx}/task/ucpTask/delete?id=" + taskId;
            parent.layer.close(index);
        });
    }
</script>

