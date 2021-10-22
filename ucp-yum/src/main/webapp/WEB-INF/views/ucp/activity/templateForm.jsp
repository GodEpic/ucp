<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<%@ page import="com.yum.ucp.modules.activity.entity.Activity" %>
<%@ page import="com.yum.ucp.modules.sys.utils.DictUtils" %>
<%@ page import="com.yum.ucp.modules.sys.entity.Dict" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.yum.ucp.common.utils.SnowFlakeUtils" %>
<html lang="en">
<head>
    <title>普通活动</title>
    <style>
        #title {
            width: 700px;
        }

        .importTable td {
            border: 1px solid #ddd;
            padding: 5px 15px;
        }

        .sec-import div {
            display: inline-block;
            margin: 5px 0;
        }

        .sec-form li {
            width: 45%;
        }

        .sec-form textarea {
            width: 780px;
            padding: 10px;
        }

        .sec-desc {
            width: 92%;
            display: inline-block;
        }

        .sec-desc li {
            width: 100%;
        }

        .sec-form li label.error {
            left: auto;
            right: 0;
            margin: 0;
            display: inline-block;
        }

        .import-desc {
            font-size: 16px;
            color: red;
            font-weight: bolder;
        }

        .layui-form-radio i {
            top: 0;
            width: 16px;
            height: 16px;
            line-height: 16px;
            border: 1px solid #d2d2d2;
            font-size: 12px;
            border-radius: 2px;
            background-color: #fff;
            color: #fff !important;
        }

        .layui-form-radioed i {
            position: relative;
            width: 18px;
            height: 18px;
            border-style: solid;
            background-color: #6A8BF5;
            color: #6A8BF5 !important;
        }

        /* 使用伪类画选中的对号 */
        .layui-form-radioed i::after, .layui-form-radioed i::before {
            content: "";
            position: absolute;
            top: 8px;
            left: 5px;
            display: block;
            width: 12px;
            height: 2px;
            border-radius: 4px;
            background-color: #fff;
            -webkit-transform: rotate(-45deg);
            transform: rotate(-45deg);
        }

        .layui-form-radioed i::before {
            position: absolute;
            top: 10px;
            left: 2px;
            width: 7px;
            transform: rotate(-135deg);
        }


        .layui-upload-attach {
            text-decoration: underline !important;
        }

        .layui-attach .layui-upload-item, .layui-attach .upload_btn {
            text-align: center;
            display: block;
        }

        .layui-attach .layui-upload-item {
            width: fit-content;
            width: -webkit-fit-content;
            width: -moz-fit-content;
            margin: 15px auto;
        }

        .layui-attach .layui-upload-item div {
            padding: 0px 10px;
            border: 1px solid #DDD;
            border-radius: 6px;
            background-color: #DDD;
            color: #000;
        }

        .layui-upload-img-del {
            cursor: pointer;
        }

        .layui-border {
            border: 1px solid red;
        }


    </style>
</head>

<%--<link rel="stylesheet" href="http://172.25.221.188:8081/static/js/plugs/layui-v2.5.4/css/layui.css">--%>
<%--<link rel="stylesheet" href="http://172.25.221.188:8081/static/css/activity.css">--%>
<%--<link rel="stylesheet" href="http://172.25.221.188:8081/static/css/qa.reject.css"/>--%>


<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
<link rel="stylesheet" href="${static.resource.url}/css/activity.css">
<link rel="stylesheet" href="${static.resource.url}/css/qa.reject.css"/>

<body>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String notifyActivityId = request.getParameter("notifyId");//用request得到
    Activity activity = (Activity) request.getAttribute("activity");
    String notifyActivityNo = "";

    if (activity != null && StringUtils.isEmpty(notifyActivityId)) {
        notifyActivityId = activity.getNotifyActivityId();
    }
    if (activity != null && StringUtils.isNotEmpty(notifyActivityId)) {
        //  notifyActivityNo = request.getParameter("notifyActivityNo");
        //  activity.setNotifyActivityNo(notifyActivityNo);
        String brand = request.getParameter("brand");
        List<Dict> list = DictUtils.getDictList("brand");
        if (list != null) {
            for (Dict dict : list) {
                if (dict.getLabel().equals(brand)) {
                    activity.setBrand(dict.getValue());
                }
            }
        }
    } else {
        //notifyActivityNo="DSC"+SnowFlakeUtils.getNextId();
    }


