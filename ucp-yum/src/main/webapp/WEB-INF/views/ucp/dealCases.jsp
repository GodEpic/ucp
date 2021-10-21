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
            <form id="searchForm" action="${ctx}/common/l1DealCases" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>关键字：</label>
                        <input type="text" name="text" value="${text}">
                    </li>
                </div>
                <input type="submit" id="search" onclick="return page();" value="">
            </form>
        </div>
    </div>

    <div class="sec no-border">
        <div class="sec-tbl">
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>主题</th>
                    <th>优先级</th>
                    <th>发布时间</th>
                    <th>JIRA状态</th>
                    <th>状态</th>
                    <th>锁定人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">

                    <tr <c:if test="${issue.isColor}">class='press-color'</c:if>>
                        <td> ${ status.index + 1}</td>
                        <td>${issue.summary}</td>
                        <td>${issue.priority}</td>
                        <td>${issue.creationDate}</td>
                        <td>${issue.jiraStatus}</td>
                        <td>${issue.status}</td>
                        <td>${issue.lockUserName}</td>
                        <td>
                            <c:if test="${!issue.isLock}">
                                <a target="_blank" href="${ctx}${issue.lv2Page}?key=${issue.key}">详情</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">${page}</div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/views/include/footer.jsp" %>

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
        $("#searchForm").attr("action", "${ctx}/common/l1DealCases");
        $("#searchForm").submit();
        return false;
    }
    setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/common/l1DealCases?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#l1_deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#l1_deal_case").height($(".deal_l2").height() + 40);

</script>

