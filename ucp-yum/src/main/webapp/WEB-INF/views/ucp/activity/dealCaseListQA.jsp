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
                        <label>关键字：</label>
                        <input type="text" name="searchTxt" value="${searchTxt}">
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
                <input type="hidden" name="status" value="${status}"/>
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
        </div>
        <div class="sec-tbl sec-tbl-width">
            <c:forEach items="${fns:getDictList('test_status')}" var="g">
                <input type="radio" name="statusSelect" value="${g.value}"/>${g.label}&nbsp;
            </c:forEach>
            <table id="contentTable" class="table  table-bordered table-condensed">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>优先级</th>
                    <th>活动编号</th>
                    <th>标题</th>
                    <th>品牌</th>
                    <th>券数量</th>
                    <th>建议测试时间</th>
                    <th>测试完成时间</th>
                    <th>状态</th>
                    <th>活动创建人</th>
                    <th>L2经办人</th>
                    <th>QA经办人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr <c:if test="${issue.recommendedTestTimeOutBoolean && (issue.status eq '13011' || issue.status eq '13012') }">style="background-color: #f47564;"</c:if>>
                        <td> ${ status.index + 1}</td>
                        <td <c:if test="${issue.priority == 1}">class="red"</c:if>>${fns:getDictLabel(issue.priority, 'priority', '')}</td>
                        <td title="${issue.notifyActivityNo}">${issue.notifyActivityNo}</td>
                        <td title="${issue.summary}">${issue.summary}</td>
                        <td>${fns:getDictLabel(issue.brand, 'brand', '')}</td>
                        <td>${issue.couponCount}</td>
                        <td>${issue.recommendedTestTime}</td>
                        <td>${issue.testFinishedTime}</td>
                        <td>${fns:getDictLabel(issue.status, 'activity_status', '')}</td>
                        <td>${fns:getUserById(issue.createBy).name}</td>
                        <td>${fns:getUserNameByWorkCode(issue.l2Submitter)}</td>
                        <td>${fns:getUserNameByWorkCode(issue.receiveUser)}</td>
                        <td>
                            <c:if test="${issue.type ne '1'}">
                                <c:choose>
                                    <c:when test="${issue.receiveUser == null and (issue.status eq actStatusNotTested or issue.status eq actStatusTestFeedBack)}">
                                        <a href="javascript:void(0)" onclick="qaReceiveAct('${issue.id}')">认领</a>&nbsp;
                                    </c:when>
                                    <c:when test="${issue.receiveUser == agent}">
                                        <c:if test="${issue.status eq actStatusTesting}">
                                            <a target="_blank" href="${ctx}/activity/qa/detail?actId=${issue.id}">继续</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/approval?actId=${issue.id}">测试报告</a>&nbsp;
                                        </c:if>

                                        <c:if test="${issue.status eq actStatusNotPass}">
                                            <a target="_blank" href="${ctx}/activity/qa/detail?actId=${issue.id}">查看</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/feedBackReportDetail?actId=${issue.id}">反馈报告</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/approval?actId=${issue.id}">测试报告</a>&nbsp;
                                        </c:if>

                                        <c:if test="${issue.status eq actStatusTestFeedBack}">
                                            <a target="_blank" href="${ctx}/activity/qa/detail?actId=${issue.id}">查看</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/recheck?actId=${issue.id}">反馈报告</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/approval?actId=${issue.id}">测试报告</a>&nbsp;
                                        </c:if>

                                        <c:if test="${issue.status eq actStatusPass}">
                                            <a target="_blank" href="${ctx}/activity/qa/detail?actId=${issue.id}">查看</a>&nbsp;
                                            <a target="_blank" href="${ctx}/activity/qa/report?actId=${issue.id}">测试报告</a>&nbsp;
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <a target="_blank" href="${ctx}/activity/qa/detail?actId=${issue.id}">查看</a>&nbsp;
                                        <c:if test="${issue.status ne actStatusTesting}">
                                            <a target="_blank" href="${ctx}/activity/qa/feedBackReportDetail?actId=${issue.id}">反馈报告</a>&nbsp;
                                        </c:if>
                                        <c:if test="${issue.status ne actStatusPass}">
                                            <a target="_blank" href="${ctx}/activity/qa/report?actId=${issue.id}">测试报告</a>&nbsp;
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:if test="${issue.type eq '1'}">
                                <c:choose>
                                    <c:when test="${issue.status eq actStatusTesting}">
                                        <a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">继续</a>&nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        <a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">查看</a>&nbsp;
                                    </c:otherwise>
                                </c:choose>

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
    var ctx = '${ctx}';
    initStatus();
    $(".form_datetime").datepicker({format: 'yyyy-mm-dd'});
    var lis = $('nav li');
    lis.removeClass();
    $("#deal_case").attr("class", "current");

    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/activity/dealListQA?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
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
//    setTimeout(" reload()", 120000);
    function reload() {
        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/activity/dealList?time=" + new Date().getTime());
    }
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);

    $("input[name='statusSelect']").click(function () {
        $("input[name='status']").val($(this).val());
        page();
    });

    function initStatus() {
        var status = $("input[name='status']").val();
        if(!status) {
            $("input[name='statusSelect']").eq(0).attr("checked", true);
            return;
        }
        $("input[name='statusSelect']").each(function(){
            if($(this).val() == status) {
                $(this).attr("checked", true);
            }
        })
    }

    function qaReceiveAct(id){
        parent.layer.confirm('是否确认认领该活动？', function(index) {
            window.location.href="${ctx}/activity/receiveQA?id=" + id;
            parent.layer.close(index);
        });
    }
</script>
<script src="${ctxStatic}/ucp/task.js?v=20190809"></script>
