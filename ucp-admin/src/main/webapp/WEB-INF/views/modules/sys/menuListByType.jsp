<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>数据字典管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<style>
    .margin-top {
        margin-top: 15px
    }

    .margin-left {
        margin-left: 15px
    }

    .margin-left-27 {
        margin-left: 27px
    }
</style>
<ul class="nav nav-tabs">
    　
    <li onclick="addMenu()">状态管理添加</li>
    　
</ul>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <tr>
        <th>名称</th>
        <th>值</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${list}" var="menu">
        <tr>
            <td>${menu.name}</td>
            <td>${menu.href}</td>
            <td>
                <a id="${menu.id}" name="${menu.name}" value="${menu.href}" onclick="editMenu(this)">修改</a>
                <a onclick="deleteMenu(this)" id="${menu.id}">删除</a>
            </td>
            　
        </tr>
    </c:forEach>
</table>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script>
    $.getParam = function (property) {
        var paramStr = location.href.split("?")[1];
        var params = [];
        if (paramStr != null && paramStr.indexOf("=") >= 0) {

            var combo = paramStr.split("&");
            for (var i = 0; i < combo.length; i++) {
                var cParam = combo[i].split('=');
                params[cParam[0]] = cParam[1];
            }
        }
        return params[property];
    };
    var treeNodeId = $.getParam("parentId");
    function addMenu() {
        if (treeNodeId == "" || typeof (treeNodeId) == 'undefined') {
            Common.myAlert("请选择目录");
            return false;
        }
        $.ajax({
            type: "post",
            data: {parentId: treeNodeId},
            url: "${ctx}/sys/menu/addMenu",
            success: function (msg) {
                if (msg.obj) {
                    var html = "";
                    html += "<div class=\"sec-form\"><li>";
                    html += "<label>名称</label><input name=\"name\" class=\"margin-left\"/>";
                    html += "</li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>值</label>";
                    html += "<input name=\"href\"  class=\"margin-left-27\"/></li></div>";
                    $.Dialog.load({
                        dialogHtml: html,
                        title: "新增",
                        btnok: "确定",
                        btncl: "取消",
                        okCallBack: function () {
                            var href = $("input[name='href']").val();
                            var name = $("input[name='name']").val();
                            if ($.trim(name) == '') {
                                Common.myAlert("名称不能为空");
                                return false;
                            }
                            if ($.trim(href) == '') {
                                Common.myAlert("值不能为空");
                                return false;
                            }
                            var param = {name: name, href: href, parent: {id: treeNodeId}, menuType: 3};
                            $.ajax({
                                type: "post",
                                dataType: 'json',
                                contentType: 'application/json',
                                data: JSON.stringify(param),
                                url: "${ctx}/sys/menu/saveMenu",
                                success: function (msg) {
                                    debugger;
                                    if (msg.success) {
                                        $(window.parent.document).find('#menuContent').attr("src", "${ctx}/sys/menu/listByType?type=3&parentId=" + treeNodeId);
                                    } else {
                                        Common.myAlert(msg.message);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Common.myAlert("有子节点不能新增");
                }
            }
        });
    }
    function editMenu(obj) {

        var html = "";
        html += "<div class=\"sec-form\"><li>";
        html += "<label>名称</label><input name=\"name\" class=\"margin-left\" value=\"" + $(obj).attr("name") + "\"/>";
        html += "</li></div>";
        html += "<div class=\"sec-form margin-top\"><li>";
        html += "<label>值</label>";
        html += "<input name=\"href\" class=\"margin-left-27\"  value=\"" + $(obj).attr("value") + "\" /></li></div>";
        $.Dialog.load({
            dialogHtml: html,
            title: "编辑",
            btnok: "确定",
            btncl: "取消",
            okCallBack: function () {
                var href = $("input[name='href']").val();
                var name = $("input[name='name']").val();
                if ($.trim(name) == '') {
                    Common.myAlert("名称不能为空");
                    return false;
                }
                if ($.trim(href) == '') {
                    Common.myAlert("值不能为空");
                    return false;
                }
                $.ajax({
                    type: "post",
                    data: {name: name, href: href, id: $(obj).attr("id")},
                    url: "${ctx}/sys/menu/updateMenu",
                    success: function (msg) {
                        if (msg.success) {
                            $(window.parent.document).find('#menuContent').attr("src", "${ctx}/sys/menu/listByType?type=3&parentId=" + treeNodeId);
                        } else {
                            Common.myAlert(msg.message);
                        }
                    }
                });
            }
        });
    }
    function deleteMenu(obj) {
        $.ajax({
            type: "get",
            data: {id: $(obj).attr("id")},
            url: "${ctx}/sys/menu/delete",
            success: function (msg) {
                if (msg.success) {
                    debugger;
                    $(obj).parent().parent().remove();
                } else {
                    Common.myAlert(msg.message);
                }
            }
        });
    }
</script>
</body>
</html>

