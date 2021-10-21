<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<body>
<div class="tab-con all_case_find">
    <div class="sec">
        <article class="sec-title t3">搜索</article>
        <div class="sec-form input-style">
            <form id="searchForm" action="${ctx}/common/allCases" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li style="list-style: none;">
                        <label>我的case：</label>
                        <input id="checkboxCss" style="border:0px solid #bfc7db;" type="checkbox" class="creator-width"
                               name="creator"
                               <c:if test="${creator==jiraUserName}">checked value="${creator}"</c:if>
                               onclick="addValue('${jiraUserName}')">
                        <span class="creator-value">是</span>
                    </li>
                </div>
                <div class="sec-form">
                    <li>
                        <label>项目：</label>
                        <select name="projectId" onchange="findIssueType()">
                            <option value="">全部</option>
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
                            <select class="preorder" name="preorder">
                                <c:forEach items="${thirdPartyRepairOnesMonitor}" var="card">
                                    <option
                                            <c:if test="${preorder==card.value}">selected</c:if>
                                            value="${card.value}">${card.name}</option>
                                </c:forEach>
                            </select>
                        </li>
                    </c:if>

                </div>
                <div class="sec-form">
                    <li>
                        <label>类型：</label>
                        <select name="issueTypeId" onchange="findStatuses()">
                            <option value="">全部</option>
                            <c:forEach items="${issueTypes}" var="s">
                                <option
                                        <c:if test="${s.target==issueTypeId}">selected</c:if>
                                        value="${s.target}">${s.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <label>状态：</label>
                        <select name="status">
                            <option value="">全部</option>
                            <c:forEach items="${statues}" var="s">
                                <option
                                        <c:if test="${s.value==status}">selected</c:if>
                                        value="${s.value}">${s.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                </div>

                <div class="sec-form">
                    <li>
                        <label>优先级：</label>
                        <select name="priority">
                            <option value="">全部</option>
                            <c:forEach items="${priorities}" var="p">
                                <option
                                        <c:if test="${p.name==priority}">selected</c:if>
                                        value="${p.name}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <c:if test="${roleType==2}">
                        <li>
                            <label>经办人：</label>
                            <input type="text" name="assignee" value="${assignee}" style="min-width:0px">
                        </li>
                    </c:if>
                </div>

                <div class="sec-form">
                    <li>
                        <label>关键字：</label>
                        <input type="text" name="text" value="${text}" style="min-width:220px">
                    </li>
                    <li>
                        <label>时间：</label>
                        <input 　type="text" id="startTime" name="startDate" class="form_datetime" value="${startDate}"
                               style="width: 220px">
                        <input 　type="text" id="endTime" name="endDate" class="form_datetime" value="${endDate}"
                               style="width: 220px">
                    </li>
                </div>
                <input type="submit" id="search" onclick="return page();" value="">
                <c:if test="${monitor!=null}">
                    <input type="button" id="lockKey" value="批量结案"
                           style="height: 30px;width: 80px;border-radius: 3px;margin-left: 15px;float: left;background: #F47564;color: white">
                </c:if>
                <input type="text" class="sort" value="${sort}" style="display: none">
                <input type="text" class="sortBy" value="${sortBy}" style="display: none">
            </form>
        </div>
    </div>
    <div class="sec">
        <article class="sec-title t1">搜索结果共计<span id="count">${page.count}</span>个数据</article>
        <div class="sec-tbl">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <c:if test="${monitor!=null}">
                        <th>批处理</th>
                    </c:if>
                    <th style="cursor:pointer" onclick="sortPage('summary')">主题</th>
                    <th style="cursor:pointer" onclick="sortPage('锁定人')">类型</th>
                    <th style="cursor:pointer" onclick="sortPage('Priority')">优先级</th>
                    <th style="cursor:pointer" onclick="sortPage('status')">状态</th>
                    <th style="cursor:pointer" onclick="sortPage('created')">登记时间</th>
                    <th style="cursor:pointer" onclick="sortPage('锁定人')">锁定人</th>
                    <c:if test="${monitor==null and roleType==2}">
                        <th style="cursor:pointer">经办人</th>
                    </c:if>
                    <c:if test="${monitor!=null}">
                        <th style="cursor:pointer">解决人</th>
                    </c:if>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue">
                    <tr>
                        <c:if test="${monitor!=null}">
                            <td><input type="checkbox" name="lockKey" style="margin-top:14px" value="${issue.key}"></td>
                        </c:if>
                        <td>${issue.summary}</td>
                        <td>${issue.type}</td>
                        <td>
                            <c:if test="${monitor!=null}">
                                <c:if test="${issue.priority=='Highest'}">
                                    <img src="${ctxStatic}/images/timg.jpg" class="highestImage"
                                         style="width: 20px;margin-left: -2px;height: 20px;">
                                </c:if>
                            </c:if>
                                ${issue.priority}
                        </td>
                        <td>${issue.jiraStatus}</td>
                        <td>${issue.creationDate}</td>
                        <!-- 锁定人begin -->
                        <c:if test="${monitor!=null or iotmonitor != null}">
                            <td>${issue.lockUser}</td>
                        </c:if>
                        <c:if test="${monitor==null and iotmonitor == null}">
                            <td>${issue.lockUserName}</td>
                        </c:if>
                        <!-- 锁定人end -->
                        <c:if test="${monitor==null and roleType==2}">
                            <td>
                                    ${issue.assignee}
                            </td>
                        </c:if>
                        <c:if test="${monitor!=null}">
                            <td>
                                    ${issue.resolvePerson}
                            </td>
                        </c:if>
                        <td>
                            <a target="_blank"
                               href="${ctx}${issue.viewPage}?key=${issue.key} ">查看</a>
                            <c:if test="${issue.l1IsDeal}">
                                <!-- iot和监控不显示处理按钮 -->
                                <c:if test="${issue.jiraStatus!='CSP处理中' and monitor == null and iotmonitor == null}">
                                    <a target="_blank"
                                       href="${ctx}${issue.lv1Page}?key=${issue.key} ">处理</a>
                                </c:if>
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
<script>
    var html = "<textarea class='endCasesText' style='width: 100%;height: 150px'></textarea>";
    $(function () {
        if (!!window.ActiveXObject || "ActiveXObject" in window) {
            $("#checkboxCss").css("margin-right", "-2px");
            $("#checkboxCss").css("height", "21px");
        }
    })
    function addValue(jiraName) {
        debugger;
        if ($("input[name='creator']").attr("checked")) {
            $("input[name='creator']").val(jiraName);
        } else {
            $("input[name='creator']").val("");
        }
    }

    //批量结案
    $("#lockKey").click(function () {
        var endCasesKey = "";
        var groupCheckbox = $("input[name='lockKey']");
        for (i = 0; i < groupCheckbox.length; i++) {
            if (groupCheckbox[i].checked) {
                endCasesKey += groupCheckbox[i].value + ",";
            }
        }
        if (endCasesKey != "") {
            $.Dialog.load({
                dialogHtml: html,
                title: "结案备注",
                btnok: "确定",
                btncl: "取消",
                okCallBack: function () {
                    if ($(".endCasesText").val() == null || $(".endCasesText").val() == "") {
                        Common.myAlert("备注值不能为空", 3);
                        return false;
                    }
                    $.ajax({
                        type: "post",
                        data: {key: endCasesKey, endCasesText: $(".endCasesText").val()},
                        url: "${ctx}/monitor/info/endCases",
                        success: function (msg) {
                            if (msg.success) {
                                Common.myAlert(msg.message);
                                location.reload();
                            } else {
                                Common.myAlert(msg.message);
                            }
                        }
                    });
                }
            });
            $("input[name='customName2']").val(customName);
            $("input[name='customPhone2']").val(customContact);
        } else {
            Common.myAlert(msg.message);
        }
    })

    $("#startTime").datepicker({
        minView: "month",
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: "linked",
        pickerPosition: "bottom-left"
    }).on('changeDate', function (ev) {
        var starttime = $("#startTime").val();
        $("#endTime").datepicker('setStartDate', starttime);
        $("#startTime").datepicker('hide');
    });

    $("#endTime").datepicker({
        minView: "month",
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: "linked",
        pickerPosition: "bottom-left"
    }).on('changeDate', function (ev) {
        var starttime = $("#startTime").val();
        var endtime = $("#endTime").val();
        $("#startTime").datepicker('setEndDate', endtime);
        $("#endTime").datepicker('hide');
    });
    var lis = $('nav li');
    lis.removeClass();
    $("#all_case").attr("class", "current");
    function page(n, s) {
        if (n) $("#pageNo").val(n);
        if (s) $("#pageSize").val(s);
        $("#searchForm").attr("action", "${ctx}/common/allCases?sort=" + $(".sort").val() + "&&sortBy=" + $(".sortBy").val());
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

    $(window.parent.document).find("#all_case").height($(".all_case_find").height());
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
                    findStatuses();
                } else {
                    Common.myAlert(msg.message);
                }

            }
        })
    }

    function findStatuses() {
        $.ajax({
            type: "get",
            data: {
                "projectKey": $("select[name='projectId']").val(),
                "issueTypeId": $("select[name='issueTypeId']").val()
            },
            url: "${ctx}/findStatusByProject?time=" + new Date().getTime(),
            success: function (msg) {
                if (msg.success) {
                    $("select[name='status']").children().remove();
                    var html = " <option value=\"\">全部</option>";
                    $.each(msg.obj, function (index, item) {
                        html += "<option value=\"" + item.value + "\">" + item.name + "</option>"
                    });
                    $("select[name='status']").append(html);
                } else {
                    Common.myAlert(msg.message);
                }

            }
        })
    }

</script>

