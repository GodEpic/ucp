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
            <form:form id="searchForm" modelAttribute="activity" action="${ctx}/activity/" method="post" class="form-search">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>标题：</label>
                        <input type="text" name="summary" value="${summary}">
                    </li>
                    <li>
                        <%--<label>状态：</label>--%>
                        <%--<select class="select-status">--%>
                            <%--<option>请选择</option>--%>
                            <%--<option>状态1</option>--%>
                        <%--</select>--%>
                        <%--<select name="couponType" class="select-status">
                            <c:forEach items="${fns:getDictList('couponType')}" var="g">
                                <option value="${g.value}" title="${g.description}" <c:if test="${g.value == activity.couponType}">selected</c:if>>${g.label}</option>
                            </c:forEach>
                        </select>--%>
                    </li>
                </div>
                <input type="hidden" name="releaseStatus" value="${releaseStatus}"/>
                <input type="submit" id="search" onclick="return page();" value="">
            </form:form>
        </div>
    </div>
    <div id="messageBox" class="alert alert-${alertClass} ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
        <label>${message}</label>
    </div>
    <div class="sec no-border">
        <div class="div-head">
            <div class="div-records">处理记录</div>
            <div class="div-create">
                <a class="layui-yum-btn" type="button" target="_blank" href="${ctx}/activity/urgentForm?type=2">创建键值任务</a>
            </div>
        </div>
        <div class="sec-tbl sec-tbl-width">
            <c:forEach items="${fns:getDictList('release_status')}" var="g" varStatus="gInd">
                <c:choose>
                    <c:when test="${releaseStatus eq g.value}">
                        <input type="radio" name="releaseStatusSelect" checked value="${g.value}"/>${g.label}&nbsp;
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="releaseStatusSelect" value="${g.value}"/>${g.label}&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <input type="radio" name="releaseStatusSelect" <c:if test="${actStatusPass==releaseStatus}">checked</c:if> value="${actStatusPass}"/>已完成&nbsp;
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>标题</th>
                    <th>品牌</th>
                    <th>券数量</th>
                    <th>创建时间</th>
                    <th>上架时间</th>
                    <th>优先级</th>
                    <th>发布状态</th>
                    <th>活动状态</th>
                    <th>创建经办人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr>
                        <td> ${ status.index + 1}</td>
                        <td title="${issue.summary}">${issue.summary}</td>
                        <td>${fns:getDictLabel(issue.brand, 'brand', '')}</td>
                        <td>${issue.couponCount}</td>
                        <td><fmt:formatDate value="${issue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>${issue.launchTime}</td>
                        <td <c:if test="${issue.priority == 1}">class="red"</c:if>>${fns:getDictLabel(issue.priority, 'priority', '')}</td>
                        <td>${fns:getDictLabel(issue.releaseStatus, 'release_status', '')}</td>
                        <td>${fns:getDictLabel(issue.status, 'activity_status', '')}</td>
                        <td>${fns:getUserById(issue.createBy).name}</td>
                        <td>
                            <c:if test="${issue.type eq '2'}">
                                <c:if test="${releaseStatus eq RELEASE_STATUS_UNRELEASED}">
                                    <a target="_blank" href="${ctx}/activity/urgentForm?id=${issue.id}" >编辑</a>&nbsp;
                                    <a href="javascript:void(0)" onclick="delActivity('${issue.id}')">删除</a>
                                </c:if>
                                <c:if test="${releaseStatus ne RELEASE_STATUS_UNRELEASED}">
                                    <a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">查看</a>&nbsp;
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
        $("#searchForm").attr("action", "${ctx}/activity/dealKeyValueList?sort=" + $(".sort").val() + "&sortBy=" + $(".sortBy").val());
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

    function delActivity(actId){
        parent.layer.confirm('确认要删除该活动吗？', function(index) {
            window.location.href="${ctx}/activity/delete?id=" + actId;
            parent.layer.close(index);
        });
    }
</script>
<script src="${ctxStatic}/ucp/task.js?v=2019090413"></script>