%>
<div class="tab-con case-tab-top input-style">
    <div id="case">
        <div class="">
            <form id="templateForm" class="layui-form">
                <div class="sec">
                    <%--                    <input name="notifyActivityId" id="notifyActivityId" type="hidden" value="<%=notifyActivityId%>"/>--%>
                    <article class="sec-title t2">模板基本信息</article>
                    <div class="sec-form">
                        <li>
                            <label><b class="label">*</b>模板名称：</label>
                            <input type="text" name="templateName" id="summary" value="${template.templateName}"
                                   required>
                        </li>
                        <li>
                            <label><b class="label">*</b>品牌：</label>
                            <select name="brand" required>
                                <c:forEach items="${fns:getDictList('brand')}" var="g">
                                    <option value="${g.value}" title="${g.description}"
                                            <c:if test="${g.value == template.brand}">selected</c:if>>${g.label}</option>
                                </c:forEach>
                            </select>
                        </li>
                    </div>
                    <div class="sec-form">
                        <li>
                            <label><b class="label">*</b>上传模板附件：</label>
                            <input type="file" id="upLoadInput" required>
                            <input type="hidden" id="fileName" name="fileName" value="${template.fileName}">
                            <input type="hidden" id="filePath" name="filePath" value="${template.filePath}">
                        </li>
                        <div class="clear"></div>
                    </div>
                    <div class="sec-form">
                        <li>
                            <label><b class="label">*</b>附件名称 ：</label>
                            <a id="file_a" href="${template.filePath}"
                               title="${template.fileName}">${template.fileName}</a>
                        </li>
                        <div class="clear"></div>
                    </div>
                    <div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
                        <button data-dismiss="alert" class="close">×</button>
                        <label id="loginError" class="error">${message}</label>
                    </div>
                    <div class="form-submit">
                        <div class="btn-container">
                            <!-- 资深员工才能发送任务 -->
                            <%--                            <c:if test="${roleType == 3}">--%>
                            <input type="button" class="templateSave" value="保存模板">
                            <%--                            <input type="button" class="saveAndCreateTask" value="保存并发送任务">--%>
                            <%--                            </c:if>--%>
                            <input id="windowClose" type="button" value="关闭">
                        </div>
                        <div class="ticketGradeSelect gradeSelect"></div>
                        <div class="clear"></div>
                    </div>
                    <%--                <input type="hidden" name="jiraNo" value="${activity.jiraNo}"/>--%>
                    <input type="hidden" name="id" value="${template.id}"/>
                    <%--                <input type="hidden" name="jiraStatus" value="${activity.jiraStatus}"/>--%>
                    <%--                <input type="hidden" name="status" value="${activity.status}"/>--%>
                    <%--                <input type="hidden" name="releaseStatus" value="${activity.releaseStatus}"/>--%>
                    <%--                <input type="hidden" name="couponImages"/>--%>
            </form>
        </div>
    </div>
</div>


