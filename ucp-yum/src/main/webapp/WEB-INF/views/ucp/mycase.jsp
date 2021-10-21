<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<style>
    .sec-form li{
        margin-left:0px;
        margin-right: 0px;
    }
    .sec-form li select,input{
        margin-top: -4px;
    }
    .sec-form li{
        padding:0 0 5px 0
    }
</style>
<body>
<div class="tab-con my-case-find">
    <div class="sec input-style">
        <div class="sec-form">
            <form id="searchForm" action="${ctx}/common/myProjectCases" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>项目：</label>
                        <select name="projectId" onchange="findIssueType()">
                        <c:if test="${monitor==null}">
                            <option value="">全部</option>
                        </c:if>
                            <c:forEach items="${projects}" var="p">
                                <option
                                        <c:if test="${p.id==projectId}">selected</c:if>
                                        value="${p.id}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <c:if test="${monitor!=null}">
                        <li>
                            <label>业务类型：</label>
                            <select  class="preorder" name="preorder">
                                <c:forEach items="${thirdPartyRepairOnesMonitor}" var="card">
                                    <option  <c:if test="${preorder==card.value}">selected</c:if> value="${card.value}" >${card.name}</option>
                                </c:forEach>
                            </select>
                        </li>
                    </c:if>
                </div>
                <div class="sec-form">
                    <li>
                        <label>类型：</label>
                        <select name="issueTypeId">
                            <option value="">全部</option>
                            <c:forEach items="${issueTypes}" var="s">
                                <option
                                        <c:if test="${s.target==issueTypeId}">selected</c:if>
                                        value="${s.target}">${s.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                </div>
                <div class="sec-form" >
                    <li>
                        <label>关键字：</label>
                        <input type="text" name="text" value="${text}" style="min-width:220px">
                    </li>
                    <li>
                        <label>时间：</label>
                        <input id="startTime"　type="text" name="startDate" class="form_datetime" value="${startDate}" style="width: 220px">
                        <input id="endTime"　type="text" name="endDate" class="form_datetime" value="${endDate}" style="width: 220px">
                    </li>
                </div>

                <input type="submit" id="search" onclick="return page();" value="">
            </form>
            <div class="clear"></div>
        </div>
    </div>
    <div class="sec">
        <article class="sec-title t1">搜索结果共计<span id="count">${page.count}</span>个数据</article>
        <div class="sec-tbl">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th>主题</th>
                    <th>类型</th>
                    <th>优先级</th>
                    <th>状态</th>
                    <th>登记时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr>
                        <td>${issue.summary}</td>
                        <td>${issue.type}</td>
                        <td>${issue.priority}</td>
                        <td>${issue.jiraStatus}</td>
                        <td>${issue.creationDate}</td>
                        <td>
                            <c:if test="${monitor != null}">
                                <c:if test="${issue.isLock == true and issue.lockUserName == userName or issue.isLock == false}">
                                    <a target="_blank" href="${ctx}${issue.lv1Page}?key=${issue.key} ">详情</a>
                                </c:if>
                            </c:if>
                            <c:if test="${monitor == null}">
                                <a target="_blank" href="${ctx}${issue.viewPage}?key=${issue.key} ">查看</a>
                                <a target="_blank"
                                   href="${ctx}${issue.lv1Page}?key=${issue.key} ">处理</a>
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
</body>
</html>
<script>
    //$(".form_datetime").datepicker({format: 'yyyy-mm-dd'});

    $("#startTime").datepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: "linked",
        pickerPosition: "bottom-left"
    }).on('changeDate',function(ev){
        var starttime=$("#startTime").val();
        $("#endTime").datepicker('setStartDate',starttime);
        $("#startTime").datepicker('hide');
    });

    $("#endTime").datepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: "linked",
        pickerPosition: "bottom-left"
    }).on('changeDate',function(ev){
        var starttime=$("#startTime").val();
        var endtime=$("#endTime").val();
        $("#startTime").datepicker('setEndDate',endtime);
        $("#endTime").datepicker('hide');
    });

    var lis = $('nav li');
    lis.removeClass();
    $("#my_case").attr("class", "current");
    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/common/myProjectCases");
        $("#searchForm").submit();
        return false;
    }
    $(window.parent.document).find("#my_case").height($(".my-case-find").height());
    setTimeout(" reload()", 120000) ;
    function reload(){
        $(window.parent.document).find("#my_case").attr("src", "${ctx}/common/myProjectCases?time=" + new Date().getTime());
    }
    function findIssueType() {
        $.ajax({
            type: "get",
            data: {
                "projectKey": $("select[name='projectId']").val()
            },
            url: "${ctx}/findIssueTypesByProject?time=" + new Date().getTime(),
            success: function (msg) {
                if (msg.success) {
                    $("select[name='issueTypeId']").children().remove();
                    var html = " <option value=\"\">全部</option>";
                    $.each(msg.obj, function (index, item) {
                        html += "<option value=\"" + item.target + "\">" + item.name + "</option>"
                    });
                    $("select[name='issueTypeId']").append(html);
                } else {
                    Common.myAlert(msg.message);
                }

            }
        })
    }
</script>
