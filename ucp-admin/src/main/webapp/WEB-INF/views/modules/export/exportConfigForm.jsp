<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<head>
    <title>导出配置管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/export/list">导出配置管理</a></li>
        <li class="active"><a href="${ctx}/export/form?id=${exportConfig.id}">导出配置${not empty exportConfig.id?'修改':'添加'}</a></li>
    </ul><br/>
    <form:form id="inputForm" modelAttribute="exportConfig" action="${ctx}/export/save" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <div class="control-group">
            <label class="control-label">名称：</label>
            <div class="controls">
                <form:input path="name" htmlEscape="false" maxlength="200" class="required" size="100"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">导出SQL：</label>
            <div class="controls">
                <form:textarea cssStyle="width: 950px;height: 550px;" path="exportSql" htmlEscape="false"/>
            </div>
        </div>
        <div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
        </div>
    </form:form>

</body>
<script>
    var ctx = '${ctx}';
</script>
<script>
    $(function () {
        $("#exportButton").click(function () {
            top.$.jBox.confirm("确认导出？","系统提示",function(v,h,f){
                if(v=="ok"){
                    $("#checkForm").attr("action","${ctx}/sys/menu/exportRecord");
                    $("#checkForm").submit();
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
        })
    })
</script>
