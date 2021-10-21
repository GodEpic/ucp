<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>数据字典管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<style>
    .margin-top{margin-top: 15px}
    .margin-left{margin-left: 15px}
    .margin-left-27{margin-left: 27px}
</style>
<ul class="nav nav-tabs">
    　
    <a href="#" onclick="addDict();">数据字典管理添加</a>
    　
</ul>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <tr>
        <th>名称</th>
        <th>值</th>
        <th>排序</th>
        <th>描述</th>
        <th>默认值</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${list}" var="dict">
        <tr>
            <td>${dict.label}</td>
            <td>${dict.value}</td>
            <td>${dict.sort}</td>
            <td>${dict.description}</td>
            <td><c:if test="${dict.isSelect==1}">是</c:if><c:if test="${dict.isSelect==0}">否</c:if></td>
            <td>
                <a id="${dict.id}" sort="${dict.sort}" label="${dict.label}" description="${dict.description}" value="${dict.value}" isSelect="${dict.isSelect}" onclick="editDict(this)">修改</a>
                <a onclick="deleteDict(this)" id="${dict.id}">删除</a>
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
    function addDict() {
//        if (treeNodeId == "" || typeof (treeNodeId) == 'undefined') {
//            Common.myAlert("请选择目录");
//            return false;
//        }
//        if (treeNodeId == "0") {
//            Common.myAlert("根目录不能新增");
//            return false;
//        }
        $.ajax({
            type: "post",
            data: {parentId: treeNodeId},
            url: "${ctx}/sys/dict/addDict",
            success: function (msg) {
                if (msg.obj) {
                    var html = "";
                    html += "<div class=\"sec-form\"><li>";
                    html += "<label>名称</label><input name=\"label\" class=\"margin-left\"/>";
                    html += "</li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>值</label>";
                    html += "<input name=\"value\"  class=\"margin-left-27\"/></li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>排序</label>";
                    html += "<input name=\"sort\"  class=\"margin-left\"/ value='"+(document.getElementById("contentTable").rows.length-1)*10+"'></li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>描述</label>";
                    html += "<input name=\"description\"  class=\"margin-left\"/ ></li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>值</label>";
                    html += "<input name=\"select\" type=\"radio\" value=\"0\" checked class=\"margin-left-27\"/>否";
                    html += "<input name=\"select\" type=\"radio\" value=\"1\" class=\"margin-left-27\"/>是";
                    html += "</li></div>";
                    $.Dialog.load({
                        dialogHtml: html,
                        title: "新增",
                        btnok: "确定",
                        btncl: "取消",
                        okCallBack: function () {
                            var value = $("input[name='value']").val();
                            var name = $("input[name='label']").val();
                            var sort = $("input[name='sort']").val();
                            var description = $("input[name='description']").val();
                            console.log(sort)
                            var select = $("input[name='select']:checked").val();
                            if ($.trim(name) == '') {
                                Common.myAlert("名称不能为空");
                                return false;
                            }
                            if ($.trim(value) == '') {
                                Common.myAlert("值不能为空");
                                return false;
                            }
                            $.ajax({
                                type: "post",
                                data: {label: name, value: value,sort:sort, description:description,parentId: treeNodeId, type: 2,isSelect:select},
                                url: "${ctx}/sys/dict/saveDict",
                                success: function (msg) {
                                    debugger;
                                    if (msg.success) {
                                        $(window.parent.document).find('#dictContent').attr("src", "${ctx}/sys/dict/listByType?type=2&parentId=" + treeNodeId);
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
    function editDict(obj) {
        var html = "";
        html += "<div class=\"sec-form\"><li>";
        html += "<label>名称</label><input name=\"label\" class=\"margin-left\" value=\"" + $(obj).attr("label") + "\"/>";
        html += "</li></div>";
        html += "<div class=\"sec-form margin-top\"><li>";
        html += "<label>值</label>";
        html += "<input name=\"value\" class=\"margin-left-27\"  value=\"" + $(obj).attr("value") + "\" /></li></div>";
        html += "<div class=\"sec-form margin-top\"><li>";
        html += "<label>排序</label>";
        html += "<input name=\"sort\"  class=\"margin-left\"/ value=\"" + $(obj).attr("sort") + "\" /></li></div>";
        html += "<div class=\"sec-form margin-top\"><li>";
        html += "<label>描述</label>";
        html += "<input name=\"description\"  class=\"margin-left\"/ value=\"" + $(obj).attr("description") + "\" /></li></div>";
        html += "<div class=\"sec-form margin-top\"><li>";
        html += "<label>默认值</label>";
        if($(obj).attr("isSelect")==0){
            html += "<input name=\"select\" type=\"radio\" value=\"0\" checked class=\"margin-left-27\"/>否";
            html += "<input name=\"select\" type=\"radio\" value=\"1\" class=\"margin-left-27\"/>是";
        }else{
            html += "<input name=\"select\" type=\"radio\" value=\"0\"  class=\"margin-left-27\"/>否";
            html += "<input name=\"select\" type=\"radio\" value=\"1\" checked class=\"margin-left-27\"/>是";
        }
        html += "</li></div>";
        $.Dialog.load({
            dialogHtml: html,
            title: "编辑",
            btnok: "确定",
            btncl: "取消",
            okCallBack: function () {
                var value = $("input[name='value']").val();
                var name = $("input[name='label']").val();
                var sort = $("input[name='sort']").val();
                var description = $("input[name='description']").val();
                var select = $("input[name='select']:checked").val();
                if ($.trim(name) == '') {
                    Common.myAlert("名称不能为空");
                    return false;
                }
                if ($.trim(value) == '') {
                    Common.myAlert("值不能为空");
                    return false;
                }
                $.ajax({
                    type: "post",
                    data: {label: name, value: value, sort:sort,description:description,id: $(obj).attr("id"),isSelect:select},
                    url: "${ctx}/sys/dict/updateDict",
                    success: function (msg) {
                        if (msg.success) {
                            $(window.parent.document).find('#dictContent').attr("src", "${ctx}/sys/dict/listByType?type=2&parentId=" + treeNodeId);
                        } else {
                            Common.myAlert(msg.message);
                        }
                    }
                });
            }
        });
    }
    function deleteDict(obj) {
        $.ajax({
            type: "post",
            data: {id: $(obj).attr("id")},
            url: "${ctx}/sys/dict/delete",
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

