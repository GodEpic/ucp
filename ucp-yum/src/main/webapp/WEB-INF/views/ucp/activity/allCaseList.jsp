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
        <article class="sec-title t3">搜索</article>
        <div class="sec-form">
            <form:form id="searchForm" modelAttribute="activity" action="${ctx}/activity/" method="post" class="form-search">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>标题：</label>
                        <input type="text" name="summary" value="${summary}">
                    </li>
                </div>
                <input type="hidden" name="releaseStatus" value="${releaseStatus}"/>
                <input type="submit" id="search" onclick="return page();" value="">
            </form:form>
        </div>
    </div>

    <div class="sec no-border">
        <div class="sec-tbl sec-tbl-width">
            <c:if test="${actStatusPass!=releaseStatus}">
                <input type="radio" name="releaseStatusSelect" checked value="${RELEASE_STATUS_RELEASED}"/>未完成&nbsp;
                <input type="radio" name="releaseStatusSelect" value="${actStatusPass}"/>已完成&nbsp;
            </c:if>
            <c:if test="${actStatusPass==releaseStatus}">
                <input type="radio" name="releaseStatusSelect" value="${RELEASE_STATUS_RELEASED}"/>未完成&nbsp;
                <input type="radio" name="releaseStatusSelect" checked value="${actStatusPass}"/>已完成&nbsp;
            </c:if>
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>标题</th>
                    <th>品牌</th>
                    <th>券数量</th>
                    <th>上架时间</th>
                    <th>优先级</th>
                    <th>活动状态</th>
                    <th>创建经办人</th>
                    <th>操作</th>
                    <th>QA测试结果</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${page.list}" var="issue" varStatus="status">
                        <tr>
                            <td> ${ status.index + 1}</td>
                            <td title="${issue.summary}">${issue.summary}</td>
                            <td>${fns:getDictLabel(issue.brand, 'brand', '')}</td>
                            <td>${issue.couponCount}</td>
                            <td>${issue.launchTime}</td>
                            <td <c:if test="${issue.priority == 1}">class="red"</c:if>>${fns:getDictLabel(issue.priority, 'priority', '')}</td>
                            <td>${fns:getDictLabel(issue.status, 'activity_status', '')}</td>
                            <td>${fns:getUserById(issue.createBy).name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${issue.status eq actStatusNotPass || issue.status eq actStatusNotTested || issue.status eq actStatusTesting || issue.status eq actStatusPass || issue.status eq actStatusTestFeedBack}">
                                        <c:choose>
                                            <c:when test="${issue.type eq '1'}">
                                                <a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">查看</a>&nbsp;
                                            </c:when>
                                            <c:otherwise>
                                                <a target="_blank" href="${ctx}/activity/detail?id=${issue.id}">查看</a>&nbsp;
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${issue.type eq '1'}">
                                                <c:if test="${releaseStatus eq RELEASE_STATUS_UNRELEASED}">
                                                    <a target="_blank" href="${ctx}/activity/urgentForm?id=${issue.id}">修改</a>&nbsp;
                                                </c:if>
                                                <c:if test="${releaseStatus ne RELEASE_STATUS_UNRELEASED}">
                                                    <a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">查看</a>&nbsp;
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <a target="_blank" href="${ctx}/activity/form?id=${issue.id}">修改</a>&nbsp;
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${issue.type ne '1'}">
                                    <a target="" href="${ctx}/task/ucpTask/allList?actId=${issue.id}">查看任务台账</a>&nbsp;
                                </c:if>

                            </td>
                            <td>
                                <c:if test="${issue.type ne '1'}">
                                    <c:if test="${issue.status eq actStatusPass or issue.status eq actStatusTesting}">
                                        <a target="_blank" href="${ctx}/activity/qa/report?actId=${issue.id}">测试报告</a>&nbsp;
                                    </c:if>

                                    <c:if test="${issue.status eq actStatusNotPass}">
                                        <a target="_blank" href="${ctx}/activity/qa/feedBack?actId=${issue.id}">反馈报告</a>&nbsp;
                                        <a target="_blank" href="${ctx}/activity/qa/report?actId=${issue.id}">测试报告</a>&nbsp;
                                    </c:if>

                                    <c:if test="${issue.status eq actStatusTestFeedBack}">
                                        <a target="_blank" href="${ctx}/activity/qa/feedBackReportDetail?actId=${issue.id}">反馈报告</a>&nbsp;
                                        <a target="_blank" href="${ctx}/activity/qa/report?actId=${issue.id}">测试报告</a>&nbsp;
                                    </c:if>
                                </c:if>
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
        $("#searchForm").attr("action", "${ctx}/activity/allList?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
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

    //setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/activity/list?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);
    $(window.parent.document).find('#all_case').height($(".deal_l2").height()+40);

</script>

