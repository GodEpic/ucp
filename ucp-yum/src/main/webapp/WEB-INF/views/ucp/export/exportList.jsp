<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<style>
    .sec-tbl {
        max-height:750px;
        overflow: auto;
    }
    .sec-tbl td {
        word-break: break-all;
        min-width: 50px;
        line-height: 25px !important;
    }
    .errorMessage {
        color: red !important;
        text-align: left !important;
        margin-left: 10px;
    }
    img, input, button, select, textarea {
        width: auto;
    }
</style>
<body>
<div class="tab-con all_case_find">
    <div class="sec">
        <article class="sec-title t3">搜索</article>
        <div class="sec-form input-style">
            <form id="searchForm" action="${ctx}/export/list" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="sec-form">
                    <li>
                        <label>导出模板：</label>
                        <select name="exportConfigId">
                            <c:forEach items="${exportConfigList}" var="p">
                                <option
                                        <c:if test="${p.id==exportConfigId}">selected</c:if>
                                        value="${p.id}">${p.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                </div>
                <input type="button" id="search" value="">
                <input type="button" id="export" value="">
                <label class="errorMessage">${errorMessage}</label>
            </form>
        </div>
    </div>
    <div class="sec">
        <div class="sec-tbl">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <c:forEach items="${headList}" var="head">
                        <th>${head}</th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <c:forEach items="${item}" var="entry">
                            <td>${entry.value}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script>
    $("#search").click(function () {
        $("#searchForm").attr("action", "${ctx}/export/list");
        $("#searchForm").submit();
    })
    $("#export").click(function () {
        var result = confirm("确认导出？");
        if(result){
            $("#searchForm").attr("action","${ctx}/export/exportRecord");
            $("#searchForm").submit();
        }
    })
</script>

