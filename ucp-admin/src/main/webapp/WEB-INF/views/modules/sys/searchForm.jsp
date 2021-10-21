<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>信息查询管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#name").focus();
            $("#inputForm").validate({

                submitHandler: function (form) {
                    var index = 0;
                    var ids = [], nodes = tree.getCheckedNodes(true);
                    for (var i = 0; i < nodes.length; i++) {

                        ids.push(nodes[i].id);
                    }
                    $("#linkIds").val(ids);
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

            var defaultSetting = {
                check: {enable: true}, view: {selectedMulti: false},
                data: {simpleData: {enable: true}}, callback: {
                    beforeClick: function (id, node) {
                        tree.checkNode(node, !node.checked, true, true);
                        return false;
                    }
                }
            };
            // 用户-菜单
            var zNodes = [
                    <c:forEach items="${functionLinks}" var="link">{
                    id: "${link.id}",
                    pId: "0",
                    name: "${link.funTitle}"
                },
                </c:forEach>];
            debugger;
            // 初始化树结构
            var tree = $.fn.zTree.init($("#menuTree"), defaultSetting, zNodes);
            // 不选择父节点
            tree.setting.check.chkboxType = {"Y": "ps", "N": "s"};
            // 默认选择节点
            var ids = "${linkIds}".split(",");
            for (var i = 0; i < ids.length; i++) {
                var node = tree.getNodeByParam("id", ids[i]);
                try {
                    tree.checkNode(node, true, false);
                } catch (e) {
                }
            }
            // 默认展开全部节点
            tree.expandAll(true);
        })


    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/menu/findParentMenus">菜单列表</a></li>
    <li class="active"><a href="${ctx}/sys/menu/form?id=${menu.id}">菜单编辑</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>

    <div class="control-group">
        <label class="control-label">业务线名称:</label>

        <div class="controls">
            <input readonly value="${menu.name}">

        </div>
    </div>

    <div class="control-group">
        <label class="control-label">接口勾选:</label>

        <div class="controls">
            <div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
            <form:hidden path="linkIds"/>

        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>