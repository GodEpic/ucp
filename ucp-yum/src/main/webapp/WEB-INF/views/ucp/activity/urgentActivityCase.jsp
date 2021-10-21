<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<head>
    <title>紧急活动</title>
    <link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
    <link rel="stylesheet" href="${static.resource.url}/css/activity.css" />
    <link rel="stylesheet" href="${static.resource.url}/css/task.css" />
    <link rel="stylesheet" href="${static.resource.url}/css/qa.reject.css" />
    <script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
    <style>
        label {
            width: 160px;
            text-align: right;
        }
        input[type=text]{
            width: 255px;
            color: #666;
            margin-right: 0;
            height: 30px;
            line-height: 25px;
            box-sizing: border-box;
            cursor: text;
        }
        .layui-upload-attach{
            text-decoration: underline !important;
        }

        .layui-attach .layui-upload-item, .layui-attach .upload_btn{
            text-align: center;
            display: block;
        }

        .layui-attach .layui-upload-item{
            width:fit-content;
            width:-webkit-fit-content;
            width:-moz-fit-content;
            margin: 15px auto;
        }

        .layui-attach .layui-upload-item div{
            padding: 0px 10px;
            border: 1px solid #DDD;
            border-radius: 6px;
            background-color: #DDD;
            color: #000;
        }

        .layui-upload-img-del{
            cursor: pointer;
        }

        .layui-border {
            border: 1px solid red;
        }

        .sec-form li label.error{
            left: 90px;
        }

        .sec-desc{
            width: 100%;
            display: inline-block;
        }

        .sec-desc textarea{
            padding: 10px;
            width: 780px;
        }

        .sec-desc li label.error{
            display: inline-block;
            right: 0;
            left: auto;
            margin: 0;
            width: auto;
        }

        .sec-desc li{
            width: 92%;
        }

        .sec-case-form{
            width: 1150px;
        }

        .sec-form input[type=text]{
            margin-right: 0;
        }
    </style>
</head>
<body class="childBody">
<div class="tab-con sec-case-form btn-active">
    <form id="activityForm">
        <div class="sec">
            <article class="sec-title t2">活动基本信息</article>
            <div class="sec-form">
                <li>
                    <label><b class="label">*</b>标题：</label>
                    <input type="text" name="summary" id="summary" value="${activity.summary}" required >
                </li>
                <div class="clear"></div>
            </div>
            <div class="sec-form">
                <li>
                    <label><b class="label">*</b>反馈时间：</label>
                    <input id="feedbackTime" name="feedbackTime" type="text" maxlength="20" class="input-medium Wdate "
                           value="${activity.feedbackTime}" readonly="readonly" required
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:true, minDate: '%y-%M-%d %H:%m'});"/>
                </li>
                <div class="clear"></div>
            </div>
            <div class="sec-form sec-desc">
                <li>
                    <label><b class="label">*</b>活动说明：</label>
                    <textarea id="description" name="description" required
                              placeholder="说明...">${activity.description}</textarea>
                </li>
                <div class="clear"></div>
            </div>
        </div>
        <div class="sec">
            <div class="sec-top">
                <span><button id="addActFile" type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="addActFile" title="添加图片和附件">+ 添加图片和附件</button></span>
            </div>
            <div class="sec-form">
                <table class="layui-table" id="actFileTab" lay-filter="actFileTab"></table>
            </div>
            <input type="hidden" id="actFiles" name="actFiles"/>
            <input type="hidden" id="receiveUser" name="receiveUser" value="${activity.receiveUser}"/>
            <input type="hidden" name="id" value="${activity.id}">
            <input type="hidden" name="jiraNo" value="${activity.jiraNo}"/>
            <input type="hidden" name="jiraStatus" value="${activity.jiraStatus}"/>
            <input type="hidden" name="status" value="${activity.status}"/>
            <input type="hidden" name="releaseStatus" value="${activity.releaseStatus}"/>
            <input type="hidden" name="type" value="${activity.type}"/>
            <div class="sec-form">
                <li>
                    <label><b class="label">*</b>指定接收人：</label>
                    <select id="assigners" name="assigners" lay-search>
                    </select>
                </li>
                <div class="clear"></div>
            </div>
        </div>
        <div class="sec">
            <div class="sec-btn">
                <div class="sec-btn-submit">
                    <button id="submitBtn" type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="saveData" title="保存">保存活动</button>
                    <button type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="saveAndCreateTask" title="保存并发送"> 保存并发送任务</button>
                    <button id="windowClose" type="button" class="layui-btn layui-btn-sm layui-coupon-btn" title="关闭">关闭</button>
                </div>
            </div>
        </div>
    </form>
