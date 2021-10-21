<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html lang="en">
	<head lang="en">
	    <title>L2待处理任务</title>
	    <%@include file="/WEB-INF/views/include/head.jsp" %>
	</head>
	<script src="${ctxStatic}/ucp/task.js?v=20190809"></script>
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
						<input type="hidden" name="verifyStatus" value="${verifyStatus}"/>
		                <input type="submit" id="search" onclick="return page();" value="">
		            </form:form>
		        </div>
		    </div>
			<div id="messageBox" class="alert alert-${alertClass} ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
				<label>${message}</label>
			</div>
		    <div class="sec no-border">
		        <div class="sec-tbl sec-tbl-width">
					<c:forEach items="${fns:getDictList('key_value_verify_status')}" var="g">
						<input type="radio" name="verifyStatusSelect" value="${g.value}"/>${g.label}&nbsp;
					</c:forEach>
					<input type="radio" name="verifyStatusSelect" value=""/>全部&nbsp;
		            <table id="contentTable" class="table  table-bordered table-condensed">
		                <thead>
		                <tr>
		                    <th>序号</th>
		                    <th>优先级</th>
		                    <th>任务发送时间</th>
		                    <th>活动名</th>
		                    <th>任务名</th>
		                    <th>券数量</th>
							<th>配置完成时间</th>
		                    <th>检查情况</th>
							<th>活动创建人</th>
							<th>L1经办人</th>
							<th>L2经办人</th>
		                    <th>操作</th>
		                </tr>
		                </thead>
		                <tbody>
		                <c:forEach items="${page.list}" var="issue" varStatus="status">
		                    <tr>
		                        <td> ${ status.index + 1}</td>
		                        <td <c:if test="${issue.priority == 1}">class="red"</c:if>>${fns:getDictLabel(issue.priority, 'priority', '')}</td>
		                        <td><fmt:formatDate value="${issue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                        <td title="${issue.activityName}">${issue.activityName}</td>
		                        <td title="${issue.activityName}-${issue.version}">${issue.activityName}-${issue.version}</td>
		                        <td>${issue.couponCount}</td>
								<td>${issue.confFinishedTime}</td>
		                        <td>${fns:getDictLabel(issue.verifyStatus, 'verify_status', '')}</td>
								<td>${fns:getUserById(issue.createBy).name}</td>
								<td>${fns:getUserNameByWorkCode(issue.receiveL1)}</td>
								<c:if test="${issue.type eq '2'}">
									<td>${fns:getUserNameByWorkCode(issue.receive)}</td>
								</c:if>
		                        <td>
									<c:if test="${issue.type eq '2' and issue.jiraStatus eq SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN}">
										<a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">继续</a>&nbsp;
									</c:if>
									<c:if test="${issue.type eq '2' and issue.jiraStatus eq ACTIVITY_JIRA_STATUS_DONE}">
										<a target="_blank" href="${ctx}/activity/urgentDetail?id=${issue.id}">查看</a>&nbsp;
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
		<script type="text/javascript">
            var ctx = '${ctx}';
            initVerifyStatus();
		    $(".form_datetime").datepicker({format: 'yyyy-mm-dd'});
		    var lis = $('nav li');
		    lis.removeClass();
		    $("#deal_case").attr("class", "current");
		    function page(n, s) {
		        if (n) $("#pageNo").val(n);
		        if (s) $("#pageSize").val(s);
		        $("#searchForm").attr("action", "${ctx}/task/ucpTask/dealKeyValueListL2");
		        $("#searchForm").submit();
		        return false;
		    }
		    //setTimeout(" reload()", 120000);
		    function reload() {
		        $(window.parent.document).find("#deal_case").attr("src", "${ctx}/task/ucpTask/list?time=" + new Date().getTime());
		    }
		    $(window.parent.document).find("#deal_case").height($(".deal_l2").height());
		    $(window.parent.document).find("#deal_case").height($(".deal_l2").height() + 40);

            $("input[name='verifyStatusSelect']").click(function () {
				$("input[name='verifyStatus']").val($(this).val());
                page();
            });

            function initVerifyStatus() {
                var verifyStatus = $("input[name='verifyStatus']").val();
                if(!verifyStatus) {
                    $("input[name='verifyStatusSelect']").attr("checked",'');
                    return;
                }
                $("input[name='verifyStatusSelect']").each(function(){
                    if($(this).val() == verifyStatus) {
                        $(this).attr("checked", true);
                    }
                })
            }

		</script>
	</body>
</html>
