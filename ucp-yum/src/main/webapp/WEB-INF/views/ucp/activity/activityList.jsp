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
                <input type="hidden" name="status" value="${status }" />
				<div class="sec-form">
                   <li>
                       <label>关键字：</label>
                       <input type="text" name="searchTxt" value="${searchTxt}">
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

        </div>
        <div class="sec-tbl sec-tbl-width">
            <c:choose>
                <c:when test="${status eq '1'}">
            <input type="radio" name="releaseStatusSelect"  value="1" checked/>待接受
            &nbsp;<input type="radio" name="releaseStatusSelect"  value=""/>所有
                </c:when>

                <c:otherwise>
                <input type="radio" name="releaseStatusSelect"  value="1" />待接受
                &nbsp;<input type="radio" name="releaseStatusSelect"  value="" checked/>所有
                </c:otherwise>
            </c:choose>

            <table id="contentTable" class="table  table-bordered table-condensed" >
                <thead>
                <tr>
                    <th>序号</th>
                    <th>活动编号</th>
                    <th>活动名称</th>
                    <th>品牌</th>
                    <th>文件地址</th>
                    <th>状态</th>
                    <th>备注</th>
                    <th>申请时间</th>
                    <th>申请人</th>
                    <th>加急状态</th>
                    <th>加急时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="issue" varStatus="status">
                    <tr>
                        <td> ${ status.index + 1}</td>
                        <td title="${issue.activityId}">${issue.activityId}</td>
                        <td title="${issue.activityName }">${issue.activityName}</td>
                        <td>${issue.brand}</td>
                        <td style="word-break:break-all;white-space:nowrap;overflow:hidden;text-overflow: ellipsis" >
                    <c:forEach items="${issue.notifyFileList}" var="fileList" >
                            <a href="${fileList.fileUrl}" title="${fileList.fileName }">${fileList.fileName}</a><br>
                    </c:forEach>
                        </td>
                        <td> ${issue.statusValue}</td>
                        <td> ${issue.comments}</td>
                        <td style="white-space:nowrap;overflow:hidden;text-overflow: ellipsis" title="<fmt:formatDate value="${issue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate value="${issue.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td> ${issue.proposer}</td>
                        <c:if test="${issue.isUrgent eq 'Y'}">
                            <td><font size="3" color="red">加急</font></td>
                            <td style="white-space:nowrap;overflow:hidden;text-overflow: ellipsis" title="<fmt:formatDate value="${issue.urgentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate value="${issue.urgentDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </c:if>
                        <c:if test="${issue.isUrgent eq 'N'}">
                            <td>不加急</td>
                            <td></td>
                        </c:if>
                        <td  style="word-break:break-all;">
                            <c:choose>
                                <c:when test="${issue.status eq '1'}">
                                    <a href="${ctx}/activity/form?notifyId=${issue.id}&brand=${issue.brand}&notifyActivityNo=${issue.activityId}" target="view_window">创建活动</a>
                                    <a href="javascript:void(0)" onclick="revocation('${issue.id}')" >撤回</a>
                                </c:when>
                                <c:otherwise>
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                </c:otherwise>
                            </c:choose>
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
        $("#searchForm").attr("action", "${ctx}/activity/activityList?sort=" + $(".sort").val() + "&sortBy=" + $(".sortBy").val());
        $("#searchForm").submit();
        return false;
    }

    $("input[name='releaseStatusSelect']").click(function () {
        $("input[name='status']").val($(this).val());
        page();
    });

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


    function revocation(actId){

        parent.layer.confirm('确认要撤回该活动吗？', function(index) {
            window.location.href="${ctx}/activity/revocation?notivifyId=" + actId;
            parent.layer.close(index);
        });
    }


</script>
<script src="${ctxStatic}/ucp/task.js?v=2019090413"></script>
