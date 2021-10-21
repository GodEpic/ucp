<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>

<html lang="en">
<head lang="en">
    <title>L2检查-任务</title>

    <link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912"/>
    <link rel="stylesheet" href="${static.resource.url}/js/plugs/layer/theme/default/layer.css"/>
    <link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
    <style type="text/css">

        .layui-form-item{
            display: inline-block;
            float: none;
        }

        .layui-input-block{
            line-height: 36px;
            margin-left: 150px;
        }

        .layui-form-label{
            width: 120px;
        }

        .import-desc{
            font-size: 16px;
            color: red;
            font-weight: bolder;
            display: inline-block;
            background-color: #eee;
            line-height: 30px;
            padding: 0 20px;
            vertical-align: bottom;
        }

        .import-prew{
            margin: 15px 0;
        }
    </style>
</head>
<body>
<div class="tab-con sec-case-form">
    <div class="act-title">${act.activityName}-${task.version}</div>
    <div class="sec-case-forms">
        <div class="sec">
            <article class="sec-title t2">活动基本信息</article>
            <div class="sec-form layui-row">
                <div class="layui-col-xs10 layui-form-item">
                    <label class="layui-form-label layui-form-title">标题：</label>
                    <div class="layui-input-block">
                        ${act.summary}
                    </div>
                </div>
            </div>

            <div class="sec-form layui-row">
                <div class="layui-col-xs10 layui-form-item">
                    <label class="layui-form-label layui-form-title">活动名：</label>
                    <div class="layui-input-block">
                        ${act.activityName}
                    </div>
                </div>
            </div>

            <div class="sec-form layui-row">
                <div class="layui-col-xs5 layui-form-item">
                    <label class="layui-form-label layui-form-title">活动创建人：</label>
                    <div class="layui-input-block">
                        ${fns:getUserById(act.createBy).name}
                    </div>
                </div>
                <div class="layui-col-xs5 layui-form-item">
                    <label class="layui-form-label layui-form-title">L1经办人：</label>
                    <div class="layui-input-block">
                        ${fns:getUserNameByWorkCode(taskReceiveL1.receive)}
                    </div>
                </div>
            </div>

            <div class="sec-form layui-row">
                <div class="layui-col-xs10 layui-form-item">
                    <label class="layui-form-label layui-form-title">注意事项：</label>
                    <div class="layui-input-block">
                        ${fns:replaceEnter(act.description)}
                    </div>
                </div>
            </div>
        </div>
        <div class="sec">
            <article class="sec-title t2">券信息</article>

            <c:choose>
                <c:when test="${not empty modRowLists}">
                    <div class="sec-form">
                        <div class="sec-import">
                            <div class="import-prew">
                                <button id="prewImportData" type="button">预览导入信息</button>

                                <div class="import-desc">已导入<span id="importDataSize">${couponCount}</span>条信息</div>
                            </div>
                        </div>
                    </div>

                    <div class="sec-form">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>查看详情</th>
                                <th>检查情况</th>
                                <th>具体项-配券中心</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${modRowLists}" var="rw" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td><a href="javascript:void(0)" class="row-show" data-value="${rw.configCertifiCenter}"
                                           data-rowid="${rw.id}">查看</a></td>
                                    <td>
                                        <div id="${rw.id}">${fns:getModuleActRowStatus(rw.checkStatus)}</div>
                                    </td>
                                    <td class="${rw.change eq '1' ? 'value-change' : ''}">${rw.configCertifiCenter}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="import-desc">此任务没有券信息。</div>
                </c:otherwise>
            </c:choose>

            <div class="sec-form layui-row"> 
                <article class="sec-title t2">图片和附件：</article>
				<div class="sec-form">
					<div class="sec-tbl layui-module-row">
						<table class="layui-table" id="actFileTab" lay-filter="actFileTab"></table>
					</div>
				</div>
               
            </div>


            <div class="sec-form">
                <article class="sec-title t2">券图</article>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>图片链接</th>
                        <th>图片说明</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${couponImages}" var="cp" varStatus="status">
                        <tr>
                            <th>${status.index + 1}</th>
                            <th>${cp.couponLink}</th>
                            <th>${fns:replaceEnter(cp.couponDesc)}</th>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="sec">
            <div class="sec-btn">
                <c:if test="${taskReceive.receive eq agent && fns:getDictLabel(task.verifyStatus, 'verify_status', '') ne '已完成'}">
                    <div class="sec-btn-submit"><a id="submitBtn" href="javascript:void(0)" class="layui-yum-btn">检查完成</a></div>
                </c:if>
                <div class="sec-btn-close"><a id="closeBtn" href="javascript:void(0)" class="layui-yum-btn">关闭</a></div>
            </div>
        </div>
    </div>
