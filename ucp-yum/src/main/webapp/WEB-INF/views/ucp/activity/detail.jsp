<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
		<title>普通活动详情</title>
		
		<link rel="stylesheet" href="${static.resource.url}/css/task.css" />
		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
        <link rel="stylesheet" href="${static.resource.url}/css/activity.css">
		<style type="text/css">
			.sec-case-form{
				width: 80%;
				margin: auto;
			}
			
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

			.layui-table-view{
				border: none;
			}

			.layui-table-view .layui-table td, .layui-table-view .layui-table th{
				border:none;
			}

			.sec-form .table th{
				border: none;
			}

			.import-desc{
				font-size: 16px;
				color: red;
				font-weight: bolder;
			}
		</style>
	</head>
	<body>
		<div class="tab-con sec-case-form">
			<div class="sec">
				<article class="sec-title t2">活动基本信息</article>
				<div class="sec-form layui-row">
						<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>标题：</label>
						<div class="layui-input-block">
							${activity.summary}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>品牌：</label>
						<div class="layui-input-block">
							${fns:getDictLabel(activity.brand, 'brand', '')}
						</div>
					</div>
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>券类型：</label>
						<div class="layui-input-block">
							${fns:getDictLabel(activity.couponType, 'couponType', '')}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>活动名：</label>
						<div class="layui-input-block">
							${activity.activityName}
						</div>
					</div>
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>券数量：</label>
						<div class="layui-input-block">
							${activity.couponCount}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>配置完成时间：</label>
						<div class="layui-input-block">
							${activity.confFinishedTime}
						</div>
					</div>
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>建议测试时间：</label>
						<div class="layui-input-block">
							${activity.recommendedTestTime}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>测试完成时间：</label>
						<div class="layui-input-block">
							${activity.testFinishedTime}
						</div>
					</div>
					<div class="layui-col-xs5 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>上架时间：</label>
						<div class="layui-input-block">
							${activity.launchTime}
						</div>
					</div>
				</div>
				
				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>注意事项：</label>
						<div class="layui-input-block">
							${fns:replaceEnter(activity.description)}
						</div>
					</div>
				</div>

				<div class="sec-form layui-row">
					<div class="layui-col-xs10 layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>优先级：</label>
						<div class="layui-input-block">
							${fns:getDictLabel(activity.priority, 'priority', '')}
						</div>
					</div>
				</div>
			</div>
			<div class="sec">
				<article class="sec-title t2">券信息</article>


				<div class="sec-form">
					<div class="sec-import">
						<div class="import-prew">
							<button id="prewImportData" type="button">预览导入信息</button>
						</div>
					</div>
					<div class="import-result">
						<table class="importTable">
							<tr>
								<th align="center">导入说明</th>
							</tr>
							<tr>
								<td align="center" class="import-desc">已导入<span id="importDataSize">${couponCount}</span>条信息</td>
							</tr>
						</table>
					</div>
					<%--<div class="sec-tbl layui-module-row">
						<table class="layui-table" id="moduleRowTab" lay-filter="moduleRowTab"></table>
					</div>--%>
				</div>
			</div>


			<div class="sec-form layui-row"> 
                <article class="sec-title t2">图片和附件：</article>
				<div class="sec-form">
					<div class="sec-tbl layui-module-row">
						<table class="layui-table" id="actFileTab" lay-filter="actFileTab"></table>
					</div>
				</div>
               
            </div>


			<div class="sec">
				<article class="sec-title t2">券图信息</article>

				<div class="sec-form">
					<div class="sec-tbl">
						<table class="table table-bordered layui-table">
							<thead>
							<tr>
								<th>序号</th>
								<th>图片链接</th>
								<th>图片说明</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${couponImageList}" var="cp" varStatus="status">
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
			</div>
			<div class="sec">
				<div class="sec-btn">
					<div class="sec-btn-close">
						<a id="closeBtn" href="javascript:void(0)" class="btn btn-normal layui-coupon-btn">关闭</a>
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
							<div title="点击下载"><a href="javascript:void(0)" data-path="{{item.filePath}}"
									data-orgName="{{item.originalFileName}}" data-fileName="{{item.fileName}}"
									onclick="downloadFtpAttach(this)">{{item.originalFileName}}</a></div>
						</div>
						{{#  }); }}
					</div>
				</div>
			</div>
		</script>

		
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		
		<script type="text/javascript">
			$(function(){
				$("#closeBtn").click(function(){
					window.close();
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
                        content: '${ctx}/activity/prewDetailImportData?actId=${activity.id}'
                    });
                    layer.full(index);
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
	</body>
</html>
