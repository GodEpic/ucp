<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#inputForm").validate({
                rules: {
                    workCode: {remote: "${ctx}/sys/user/checkWorkCode?oldWorkCode=${workCode.workCode}" }
                },
                messages: {
                    workCode:{remote: "工号已存在"}
                },
                /*submitHandler: function(form){
                    var ids = [];
                    var nodes = $.fn.zTree.getZTreeObj("roleTree").getCheckedNodes(true);
                    if(nodes.length<=0)
                    {
                        alert("请给工号分配角色。");
                        return;
                    }
                    for(var i=0; i<nodes.length; i++) {
                        ids.push(nodes[i].id);
                    }
                    $("#roleIdList").val(ids);
                    loading('正在提交，请稍等...');
                    form.submit();
                },*/
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
        //树结构
        var setting = {check:{enable:true,chkStyle: "radio",radioType: "all"},view:{selectedMulti:false},
            data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
                tree.checkNode(node, !node.checked, true, true);
                return false;
            }}};
        function initRoleTree()
        {
            var roleType = $('#roleType').val();
            $.getJSON("${ctx}/sys/role/roleTree?roleType="+roleType,function(data){
                $.fn.zTree.init($("#roleTree"), setting, data).expandAll(true);
            });
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/user/list">用户列表</a></li>
    <li><a href="${ctx}/sys/user/workCodeList?id=${user.id}">工号列表</a></li>
    <li class="active"><a href="${ctx}/sys/user/workCodeEdit?id=${workCode.id}&userId=${user.id}">工号编辑</a></li>
</ul><br/>
<form:form id="inputForm" modelAttribute="workCode" action="${ctx}/sys/user/saveWorkCode" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="user.id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">工 号:</label>
        <div class="controls">
            <input id="oldWorkCode" name="oldWorkCode" type="hidden" value="${workCode.workCode}">
            <form:input path="workCode" htmlEscape="false" maxlength="50" class="required workCode"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">角色类型:</label>
        <div class="controls">
            <form:select path="roleType" class="input-medium" id="roleType" onchange="initRoleTree()">
                <form:option value="">请选择...</form:option>
                <form:options items="${fns:getDictList('role_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <span class="help-inline"><font color="red">*</font> </span>
    </div>
    <%--<div class="control-group">
        <label class="control-label">角 色:</label>
        <div class="controls">
            <div id="roleTree" class="ztree" style="margin-top:3px;float:left;"></div>
            <form:hidden path="roleIdList"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>--%>
    <div class="control-group">
        <label class="control-label">备注:</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
        </div>
    </div>
    <div class="form-actions">
        <input class="btn" type="submit" value="提 交"/>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>