</div>


<script type="text/html" id="uploadImgTemp">
    <div class="layui-item-value layui-label-color">
        <div class="layui-upload-list layui-images">
            <div class="layer-photos attach-view {{d.tempId}}" prev-class="{{d.tempId}}">
                {{#  layui.each(d.images, function(index, item){ }}
                <div class="layui-upload-item">
                    <img alt="{{item.originalFileName}}" src="{{item.filePath}}" class="layui-upload-img">
                </div>
                {{#  }); }}
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="uploadAttachTemp">
    <div class="layui-item-value layui-label-color">
        <div class="layui-upload-list layui-attach">
            <div class="layer-photos attach-view">
                {{#  layui.each(d.attach, function(index, item){ }}
                <div class="layui-upload-item">
                    <div title="点击下载"><a href="javascript:void(0)" data-path="{{item.filePath}}" data-orgName="{{item.originalFileName}}" data-fileName="{{item.fileName}}" onclick="downloadFtpAttach(this)">{{item.originalFileName}}</a></div>
                </div>
                {{#  }); }}
            </div>
        </div>
    </div>
</script>


<%@include file="/WEB-INF/views/include/footer.jsp" %>
<script src="${static.resource.url}/js/plugs/layer/layer.js"></script>
<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
<script type="text/javascript">
    $(function () {
        $("#addCommentBtn").click(function () {
            var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
            var html = document.getElementById("addCommentTemp").innerHTML;
            var createDate = new Date().format('yyyy-MM-dd hh:mm');
            var source = html.replace(reg, function (node, key) {
                return {'userName': '${userName}', 'createDate': createDate}[key];
            });
            $("#comment_find").append(source);
            commentRemove();
            commentSave();
        });

        $("#prewImportData").click(function(e){
            e.preventDefault();
            var width = $(window).width(),
                height = $(window).height();
            var index = layer.open({
                title:"预览导入信息",
                type: 2,
                shade: 0,
                area: [width * 0.8, height * 0.8],
                maxmin: true,
                content: '${ctx}/task/check/prewImportData?id=${task.id}'
            });
            layer.full(index);
        });
    });

    $("#submitBtn").click(function () {
        layer.confirm("确定检查完成了吗？", function(index) {
            var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
            $.ajax({
                url: "${ctx}/task/check/updateTaskStatus",
                type: "POST",
                data: {
                    "id" : "${task.id}"
                },
                dataType : "JSON",
                success: function (data) {
                    layer.close(mask);
                    if(data.success){
                        layer.msg("操作成功");
                        setTimeout(function(){
                            window.parent.close();
                        },3000);
                    } else {
                        layer.msg(data.message);
                    }
                }
            });
            layer.close(index);
        });
    });

    $("#closeBtn").click(function () {
        window.close();
    });

    $(".row-show").click(function () {
        var row = $(this);
        layer.open({
            title: "具体项-配券中心：" + row.attr("data-value"),
            type: 2,
            shade: 0,
            zIndex: layer.zIndex,
            area: ['80%', '80%'],
            maxmin: true,
            content: '${ctx}/task/moduleActRow/rowDetail?id=' + row.attr("data-rowid") + '&type=${checkType}' + '&taskId=${task.id}'
        });
    });

    function commentSave() {
        $(".comment-save").click(function () {
            var _t = $(this),
                tr = _t.parent().parent(),
                input = tr.find('input'),
                td = input.parent(),
                body = input.val();

            if (body.length == 0) {
                input.focus();
                layer.msg("请输入反馈内容");
                return false;
            }

            $.ajax({
                url: "${ctx}/common/addComment",
                type: "POST",
                data: {
                    "comment": body,
                    "key": "${task.jiraNo}"
                },
                dataType: "JSON",
                success: function (data) {
                    var obj = data.obj;
                    if (obj.errcode === '0') {
                        var html = '<div class="comment-body">' + body + '</div>';
                        input.remove();
                        td.append(html);
                        _t.remove();
                        tr.attr('data-uri', obj.commonUri);
                    } else {
                        layer.msg('添加反馈失败');
                    }
                }
            });
        });
    }

    function commentRemove() {
        $(".comment-remove").click(function () {
            var tr = $(this).parent().parent();
            var dataUri = tr.attr('data-uri');
            if (dataUri) {
                $.ajax({
                    url: "${ctx}/common/deleteComment",
                    type: "POST",
                    data: {
                        "uri": dataUri
                    },
                    dataType: "JSON",
                    success: function (data) {
                        if (data.obj.success) {
                            tr.remove();
                        } else {
                            Common.myAlert(data.obj.message);
                        }
                    }
                });
            } else {
                layer.msg('删除反馈失败');
            }
        });
    }



    var actFileData = '${actFileData}';
            actFileData = actFileData.replace(/\\/g, "\\\\");
            actFileData = actFileData.replace(/\n/g,"").replace(/\r/g,"");
            if(actFileData && actFileData.length > 0){
                try{
                    actFileData = JSON.parse(actFileData);
                }catch (e) {
                    actFileData = eval('('+couponImages+')');
                }
            } else {
                actFileData = new Array();
            }
            layui.use(["laytpl", 'table','element', 'upload', 'form', 'layer'], function(){
                var	laytpl = layui.laytpl,
                    table = layui.table;
                var layTableId = "actFileTab";

                table.render({
                    elem : '#' + layTableId,
                    id : layTableId,
                    page : false,
                    data : actFileData,
                    cols : [ [{
                        title : '序号',
                        type : 'numbers',
						unresize:true,
                        width:80
                    }, {
                        field: 'images',
                        title: '图片',
                        unresize:true,
                        templet: function(d) {
                            var content = "";
                            laytpl(uploadImgTemp.innerHTML).render(d, function(html){
                                content = html;
                            });
                            return content;
                        }
                    }, {
                        field: 'attach',
                        title: '附件',
                        unresize:true,
                        templet: function(d) {
                            var content = "";
                            laytpl(uploadAttachTemp.innerHTML).render(d, function(html){
                                content = html;
                            });
                            return content;
                        }
                    }, {
                        field: 'remarks',
                        title: '备注',
                        edit: 'textarea',
                        width:300,
                        unresize:true,
                        templet: function(d) {
                            return "<div class='layui-cell-text'>" + (d.remarks.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
                        }
                    }] ],
                    done : function(res, curr, count){
                        $(".layui-table-body .layui-images .attach-view").each(function(){
                            var rowId = "." + $(this).attr("prev-class");
                            layer.photos({photos: rowId, anim: 5});
                        });
                    }
                });

            });

            function downloadFtpAttach(_t){
                var filePath = $(_t).attr("data-path"),
					originalFileName = $(_t).attr("data-orgName"),
                    fileName = $(_t).attr("data-fileName");

                var baseDir = filePath.replace(fileName, "");
                	baseDir = baseDir.replace("${filePath}","");
                window.location.href= encodeURI("/ucp/common/downloadUrgentFtpAttach?originalFileName=" + originalFileName + "&fileName=" + fileName + "&baseDir=.." + baseDir);
            }
</script>

<script type="text/html" id="addCommentTemp">
    <tr class="comment-add">
        <td>
            <input class="comment-body" type="text" name="comment" value=""/>
        </td>
        <td>
            <div class="comment-user">[userName]</div>
        </td>
        <td>
            <div class="comment-date">[createDate]</div>
        </td>
        <td>
            <a href="javascript:void(0)" class="comment-save btn btn-primary btn-small">保存</a>
            <a href="javascript:void(0)" class="comment-remove btn btn-danger btn-small">删除</a>
        </td>
    </tr>
</script>
</body>
</html>
