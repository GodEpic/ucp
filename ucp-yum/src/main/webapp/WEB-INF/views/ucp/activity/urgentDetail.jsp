<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
		<title>紧急活动详情</title>

		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
		<link rel="stylesheet" href="${static.resource.url}/css/activity.css" />
		<link rel="stylesheet" href="${static.resource.url}/css/task.css" />
		<link rel="stylesheet" href="${static.resource.url}/css/qa.reject.css" />
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		<style type="text/css">
			label {
				width: 160px !important;
				text-align: right;
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

			.sec-case-form{
				width: 1150px;
			}

			.layui-table-cell{
				height: auto;
			}
		</style>
	</head>
	<body>
		<div class="tab-con sec-case-form">
			<input type="hidden" name="id" value="${activity.id}">
			<input type="hidden" name="jiraNo" value="${activity.jiraNo}"/>
			<input type="hidden" name="jiraStatus" value="${activity.jiraStatus}"/>
			<input type="hidden" name="status" value="${activity.status}"/>
			<input type="hidden" name="releaseStatus" value="${activity.releaseStatus}"/>
			<div class="sec">
				<article class="sec-title t2">活动基本信息</article>
				<div class="sec-form layui-row">
						<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title">标题：</label>
						<div class="layui-input-block">
							${activity.summary}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title">反馈时间：</label>
						<div class="layui-input-block">
							${activity.feedbackTime}
						</div>
					</div>
				</div>

				
				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title">活动说明：</label>
						<div class="layui-input-block">
							${activity.description}
						</div>
					</div>
				</div>

				<div class="sec-form layui-row">
					<div class="layui-col-xs12 layui-form-item">
						<label class="layui-form-label layui-form-title">图片和附件：</label>
						<div class="layui-input-block">
							<table class="layui-table" id="actFileTab" lay-filter="actFileTab"></table>
						</div>
					</div>
				</div>

				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title">活动创建人：</label>
						<div class="layui-input-block">
							${fns:getUserById(activity.createBy).name}
						</div>
					</div>
				</div>

				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title">指定接收人：</label>
						<div class="layui-input-block">
							${fns:getUserNameByWorkCode(activity.receiveUser)}
						</div>
					</div>
				</div>
			</div>

			<div class="sec">
				<div class="sec-btn">
					<div class="sec-btn-submit">
						<c:if test="${activity.jiraStatus eq SPECIAL_ACTIVITY_JIRA_STATUS_ASSIGN and activity.receiveUser eq agent}">
							<div class="sec-btn-submit"><a id="submitBtn" href="javascript:void(0)" class="layui-yum-btn">处理完成</a></div>
						</c:if>
						<a id="closeBtn" href="javascript:void(0)" class="layui-yum-btn">关闭</a>
					</div>
				</div>
			</div>
		</div>
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		
		<script type="text/javascript">
            $("#closeBtn").click(function(){
                window.parent.close();
            });

            $("#submitBtn").click(function () {
                layer.confirm("确定处理完成了吗？", function(index) {
                    var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    $.ajax({
                        url: "${ctx}/activity/specialEndCase",
                        type: "POST",
                        data: {
                            "id" : "${activity.id}"
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
	</body>
</html>