<script type="text/html" id="uploadImgTemp">
    <div class="layui-item-value layui-label-color">
        <div class="layui-upload-list layui-images">
            <div class="layer-photos attach-view {{d.tempId}}" prev-class="{{d.tempId}}">
                {{# layui.each(d.images, function(index, item){ }}
                <div class="layui-upload-item">
                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                    <img alt="{{item.originalFileName}}" src="{{item.filePath}}" class="layui-upload-img">
                    <input type="hidden"
                           value='{"fileName":"{{item.fileName}}","filePath":"{{item.filePath}}","originalFileName":"{{item.originalFileName}}","fileSize":{{item.fileSize}},"ext":"{{item.ext}}"}'/>
                </div>
                {{# }); }}
                <div class="upload_btn" data-rowId={{d.tempId}}>
                    <button type="button" class="layui-upload-btn"></button>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="uploadAttachTemp">
    <div class="layui-item-value layui-label-color">
        <div class="layui-upload-list layui-attach">
            <div class="layer-photos attach-view">
                {{# layui.each(d.attach, function(index, item){ }}
                <div class="layui-upload-item">
                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                    <div title="点击下载"><a href="javascript:void(0)" data-path="{{item.filePath}}"
                                         data-orgName="{{item.originalFileName}}" data-fileName="{{item.fileName}}"
                                         onclick="downloadFtpAttach(this)">{{item.originalFileName}}</a></div>
                    <input type="hidden"
                           value='{"fileName":"{{item.fileName}}","filePath":"{{item.filePath}}","originalFileName":"{{item.originalFileName}}","fileSize":{{item.fileSize}},"ext":"{{item.ext}}"}'/>
                </div>
                {{# }); }}
                <div class="upload_btn" data-rowId="{{d.tempId}}" style="width: 60px;margin: auto;">
                    <div><a href="javascript:void(0)" class="layui-upload-attach">上传附件</a></div>
                </div>
            </div>
        </div>
    </div>
</script>


</body>

<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
<%--<script src="http://172.25.221.188:8081/static/js/plugs/layui-v2.5.4/layui.js"></script>--%>


<script src="${ctxStatic}/ucp/task.js?v=20190809"></script>

<script type="text/javascript">
    var ctx = '${ctx}';

    var couponImages = '${couponImages}';
    couponImages = couponImages.replace(/\\/g, "\\\\");
    couponImages = couponImages.replace(/\n/g, "").replace(/\r/g, "");
    if (couponImages && couponImages.length > 0) {
        try {
            couponImages = JSON.parse(couponImages);
        } catch (e) {
            couponImages = eval('(' + couponImages + ')');
        }
    } else {
        couponImages = new Array();
    }
    //准备视图对象
    window.viewObj = {
        tbData: couponImages
    };

    $(function () {
        $("input[name='customName']").blur(function () {
            $("input[name='customName2']").val($(this).val());
        });
        $("input[name='customContact']").blur(function () {
            $("input[name='customPhone2']").val($(this).val());
        });
        $("input[name = 'customPhone'],input[name = 'customNumber'],input[name = 'customContact'],input[name = 'contactNumber']").bind("input porpertychange", function () {
            $(".queryOrdersByUser_tel").val($(this).val());
        });

        //自动生成标题
        $("select[name='couponType'],select[name='brand'],input[name='activityName']").change(function () {
            var brand = $("select[name='brand']").find("option:selected").text();
            var couponType = $("select[name='couponType']").find("option:selected").text();
            var activityName = $("input[name='activityName']").val();
            var summary = brand + couponType + activityName;
            $("#summary").val(summary);
        });

        $('#importData').click(function (e) {
            e.preventDefault();
            $("#importError").html("");
            document.getElementById("importExcel").click();
        });

        $("#prewImportData").click(function (e) {
            e.preventDefault();
            var actId = $("input[name='id']").val();
            var width = $(window).width(),
                height = $(window).height();
            $.ajax({
                url: "${ctx}/activity/getImportStatus",
                type: "POST",
                data: {
                    "actId": actId
                },
                async: false,
                success: function (data) {
                    console.info(data);
                    if (data.obj == 0) {
                        var index = layer.open({
                            title: "预览导入信息",
                            type: 2,
                            shade: 0,
                            area: [width * 0.8, height * 0.8],
                            maxmin: true,
                            content: '${ctx}/activity/prewImportData?actId=' + actId
                        });
                        layer.full(index);
                    } else {
                        layer.open({
                            title: '信息',
                            content: "数据正在解析中，请稍后…",
                            btn: ["确定"],
                            closeBtn: false,
                            success: function () {
                            },
                            btn1: function (ind) {
                                layer.close(ind);
                            }
                        });
                    }
                }
            });


            /**/
        });

        $("#windowClose").click(function () {
            window.parent.close();
        });
    });

    function uploadFile() {
        validator = $("#activityForm").validate();
        if (validator.form()) {
            var actFormData = document.querySelector("#activityForm");
            //将获得的表单元素作为参数，对formData进行初始化
            var formData = new FormData(actFormData);
            formData.append('importFile', $('#importExcel')[0].files[0]);

            var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});

            $.ajax({
                url: "${ctx}/activity/importExcelData",
                type: "POST",
                data: formData,
                contentType: false,
                processData: false,
                success: function (data) {
                    document.getElementById("importExcel").value = "";
                    layer.close(mask);
                    var obj = data.obj;
                    if (obj.errcode === '0') {
                        $("#importDataSize").html(obj.dataSize);

                        $("input[name='id']").val(obj.actId);
                        $("input[name='jiraNo']").val(obj.jiraNo);
                        $("input[name='jiraStatus']").val(obj.jiraStatus);
                        $("input[name='releaseStatus']").val(obj.releaseStatus);
                        if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                            $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + obj.priority + "'/>");
                        } else {
                            $("select[name='priority']").val(obj.priority);
                        }
                        $("input[name='status']").val(obj.status);

                        layer.msg("已保存活动信息并导入券信息");
                    } else {
                        if (obj.errcode === '10000') {
                            $("#importError").html(obj.errmsg);
                        } else {
                            layer.msg(obj.errmsg);
                        }
                    }
                },
                error: function (data) {
                    layer.close(mask);
                    document.getElementById("importExcel").value = "";
                }
            });
        } else {
            document.getElementById("importExcel").value = "";
            layer.msg("数据校验未通过请查看");
        }
    }

    // 自动计算日期
    function calculateDate(self) {
        var confFinishedTimeStr = $(self).val();
        var recommendedTestTime = new Date(confFinishedTimeStr.replace(/-/g, '/'));
        var testFinishedTime = new Date(confFinishedTimeStr.replace(/-/g, '/'));
        recommendedTestTime.setDate(recommendedTestTime.getDate() + 3);
        testFinishedTime.setDate(testFinishedTime.getDate() + 5);
        $("#recommendedTestTime").val(recommendedTestTime.format('yyyy-MM-dd hh:mm'));
        $("#testFinishedTime").val(testFinishedTime.format('yyyy-MM-dd hh:mm'));
    }


    var actFileData = '${actFileData}';
    actFileData = actFileData.replace(/\\/g, "\\\\");
    actFileData = actFileData.replace(/\n/g, "").replace(/\r/g, "");
    if (actFileData && actFileData.length > 0) {
        try {
            actFileData = JSON.parse(actFileData);
        } catch (e) {
            actFileData = eval('(' + couponImages + ')');
        }
    } else {
        actFileData = new Array();
    }


    layui.use(["laytpl", 'table', 'element', 'upload', 'form', 'layer'], function () {

        var laytpl = layui.laytpl,
            table = layui.table,
            elem = layui.element,
            upload = layui.upload,
            form = layui.form,
            layer = layui.layer;

        var tbWidth = $("#tableRes").width();
        var layTableId = "layTable";
        var layTableId2 = "actFileTab";

        var tableIns = table.render({
            elem: '#dataTable',
            id: layTableId,
            data: viewObj.tbData,
            width: tbWidth,
            page: false,
            loading: false,
            even: false, //不开启隔行背景
            cols: [
                [{
                    title: '序号',
                    type: 'numbers',
                    width: 80
                },
                    {
                        field: 'couponLink',
                        title: '图片链接',
                        edit: 'text',
                        templet: function (d) {
                            if (d.taskId) {
                                return "<div class='layui-cell-text layui-no-edit'>" + (d.couponLink || '') + "</div>";
                            }
                            return "<div class='layui-cell-text'>" + (d.couponLink || '') + "</div>";
                        }
                    },
                    {
                        field: 'couponDesc',
                        title: '图片备注',
                        edit: 'textarea',
                        templet: function (d) {
                            if (d.taskId) {
                                return "<div class='layui-cell-text layui-no-edit'>" + (d.couponDesc.replace(RegExp('\n', "g"), '<br/>') || '') + "</div>";
                            }
                            return "<div class='layui-cell-text'>" + (d.couponDesc.replace(RegExp('\n', "g"), '<br/>') || '') + "</div>";
                        }
                    },
                    {
                        field: 'tempId',
                        title: '操作',
                        width: 100,
                        templet: function (d) {
                            if (d.taskId) {
                                return "";
                            }
                            return "<a class='layui-btn layui-btn-xs layui-btn-danger' lay-event='del' lay-id='" + d.tempId + "'>删除</a>";
                        }
                    }
                ]
            ],
            done: function (res, curr, count) {
                viewObj.tbData = res.data;

                $(".layui-no-edit").each(function () {
                    $(this).parent().parent().removeAttr("data-edit");
                });
            }
        });

        var actTab = table.render({
            elem: '#' + layTableId2,
            id: layTableId2,
            page: false,
            data: actFileData,
            cols: [[{
                title: '序号',
                type: 'numbers',
                width: 80,
                uzresize: true
            }, {
                field: 'images',
                title: '图片',
                edit: false,
                uzresize: true,
                templet: function (d) {
                    var content = "";
                    laytpl(uploadImgTemp.innerHTML).render(d, function (html) {
                        content = html;
                    });
                    return content;
                }
            }, {
                field: 'attach',
                title: '附件',
                edit: false,
                uzresize: true,
                templet: function (d) {
                    var content = "";
                    laytpl(uploadAttachTemp.innerHTML).render(d, function (html) {
                        content = html;
                    });
                    return content;
                }
            }, {
                field: 'remarks',
                title: '备注',
                edit: 'textarea',
                width: 300,
                uzresize: true,
                templet: function (d) {
                    return "<div class='layui-cell-text'>" + (d.remarks.replace(RegExp('\n', "g"), '<br/>') || '') + "</div>";
                }
            }, {
                field: 'tempId',
                title: '操作',
                align: "center",
                width: 80,
                uzresize: true,
                templet: function (d) {
                    return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" lay-id="' + d.tempId + '">删除</a>';
                }
            }]],
            done: function (res, curr, count) {
                $(".layui-table-body .layui-images .attach-view").each(function () {
                    previewImage($(this));
                });

                $(".layui-images").find(".upload_btn").each(function () {
                    var _t = $(this);
                    var mask = "";
                    var dataRowId = _t.attr("data-rowId");
                    var up = upload.render({
                        elem: _t,
                        url: '${ctx}/common/uploadFtpAttach',
                        multiple: true,
                        accept: "images",
                        auto: true,
                        before: function (obj) {
                            mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        },
                        done: function (resInner) {
                            layer.close(mask);
                            var obj = resInner.obj;
                            var item = _t.parent();
                            _t.before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><img src="' + obj.filePath + '" class="layui-upload-img"></div>');
                            previewImage(item);

                            var lastUpload = item.find(".layui-upload-item:last");

                            var input = "<input alt='" + obj.originalFileName + "' type='hidden' value='" + JSON.stringify(obj) + "'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId2];
                            for (var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if (row.tempId.toString() === dataRowId) {
                                    row.images[row.images.length] = obj;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }
                            layer.msg('上传成功');
                            up.config.elem.next()[0].value = '';
                        },
                        error: function (index, upload) {
                            layer.close(mask);
                            layer.msg('上传失败');
                            up.config.elem.next()[0].value = '';
                        }
                    });
                });

                $(".layui-attach .upload_btn").each(function () {
                    var _t = $(this);
                    var mask = "";
                    var dataRowId = _t.attr("data-rowId");
                    var up = upload.render({
                        elem: _t,
                        url: '${ctx}/common/uploadFtpAttach',
                        multiple: true,
                        accept: "file",
                        auto: true,
                        before: function (obj) {
                            mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        },
                        done: function (resInner) {
                            layer.close(mask);
                            var attachItem = resInner.obj;
                            var item = _t.parent();
                            item.find(".layui-upload-item").remove();
                            _t.before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><div  title="点击下载"><a href="javascript:void(0)" data-path="' + attachItem.filePath + '" data-orgName="' + attachItem.originalFileName + '" data-fileName="' + attachItem.fileName + '" onclick="downloadFtpAttach(this)">' + attachItem.originalFileName + '</a></div></div>');

                            var lastUpload = item.find(".layui-upload-item:last");
                            var input = "<input alt='" + attachItem.originalFileName + "' type='hidden' value='" + JSON.stringify(attachItem) + "'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId2];
                            for (var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if (row.tempId.toString() === dataRowId) {
                                    row.attach[0] = attachItem;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }

                            layer.msg('上传成功');
                            up.config.elem.next()[0].value = '';
                        },
                        error: function (index, upload) {
                            layer.close(mask);
                            layer.msg('上传失败');
                            up.config.elem.next()[0].value = '';
                        }
                    });
                });

                $("#upload_btn").each(function () {
                    var _t = $(this);
                    var mask = "";
                    var dataRowId = _t.attr("data-rowId");
                    var up = upload.render({
                        elem: _t,
                        url: '${ctx}/common/uploadFtpAttach',
                        multiple: true,
                        accept: "file",
                        auto: true,
                        before: function (obj) {
                            mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        },
                        done: function (resInner) {
                            layer.close(mask);
                            var attachItem = resInner.obj;
                            var item = _t.parent();
                            item.find(".layui-upload-item").remove();
                            _t.before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><div  title="点击下载"><a href="javascript:void(0)" data-path="' + attachItem.filePath + '" data-orgName="' + attachItem.originalFileName + '" data-fileName="' + attachItem.fileName + '" onclick="downloadFtpAttach(this)">' + attachItem.originalFileName + '</a></div></div>');

                            var lastUpload = item.find(".layui-upload-item:last");
                            var input = "<input alt='" + attachItem.originalFileName + "' type='hidden' value='" + JSON.stringify(attachItem) + "'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId2];
                            for (var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if (row.tempId.toString() === dataRowId) {
                                    row.attach[0] = attachItem;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }

                            layer.msg('上传成功');
                            up.config.elem.next()[0].value = '';
                        },
                        error: function (index, upload) {
                            layer.close(mask);
                            layer.msg('上传失败');
                            up.config.elem.next()[0].value = '';
                        }
                    });
                });
            }
        });

        var active = {
            closeWindow: function () { //获取选中数据
                window.close();
            },
            saveAndCreateTask: function () {
                validator = $("#activityForm").validate();
                if (!validator.form()) {
                    layer.msg("数据校验未通过请查看");
                    return;
                }
                var data = table.cache[layTableId];
                var tableBody = $(".layui-table-body");
                var checkResult = true;
                var saveData = new Array();
                for (var i = 0; i < data.length; i++) {
                    var item = data[i],
                        trIndex = item.LAY_TABLE_INDEX,
                        tr = tableBody.find('tr[data-index="' + trIndex + '"]');

                    if (item.remarks.length == 0) {
                        tr.addClass("layui-border");
                        checkResult = false;
                    } else {
                        tr.removeClass("layui-border");
                    }

                    item.images = new Array();
                    tr.find(".layui-images .layui-upload-item input").each(function (index) {
                        item.images[index] = JSON.parse($(this).val());
                    });

                    item.attach = new Array();
                    tr.find(".layui-attach .layui-upload-item input").each(function (index) {
                        item.attach[index] = JSON.parse($(this).val());
                    });

                    saveData.push(item);
                }

                if (!checkResult) {
                    layer.msg("请检查是否输入备注！");
                } else {
                    console.info('待保存数据', saveData);
                    $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                    $("input[name='receiveUser']").val($('#assigners option:selected').val());
                    var param = $("form").serializeArray();
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        url: '${ctx}/activity/saveUrgent',
                        type: 'POST',
                        data: param,
                        success: function (msg) {
                            layer.close(mask);
                            if (msg.success) {
                                $("input[name='id']").val(msg.obj.id);
                                $("input[name='jiraNo']").val(msg.obj.jiraNo);
                                $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                                $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                    $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + msg.obj.priority + "'/>");
                                } else {
                                    $("select[name='priority']").val(msg.obj.priority);
                                }
                                $("input[name='status']").val(msg.obj.status);
                                layer.msg(msg.message);

                                var sendMask = layer.msg('正在发送任务', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                                $.ajax({
                                    type: "post",
                                    data: {id: msg.obj.id},
                                    url: '${ctx}/task/ucpTask/createTask?time=' + new Date().getTime(),
                                    success: function (msg) {
                                        layer.msg(msg.message);
                                        setTimeout(function () {
                                            window.parent.close();
                                        }, 3000);
                                    }
                                });
                            }
                        }
                    });
                }
            },
            saveData: function () {
                validator = $("#activityForm").validate();
                if (!validator.form()) {
                    layer.msg("数据校验未通过请查看");
                    return;
                }
                var data = table.cache[layTableId];
                var tableBody = $(".layui-table-body");
                var checkResult = true;
                var saveData = new Array();
                for (var i = 0; i < data.length; i++) {
                    var item = data[i],
                        trIndex = item.LAY_TABLE_INDEX,
                        tr = tableBody.find('tr[data-index="' + trIndex + '"]');

                    if (item.remarks.length == 0) {
                        tr.addClass("layui-border");
                        checkResult = false;
                    }

                    item.images = new Array();
                    tr.find(".layui-images .layui-upload-item input").each(function (index) {
                        item.images[index] = JSON.parse($(this).val());
                    });

                    item.attach = new Array();
                    tr.find(".layui-attach .layui-upload-item input").each(function (index) {
                        item.attach[index] = JSON.parse($(this).val());
                    });

                    saveData.push(item);
                }

                if (!checkResult) {
                    layer.msg("请检查是否输入备注！");
                } else {
                    console.info('待保存数据', saveData);
                    $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                    $("input[name='receiveUser']").val($('#assigners option:selected').val());
                    var param = $("form").serializeArray();
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        url: '${ctx}/activity/saveUrgent',
                        type: 'POST',
                        data: param,
                        success: function (msg) {
                            layer.close(mask);
                            if (msg.success) {
                                $("input[name='id']").val(msg.obj.id);
                                $("input[name='jiraNo']").val(msg.obj.jiraNo);
                                $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                                $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                    $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + msg.obj.priority + "'/>");
                                } else {
                                    $("select[name='priority']").val(msg.obj.priority);
                                }
                                $("input[name='status']").val(msg.obj.status);
                                layer.msg(msg.message);
                            } else {
                                layer.msg(msg.message);
                            }
                        }
                    });
                }
            },

            addActFile: function (e) {
                e = window.event || e;
                if (e.stopPropagation) {
                    e.stopPropagation();      //阻止事件 冒泡传播
                } else {
                    e.cancelBubble = true;   //ie兼容
                }
                var oldData = table.cache[layTableId2];
                $.ajax({
                    url: '${ctx}/ucpActFile/createActFile',
                    type: 'get',
                    success: function (data) {
                        var newRow = {
                            tempId: new Date().valueOf(),
                            rowId: data.obj,
                            remarks: "",
                            images: [],
                            attach: []
                        };
                        oldData.push(newRow);
                        actTab.reload({
                            data: oldData
                        });
                    }
                })
            },
            removeEmptyTableCache2: function () {
                var oldData = table.cache[layTableId2];
                for (var i = 0, row; i < oldData.length; i++) {
                    row = oldData[i];
                    if (!row || !row.tempId) {
                        oldData.splice(i, 1); //删除一项
                    }
                    continue;
                }
                actTab.reload({
                    data: oldData
                });
            },


            addRow: function () { //添加一行
                var oldData = table.cache[layTableId];

                for (var i = 0, row; i < oldData.length; i++) {
                    row = oldData[i];
                    if (row.couponLink.length == 0) {
                        Common.myAlert("请输入信息", 2);
                        return;
                    }
                }

                var newRow = {
                    tempId: new Date().valueOf(),
                    cpId: '',
                    couponLink: '',
                    couponDesc: ''
                };
                oldData.push(newRow);
                tableIns.reload({
                    data: oldData
                });
            },

            updateRow: function (obj) {
                console.info(obj);

                var oldData = table.cache[layTableId];
                for (var i = 0, row; i < oldData.length; i++) {
                    row = oldData[i];
                    if (row.tempId == obj.tempId) {
                        $.extend(oldData[i], obj);
                        return;
                    }
                }
                tableIns.reload({
                    data: oldData
                });
            },

            removeEmptyTableCache: function () {
                var oldData = table.cache[layTableId];
                for (var i = 0, row; i < oldData.length; i++) {
                    row = oldData[i];
                    if (!row || !row.tempId) {
                        oldData.splice(i, 1); //删除一项
                    }
                    continue;
                }
                tableIns.reload({
                    data: oldData
                });
            }
        };

        //激活事件
        var activeByType = function (type, arg) {
            if (arguments.length === 2) {
                active[type] ? active[type].call(this, arg) : '';
            } else {
                active[type] ? active[type].call(this) : '';
            }
        }


        var colActive = function (type, arg) {
            if (arguments.length === 2) {
                active[type] ? active[type].call(this, arg) : '';
            } else {
                active[type] ? active[type].call(this) : '';
            }
        }

        $('.btn-active .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        $('.layui-btn[data-type]').on('click', function () {
            var type = $(this).data('type');
            activeByType(type);
        });

        //监听工具条
        table.on('tool(' + layTableId2 + ')', function (obj) {
            var data = obj.data,
                event = obj.event,
                tr = obj.tr; //获得当前行 tr 的DOM对象;

            switch (event) {
                case "del":
                    layer.confirm('确定要删除该行吗？', function (index) {
                        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        colActive('removeEmptyTableCache2');
                        layer.close(index);
                    });
                    break;
            }
        });

        table.on('tool(dataTable)', function (obj) {
            try {
                var data = obj.data,
                    event = obj.event,
                    tr = obj.tr; //获得当前行 tr 的DOM对象;

                switch (event) {
                    case "del":
                        layer.confirm('确认要删除该行吗？', function (index) {
                            if (data.cpId) {
                                $.ajax({
                                    type: "post",
                                    data: {id: data.cpId},
                                    url: ctx + "/activity/couponImage/deleteCouponImage?time=" + new Date().getTime(),
                                    success: function (msg) {
                                        layer.msg(msg.message);
                                    }
                                });
                            }
                            obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                            layer.close(index);
                            activeByType('removeEmptyTableCache');
                        });

                        break;
                }
            } catch (e) {

            }
        });

        // 保存活动
        $(".activitySave").click(function () {
            validator = $("#activityForm").validate();
            if (validator.form()) {

                var data = table.cache[layTableId2];
                var tableBody = $(".layui-table-body");
                var checkResult = true;
                var saveData = new Array();
                for (var i = 0; i < data.length; i++) {
                    var item = data[i],
                        trIndex = item.LAY_TABLE_INDEX,
                        tr = tableBody.find('tr[data-index="' + trIndex + '"]');

                    if (item.remarks.length == 0) {
                        tr.addClass("layui-border");
                        checkResult = false;
                    } else {
                        tr.removeClass("layui-border");
                    }

                    item.images = new Array();
                    tr.find(".layui-images .layui-upload-item input").each(function (index) {
                        item.images[index] = JSON.parse($(this).val());
                    });

                    item.attach = new Array();
                    tr.find(".layui-attach .layui-upload-item input").each(function (index) {
                        item.attach[index] = JSON.parse($(this).val());
                    });

                    saveData.push(item);
                }

                console.info('待保存数据', saveData);
                if (!checkResult) {
                    layer.msg("请检查是否输入上传附件的备注！");
                } else {
                    $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                    $("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
                    var param = $("form").serializeArray();
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        type: "post",
                        data: param,
                        url: ctx + "/activity/save?time=" + new Date().getTime(),
                        success: function (msg) {
                            layer.close(mask);
                            if (msg.success) {
                                $("input[name='id']").val(msg.obj.id);
                                $("input[name='jiraNo']").val(msg.obj.jiraNo);
                                $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                                $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                    $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + msg.obj.priority + "'/>");
                                } else {
                                    $("select[name='priority']").val(msg.obj.priority);
                                }
                                $("input[name='status']").val(msg.obj.status);
                                layer.msg(msg.message);
                                loadCouponImage(msg.obj.id);
                            } else {
                                layer.msg(msg.message);
                            }
                        },
                        error: function () {
                            layer.close(mask);
                            layer.msg("保存失败");
                        }
                    });
                }


            } else {
                layer.msg("数据校验未通过请查看");
            }
        });


        $(".templateSave").click(function () {
            // self.location = document.referrer
            validator = $("#templateForm").validate();
            if (validator.form()) {
                // var data = table.cache[layTableId2];
                // var tableBody = $(".layui-table-body");
                // var checkResult = true;
                // var saveData = new Array();
                // for (var i = 0; i < data.length; i++) {
                //     var item = data[i],
                //         trIndex = item.LAY_TABLE_INDEX,
                //         tr = tableBody.find('tr[data-index="' + trIndex + '"]');
                //
                //     if (item.remarks.length == 0) {
                //         tr.addClass("layui-border");
                //         checkResult = false;
                //     } else {
                //         tr.removeClass("layui-border");
                //     }
                //
                //     item.images = new Array();
                //     tr.find(".layui-images .layui-upload-item input").each(function (index) {
                //         item.images[index] = JSON.parse($(this).val());
                //     });
                //
                //     item.attach = new Array();
                //     tr.find(".layui-attach .layui-upload-item input").each(function (index) {
                //         item.attach[index] = JSON.parse($(this).val());
                //     });
                //
                //     saveData.push(item);
                // }
                //
                // $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                // $("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
                var param = $("form").serializeArray();
                var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                $.ajax({
                    type: "post",
                    data: param,
                    url: ctx + "/template/save?time=" + new Date().getTime(),
                    success: function (msg) {
                        layer.close(mask);
                        if (msg.success) {
                            // $("input[name='id']").val(msg.obj.id);
                            // $("input[name='jiraNo']").val(msg.obj.jiraNo);
                            // $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                            // $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                            // if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                            //     $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + msg.obj.priority + "'/>");
                            // } else {
                            //     $("select[name='priority']").val(msg.obj.priority);
                            // }
                            // $("input[name='status']").val(msg.obj.status);
                            $(".templateSave").attr("disabled", true);
                            $('.templateSave').hide()
                            var successMsg = msg.message + ",请关闭此页面，并重新点击模版列表，查询最新数据。"
                            layer.msg(successMsg);
                            <%--window.open("${ctx}/template/list");--%>
                            // window.parent.close();
                            // loadCouponImage(msg. obj.id);
                        } else {
                            layer.msg(msg.message);
                        }
                    },
                    error: function () {
                        layer.close(mask);
                        layer.msg("保存失败");
                    }
                });
            }
        });

        /**
         *加载券图信息
         */
        function loadCouponImage(actId) {
            $.ajax({
                type: "post",
                data: {actId: actId},
                url: ctx + "/activity/couponImage/findByActId?time=" + new Date().getTime(),
                success: function (obj) {
                    tableIns.reload({
                        data: obj
                    });
                }
            });
        }

        $(".saveAndCreateTask").click(function () {
            layer.confirm('是否确认保存并发送该任务？', function (index) {
                validator = $("#activityForm").validate();
                if (validator.form()) {

                    var data = table.cache[layTableId2];
                    var tableBody = $(".layui-table-body");
                    var checkResult = true;
                    var saveData = new Array();
                    for (var i = 0; i < data.length; i++) {
                        var item = data[i],
                            trIndex = item.LAY_TABLE_INDEX,
                            tr = tableBody.find('tr[data-index="' + trIndex + '"]');

                        if (item.remarks.length == 0) {
                            tr.addClass("layui-border");
                            checkResult = false;
                        } else {
                            tr.removeClass("layui-border");
                        }

                        item.images = new Array();
                        tr.find(".layui-images .layui-upload-item input").each(function (index) {
                            item.images[index] = JSON.parse($(this).val());
                        });

                        item.attach = new Array();
                        tr.find(".layui-attach .layui-upload-item input").each(function (index) {
                            item.attach[index] = JSON.parse($(this).val());
                        });

                        saveData.push(item);
                    }

                    console.info('待保存数据', saveData);
                    if (!checkResult) {
                        layer.msg("请检查是否输入上传附件的备注！");
                    } else {
                        $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                        $("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
                        var param = $("form").serializeArray();
                        var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        $.ajax({
                            type: "post",
                            data: param,
                            url: ctx + "/activity/save?time=" + new Date().getTime(),
                            success: function (msg) {
                                layer.close(mask);
                                if (msg.success) {
                                    $("input[name='id']").val(msg.obj.id);
                                    $("input[name='jiraNo']").val(msg.obj.jiraNo);
                                    $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                                    $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                    if ($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                        $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='" + msg.obj.priority + "'/>");
                                    } else {
                                        $("select[name='priority']").val(msg.obj.priority);
                                    }
                                    $("input[name='status']").val(msg.obj.status);
                                    layer.msg(msg.message);
                                    loadCouponImage(msg.obj.id);

                                    // 发送任务
                                    var param = $("form").serializeArray();
                                    console.log(param);
                                    //param = param.splice(0,16);
                                    //console.log(param);
                                    var sendMask = layer.msg('正在发送任务', {
                                        icon: 16,
                                        time: 0,
                                        shade: [0.8, '#393D49']
                                    });

                                    $.ajax({
                                        type: "post",
                                        data: param,
                                        url: ctx + "/task/ucpTask/saveAndCreateTask?time=" + new Date().getTime(),
                                        success: function (msg) {
                                            layer.close(sendMask);
                                            layer.msg(msg.message);
                                            if (msg.success) {
                                                $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                            }
                                            setTimeout(function () {
                                                window.parent.close();
                                            }, 3000);
                                        },
                                        error: function () {
                                            layer.close(sendMask);
                                            layer.msg("发送任务失败");
                                        }
                                    });
                                } else {
                                    layer.msg(msg.message);
                                }
                            },
                            error: function () {
                                layer.close(mask);
                                layer.msg("保存活动失败");
                            }
                        });

                    }


                } else {
                    layer.msg("数据校验未通过请查看");
                }
            });
        });


    });


    function uploadImgDelete(obj) {
        var filePath = $(obj).parent().find("img").attr("src");
        $.ajax({
            url: "${ctx}/common/delFtpAttach",
            type: "POST",
            data: {
                "filePath": filePath
            },
            success: function (data) {
            }
        });
        $(obj).parent().remove();
    }

    function previewImage(item) {
        var photoClass = "." + item.attr("prev-class");
        layer.photos({photos: photoClass, anim: 5});
    }

    function downloadFtpAttach(_t) {
        var filePath = $(_t).attr("data-path"),
            originalFileName = $(_t).attr("data-orgName"),
            fileName = $(_t).attr("data-fileName");

        var baseDir = filePath.replace(fileName, "");
        baseDir = baseDir.replace("${filePath}", "");
        window.location.href = encodeURI("/ucp/common/downloadUrgentFtpAttach?originalFileName=" + originalFileName + "&fileName=" + fileName + "&baseDir=.." + baseDir);
    }

