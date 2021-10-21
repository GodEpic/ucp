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
            <form id="searchForm" action="${ctx}/common/allCases" method="post">
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
        <div class="sec-tbl sec-tbl-width">
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th style="cursor:pointer" onclick="sortPage('summary')">主题</th>
                    <th style="cursor:pointer" onclick="sortPage('Priority')">优先级</th>
                    <th style="cursor:pointer" onclick="sortPage('created')">发布时间</th>
                    <th style="cursor:pointer" onclick="sortPage('status')">JIRA状态</th>
                    <th style="cursor:pointer" onclick="sortPage('status')">状态</th>
                    <th style="cursor:pointer" onclick="sortPage('锁定人')">锁定人</th>
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
                        <c:if test="${monitor!=null}">
                            <td>${issue.lockUser}</td>
                        </c:if>
                        <c:if test="${monitor==null}">
                            <td>${issue.lockUserName}</td>
                        </c:if>
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
            <input type="text" class="sort" value="${sort}" style="display: none">
            <input type="text" class="sortBy" value="${sortBy}" style="display: none">

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
        $("#searchForm").attr("action", "${ctx}/common/myDealCases?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
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
    setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/common/myDealCases?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);

</script>