</div>


<script type="text/javascript">
    $(function () {
        $.ajax({
            url: '${ctx}/activity/getAssigners',
            contentType: 'application/json',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                var users = data.obj;
                if(users!=null && users.length>0){
                    for(var i=0;i<users.length;i++){
                        var user = users[i];
                        $("#assigners").append("<option value='"+user.no+"'>"+user.name+"</option>");
                    }
                }
                if($("#receiveUser").val()){
                    $("#assigners").val($("#receiveUser").val());
                }
            }
        });
        $("#windowClose").click(function(){
            window.parent.close();
        });
    });
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
            table = layui.table,
            elem = layui.element,
            upload = layui.upload,
            form = layui.form,
            layer = layui.layer;

//        var actFileData = [];
        var layTableId = "actFileTab";
        var actTab = table.render({
            elem : '#' + layTableId,
            id : layTableId,
            page : false,
            data : actFileData,
            cols : [ [{
                title : '序号',
                type : 'numbers',
                width:80,
                uzresize:true
            }, {
                field: 'images',
                title: '图片',
                edit: false,
                uzresize:true,
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
                edit: false,
                uzresize:true,
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
                uzresize:true,
                templet: function(d) {
                    return "<div class='layui-cell-text'>" + (d.remarks.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
                }
            }, {
                field: 'tempId',
                title: '操作',
                align : "center",
                width:80,
                uzresize:true,
                templet: function(d) {
                    return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" lay-id="' + d.tempId + '">删除</a>';
                }
            }] ],
            done : function(res, curr, count){
                $(".layui-table-body .layui-images .attach-view").each(function(){
                    previewImage($(this));
                });

                $(".layui-images").find(".upload_btn").each(function(){
                    var _t = $(this);
                    var mask = "";
                    var dataRowId = _t.attr("data-rowId");
                    var up = upload.render({
                        elem: _t,
                        url: '${ctx}/common/uploadFtpAttach',
                        multiple: true,
                        accept:"images",
                        auto : true,
                        before:function(obj){
                            mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        },
                        done : function(resInner){
                            layer.close(mask);
                            var obj = resInner.obj;
                            var item = _t.parent();
                            _t.before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><img src="'+ obj.filePath +'" class="layui-upload-img"></div>');
                            previewImage(item);

                            var lastUpload = item.find(".layui-upload-item:last");

                            var input = "<input alt='" + obj.originalFileName + "' type='hidden' value='"+ JSON.stringify(obj) +"'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId];
                            for(var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if(row.tempId.toString() === dataRowId) {
                                    row.images[row.images.length] = obj;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }
                            layer.msg('上传成功');
                            up.config.elem.next()[0].value = '';
                        },
                        error: function(index, upload){
                            layer.close(mask);
                            layer.msg('上传失败');
                            up.config.elem.next()[0].value = '';
                        }
                    });
                });

                $(".layui-attach .upload_btn").each(function(){
                    var _t = $(this);
                    var mask = "";
                    var dataRowId = _t.attr("data-rowId");
                    var up = upload.render({
                        elem: _t,
                        url: '${ctx}/common/uploadFtpAttach',
                        multiple: true,
                        accept:"file",
                        auto : true,
                        before:function(obj){
                            mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        },
                        done : function(resInner){
                            layer.close(mask);
                            var attachItem = resInner.obj;
                            var item = _t.parent();
                            item.find(".layui-upload-item").remove();
                            _t.before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><div  title="点击下载"><a href="javascript:void(0)" data-path="'+ attachItem.filePath +'" data-orgName="'+ attachItem.originalFileName +'" data-fileName="'+ attachItem.fileName +'" onclick="downloadFtpAttach(this)">'+ attachItem.originalFileName +'</a></div></div>');

                            var lastUpload = item.find(".layui-upload-item:last");
                            var input = "<input alt='" + attachItem.originalFileName + "' type='hidden' value='"+ JSON.stringify(attachItem) +"'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId];
                            for(var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if(row.tempId.toString() === dataRowId) {
                                    row.attach[0] = attachItem;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }

                            layer.msg('上传成功');
                            up.config.elem.next()[0].value = '';
                        },
                        error: function(index, upload){
                            layer.close(mask);
                            layer.msg('上传失败');
                            up.config.elem.next()[0].value = '';
                        }
                    });
                });
            }
        });

        var active = {
            closeWindow: function(){ //获取选中数据
                window.close();
            },
            saveAndCreateTask :function () {
                validator = $("#activityForm").validate();
                if (!validator.form()) {
                    layer.msg("数据校验未通过请查看");
                    return;
                }
                var data = table.cache[layTableId];
                var tableBody = $(".layui-table-body");
                var checkResult = true;
                var saveData = new Array();
                for(var i = 0; i < data.length; i++){
                    var item = data[i],
                        trIndex = item.LAY_TABLE_INDEX,
                        tr = tableBody.find('tr[data-index="'+ trIndex +'"]');

                    if(item.remarks.length == 0){
                        tr.addClass("layui-border");
                        checkResult = false;
                    } else {
                        tr.removeClass("layui-border");
                    }

                    item.images = new Array();
                    tr.find(".layui-images .layui-upload-item input").each(function(index){
                        item.images[index] = JSON.parse($(this).val());
                    });

                    item.attach = new Array();
                    tr.find(".layui-attach .layui-upload-item input").each(function(index){
                        item.attach[index] = JSON.parse($(this).val());
                    });

                    saveData.push(item);
                }

                if(!checkResult){
                    layer.msg("请检查是否输入备注！");
                } else {
                    console.info('待保存数据',saveData);
                    $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                    $("input[name='receiveUser']").val($('#assigners option:selected').val());
                    var param = $("form").serializeArray();
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        url: '${ctx}/activity/saveUrgent',
                        type: 'POST',
                        data:param,
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
                                    url:  '${ctx}/task/ucpTask/createTask?time=' + new Date().getTime(),
                                    success: function (msg) {
                                        layer.msg(msg.message);
                                        setTimeout(function(){
                                            window.parent.close();
                                        },3000);
                                    }
                                });
                            }
                        }
                    });
                }
            },
            saveData :function(){
                validator = $("#activityForm").validate();
                if (!validator.form()) {
                    layer.msg("数据校验未通过请查看");
                    return;
                }
                var data = table.cache[layTableId];
                var tableBody = $(".layui-table-body");
                var checkResult = true;
                var saveData = new Array();
                for(var i = 0; i < data.length; i++){
                    var item = data[i],
                        trIndex = item.LAY_TABLE_INDEX,
                        tr = tableBody.find('tr[data-index="'+ trIndex +'"]');

                    if(item.remarks.length == 0){
                        tr.addClass("layui-border");
                        checkResult = false;
                    }

                    item.images = new Array();
                    tr.find(".layui-images .layui-upload-item input").each(function(index){
                        item.images[index] = JSON.parse($(this).val());
                    });

                    item.attach = new Array();
                    tr.find(".layui-attach .layui-upload-item input").each(function(index){
                        item.attach[index] = JSON.parse($(this).val());
                    });

                    saveData.push(item);
                }

                if(!checkResult){
                    layer.msg("请检查是否输入备注！");
                } else {
                    console.info('待保存数据',saveData);
                    $("input[name='actFiles']").val(JSON.stringify(saveData, null, 2));
                    $("input[name='receiveUser']").val($('#assigners option:selected').val());
                    var param = $("form").serializeArray();
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        url: '${ctx}/activity/saveUrgent',
                        type: 'POST',
                        data:param,
                        success: function (msg) {
                            layer.close(mask);
                            if (msg.success) {
                                $("input[name='id']").val(msg.obj.id);
                                $("input[name='jiraNo']").val(msg.obj.jiraNo);
                                $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                                $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                if($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                    $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='"+msg.obj.priority+"'/>");
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

            addActFile : function(e){
                e = window.event || e;
                if (e.stopPropagation) {
                    e.stopPropagation();      //阻止事件 冒泡传播
                } else {
                    e.cancelBubble = true;   //ie兼容
                }
                var oldData = table.cache[layTableId];
                $.ajax({
                    url:'${ctx}/ucpActFile/createActFile',
                    type:'get',
                    success:function (data){
                        var newRow = {
                            tempId: new Date().valueOf(),
                            rowId : data.obj,
                            remarks : "",
                            images : [],
                            attach: []
                        };
                        oldData.push(newRow);
                        actTab.reload({
                            data: oldData
                        });
                    }
                })
            },
            removeEmptyTableCache: function() {
                var oldData = table.cache[layTableId];
                for(var i = 0, row; i < oldData.length; i++) {
                    row = oldData[i];
                    if(!row || !row.tempId) {
                        oldData.splice(i, 1); //删除一项
                    }
                    continue;
                }
                actTab.reload({
                    data: oldData
                });
            },
        };

        //激活事件
        var colActive = function(type, arg) {
            if(arguments.length === 2) {
                active[type] ? active[type].call(this, arg) : '';
            } else {
                active[type] ? active[type].call(this) : '';
            }
        }

        $('.btn-active .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //监听工具条
        table.on('tool('+ layTableId +')', function(obj) {
            var data = obj.data,
                event = obj.event,
                tr = obj.tr; //获得当前行 tr 的DOM对象;

            switch(event) {
                case "del":
                    layer.confirm('确定要删除该行吗？',  function(index) {
                        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        colActive('removeEmptyTableCache');
                        layer.close(index);
                    });
                    break;
            }
        });
    });

    function uploadImgDelete(obj){
        var filePath = $(obj).parent().find("img").attr("src");
        $.ajax({
            url: "${ctx}/common/delFtpAttach",
            type: "POST",
            data: {
                "filePath" : filePath
            },
            success: function (data) {
            }
        });
        $(obj).parent().remove();
    }

    function previewImage(item){
        var photoClass = "." + item.attr("prev-class");
        layer.photos({photos: photoClass, anim: 5});
    }

    function downloadFtpAttach(_t){
        var filePath = $(_t).attr("data-path"),
            originalFileName = $(_t).attr("data-orgName"),
            fileName = $(_t).attr("data-fileName");

        var baseDir = filePath.replace(fileName, "");
        baseDir = baseDir.replace("${filePath}","");
        window.location.href= encodeURI("/ucp/common/downloadUrgentFtpAttach?originalFileName=" + originalFileName + "&fileName=" + fileName + "&baseDir=.." + baseDir);
    }
</script>

<script type="text/html" id="uploadImgTemp">
    <div class="layui-item-value layui-label-color">
        <div class="layui-upload-list layui-images">
            <div class="layer-photos attach-view {{d.tempId}}" prev-class="{{d.tempId}}">
                {{#  layui.each(d.images, function(index, item){ }}
                <div class="layui-upload-item">
                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                    <img alt="{{item.originalFileName}}" src="{{item.filePath}}" class="layui-upload-img">
                    <input type="hidden" value='{"fileName":"{{item.fileName}}","filePath":"{{item.filePath}}","originalFileName":"{{item.originalFileName}}","fileSize":{{item.fileSize}},"ext":"{{item.ext}}"}'/>
                </div>
                {{#  }); }}
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
                {{#  layui.each(d.attach, function(index, item){ }}
                <div class="layui-upload-item">
                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                    <div  title="点击下载"><a href="javascript:void(0)" data-path="{{item.filePath}}" data-orgName="{{item.originalFileName}}" data-fileName="{{item.fileName}}" onclick="downloadFtpAttach(this)">{{item.originalFileName}}</a></div>
                    <input type="hidden" value='{"fileName":"{{item.fileName}}","filePath":"{{item.filePath}}","originalFileName":"{{item.originalFileName}}","fileSize":{{item.fileSize}},"ext":"{{item.ext}}"}'/>
                </div>
                {{#  }); }}
                <div class="upload_btn" data-rowId="{{d.tempId}}" style="width: 60px;margin: auto;">
                    <div><a href="javascript:void(0)" class="layui-upload-attach">上传附件</a></div>
                </div>
            </div>
        </div>
    </div>
</script>
</body>