</script>


<script type="text/javascript">
    //③创建fileLoad方法用来上传文件
    function fileLoad(ele) {
        //④创建一个formData对象
        var formData = new FormData();
        //⑤获取传入元素的val
        var name = $(ele).val();
        console.log(name);
        //⑥获取files
        var files = $(ele)[0].files[0];
        console.log(files);
        //⑦将name 和 files 添加到formData中，键值对形式
        formData.append("file", files);
        formData.append("name", name);
        var mask = "";
        $.ajax({
            url: '${ctx}/template/uploadTemplateFtpAttach',
            multiple: false,
            type: 'POST',
            processData: false,
            contentType: false,
            data: formData,
            auto: true,
            beforeSend: function () {
                mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
            },
            success: function (resInner) {
                layer.close(mask);
                var attachItem = resInner.obj;
                $('#fileName').val(attachItem.originalFileName);
                $('#filePath').val(attachItem.filePath);
                $("#file_a").attr("title", attachItem.originalFileName);
                $("#file_a").attr("href", attachItem.filePath);
                $("#file_a").text(attachItem.originalFileName);
                layer.msg('上传成功');
            }
            ,
            error: function (responseStr) {
                layer.close(mask);
                layer.msg('上传失败');
            }
        });
    }

    $(function () {
        var $input = $("#upLoadInput");
        // ①为input设定change事件
        $input.change(function () {
            //    ②如果value不为空，调用文件加载方法
            if ($(this).val() !== "") {
                fileLoad(this);
            }
        })
    })


</script>
</html>
