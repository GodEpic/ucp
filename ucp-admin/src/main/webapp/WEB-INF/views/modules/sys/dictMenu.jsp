]
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>数据字典管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<style>
    .margin-top{margin-top: 15px}
    .margin-left{margin-left: 15px}
    .margin-left-27{margin-left: 27px}
</style>
<sys:message content="${message}"/>
<div id="content" class="row-fluid">
    <div id="left" class="accordion-group">

        <div id="ztree" style="overflow-y: auto"class="ztree"></div>
    </div>
    <div id="openClose" class="close">&nbsp;</div>
    <div id="right">
        <iframe id="dictContent" src="${ctx}/sys/dict/listByType?type=2&parentId=" width="100%" height="91%" frameborder="0"></iframe>
    </div>
</div>
 
<script type="text/javascript">
    var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        edit: {
            enable: true,
//给节点额外增加属性来控制“重命名”、“删除”图标的显示或隐藏
            showRenameBtn: showRenameBtn,
            showRemoveBtn: showRemoveBtn
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeRemove: beforeRemove,//点击删除时触发，用来提示用户是否确定删除
            beforeEditName: beforeEditName,
            onRename: onRename,
            onClick:function(event, treeId, treeNode){
                $('#dictContent').attr("src","${ctx}/sys/dict/listByType?type=2&parentId="+treeNode.id);
            }
        }
    };
    var log, className = "dark";

    function beforeEditName(treeId, treeNode) {
        $.ajax({
            type: "post",
            data: { id: treeNode.id},
            url: "${ctx}/sys/dict/getDict",
            success: function (msg) {
                if (msg.success) {
                    var html = "";
                    html += "<div class=\"sec-form\"><li>";
                    html += "<label>名称</label><input name=\"label\" class=\"margin-left\" value=\""+msg.obj.label+"\"/>";
                    html += "</li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>值</label>";
                    html += "<input name=\"value\"  class=\"margin-left-27\" value=\""+msg.obj.value+"\"/></li></div>";
                    html += "<div class=\"sec-form margin-top\"><li>";
                    html += "<label>默认值</label>";
                    if(msg.obj.isSelect==0){
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
                                data: {label: name, value: value, id: treeNode.id,isSelect:select},
                                url: "${ctx}/sys/dict/updateDict",
                                success: function (msg) {
                                    $('.modal ').remove();
                                    $('.modal-backdrop ').remove();
                                    if (msg.success) {
                                        refresh();
                                    } else {
                                        Common.myAlert(msg.message);
                                    }
                                }
                            });
                        }
                    });
                    return false;
                } else {
                    Common.myAlert(msg.message);
                }
            }
        });

    };

    function onRename(e, treeId, treeNode) {
        showLog("[ " + getTime() + " onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    };
    function showLog(str) {
        if (!log) log = $("#log");
        log.append("<li class='" + className + "'>" + str + "</li>");
        if (log.children("li").length > 8) {
            log.get(0).removeChild(log.children("li")[0]);
        }
    };
    function getTime() {
        var now = new Date(),
                h = now.getHours(),
                m = now.getMinutes(),
                s = now.getSeconds(),
                ms = now.getMilliseconds();
        return (h + ":" + m + ":" + s + " " + ms);
    };
    //是否显示编辑按钮
    function showRenameBtn(treeId, treeNode) {
//获取节点所配置的noEditBtn属性值
        if (treeNode.noEditBtn != undefined && treeNode.noEditBtn) {
            return false;
        } else {
            return true;
        }
    };
    //是否显示删除按钮
    function showRemoveBtn(treeId, treeNode) {
        //获取节点所配置的noRemoveBtn属性值
        if (treeNode.noRemoveBtn != undefined && treeNode.noRemoveBtn) {
            return false;
        } else {
            return true;
        }
    };
    function beforeRemove(treeId, treeNode) {
        console.log(treeId);
        if(treeNode.isParent){
            $.alert("warning","请先删除子节点");
            return false;
        }
        var isDelete=confirm("你确定要删除吗？");
        if(isDelete==true){
            $.ajax({
                type: "get",
                async:false,
                url: "${ctx}/sys/dict/delete",
                data: {id:treeNode.id},
                success: function (result) {
                    debugger;
                    if (result.success) {
                        var sObj = $("#" + treeNode.tId + "");
                        sObj.remove();
                        Common.myAlert("操作成功");
                    } else {
                        Common.myAlert(result.message);
                    }
                }
            });

        }
        return false
    }
    function addHoverDom(treeId, treeNode) {
//在addHoverDom中判断第0级的节点不要显示“新增”按钮
//        if (treeNode.level === 0) {
//            return false;
//        } else {
            //给节点添加"新增"按钮
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.id
                    + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_" + treeNode.id);
            if (btn) btn.bind("click", function () {
                var html = "";
                html += "<div class=\"sec-form\"><li>";
                html += "<label>名称</label><input name=\"label\" class=\"margin-left\"/>";
                html += "</li></div>";
                html += "<div class=\"sec-form margin-top\"><li>";
                html += "<label>值</label>";
                html += "<input name=\"value\" class=\"margin-left-27\"/></li></div>";
                html += "<div class=\"sec-form margin-top\"><li>";
                html += "<label>默认值</label>";
                html += "<input name=\"select\" type=\"radio\" value=\"0\" checked class=\"margin-left-27\"/>否";
                html += "<input name=\"select\" type=\"radio\" value=\"1\" class=\"margin-left-27\"/>是";
                html += "</li></div>";
                $.Dialog.load({
                    dialogHtml: html,
                    title: "编辑",
                    btnok: "确定",
                    btncl: "取消",
                    okCallBack: function () {

                        var value = $("input[name='value']").val();
                        var name = $("input[name='label']").val();
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
                            data: {label: name, value: value, parentId: treeNode.id,type:1,isSelect:select},
                            url: "${ctx}/sys/dict/saveDict",
                            success: function (msg) {
                                if (msg.success) {
                                    $('.modal ').remove();
                                    $('.modal-backdrop ').remove();
                                    refresh();
                                } else {
                                    Common.myAlert(msg.message);
                                }
                            }
                        });
                    }
                });
            });
//        }
    };
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_" + treeNode.id).unbind().remove();
    };
    function selectAll() {
        var zTree = $.fn.zTree.getZTreeObj("ztree");
        zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
    };
    refresh();
    function refresh() {
        $.getJSON("${ctx}/sys/dict/treeData", function (data) {
            $.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
            $("#selectAll").bind("click", selectAll);
        });
    };


    var leftWidth = 400; // 左侧窗口大小
    var htmlObj = $("html"), mainObj = $("#main");
    var frameObj = $("#left, #openClose, #right, #right iframe");
    function wSize(){
        var strs = getWindowSize().toString().split(",");
        htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});
        mainObj.css("width","auto");
        frameObj.height(strs[0] - 5);
        var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
        $("#right").width($("#content").width()- leftWidth - $("#openClose").width() -5);
        $(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
    }
</script>
<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>
