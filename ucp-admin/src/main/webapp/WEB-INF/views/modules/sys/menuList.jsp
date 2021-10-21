]
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>状态管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>

<sys:message content="${message}"/>
<div id="content" class="row-fluid">
    <div id="left" class="accordion-group">

        <div id="ztree"  style="overflow-y: auto" class="ztree"></div>
    </div>
    <div id="openClose" class="close">&nbsp;</div>
    <div id="right">
        <iframe id="menuContent" src="${ctx}/sys/menu/listByType?type=3&parentId=" width="100%" height="91%" frameborder="0"></iframe>
    </div>
</div>
 
<script type="text/javascript">
    var setting = {
        view: {
            selectedMulti: false
        },

        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick:function(event, treeId, treeNode){
                $('#menuContent').attr("src","${ctx}/sys/menu/listByType?type=3&parentId="+treeNode.id);
            }
        }
    };
    var log, className = "dark";

    function selectAll() {
        var zTree = $.fn.zTree.getZTreeObj("ztree");
        zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
    };
    refresh();
    function refresh() {
        $.getJSON("${ctx}/sys/menu/treeData", function (data) {
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
