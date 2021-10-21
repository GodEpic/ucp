<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
	    <title>任务信息详情页</title>
	    
	    <link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912"/>

		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>

		<style type="text/css">
			.layui-collapse{
				border: none;
			}

			.layui-colla-item:first-child{
				border: 1px solid #E6E6E6;
			}

			.layui-colla-item{
				margin: 10px 0;
				border: 1px solid #E6E6E6;
				border-radius: 3px;
			}

			.layui-colla-title{
				padding-left: 15px;
			}

			.layui-tab-title .layui-this:after{
				top: 1px;
			}

			.layui-colla-icon{
				left: auto;
				right: 15px;
			}

			.sec-case-form{
				width: 80%;
			}

			/*body, html{
				overflow: scroll;
			}*/

			.layui-task-input{
				width: 100%;
				height: 100%;
				text-align: center;
			}

			.layui-dialog-textarea{
				width: 90%;
				height: 80%;
				padding: 10px;
				margin: auto;
				border-color: #e6e6e6;
			}

			.layui-task-info{
				margin-bottom: 10px;
				font-size: 18px;
				font-weight: bolder;
			}
		</style>
	</head>
	<body>

		<div class="tab-con sec-case-form">
			<div class="sec sec-link">
				<article class="sec-title t2">各券系统链接</article>
				<div class="sec-form">
					<div>商城系统：<a target="_blank" href="https://172.18.50.144/logon/control/logout@">https://172.18.50.144/logon/control/logout@</a></div>
				</div>
				<div class="sec-form">
					<div>券系统：<a target="_blank" href="http://172.31.50.208/couponMgmt/servlet/QueryProfileServlet">http://172.31.50.208/couponMgmt/servlet/QueryProfileServlet</a></div>
				</div>
				<div class="sec-form">
					<div>Prime系统：<a target="_blank" href="http://172.31.50.208/couponMgmt/servlet/QueryProfileServlet">http://172.31.50.208/couponMgmt/servlet/QueryProfileServlet</a></div>
				</div>
			</div>
			
			<div class="sec">
				<article class="sec-title t2">字段信息</article>
				<div class="sec-form">
					<div class="layui-tab">
						<ul class="layui-tab-title">
							<c:forEach items="${sysMap}" var="sys" varStatus="status">
								<li class="${status.first ? 'layui-this' : ''}">${sysMap[sys.key].sysName}</li>
							</c:forEach>
						</ul>

						<div class="layui-tab-content">
							<c:forEach items="${modMap}" var="modList" varStatus="status">
								<div class="layui-tab-item ${status.first ? 'layui-show' : ''}">
									<div class="sys-change-count">已修改字段数：<span style="color: red;font-weight: bolder">${sysMap[modList.key].sysCount}</span></div>
									<div class="layui-collapse">
										<c:forEach items="${modMap[modList.key]}" var="md" varStatus="mdStatus">
											<div class="layui-colla-item">
												<h2 class="layui-colla-title">${md.name} (已修改字段数：<span style="color: red;font-weight: bolder">${md.changeCount}</span>)</h2>
												<div class="layui-colla-content">
													<table class="layui-table table table-bordered">
														<thead>
															<tr>
																<th style="width: 300px;">名称</th>
																<th>内容</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td style="font-weight: bolder">具体项-配券中心</td>
																<td class="${row.change eq '1' ? 'value-change' : ''}">${row.configCertifiCenter}</td>
															</tr>
															<c:forEach items="${fns:findRowDataByModIdAndRowId(md.id, row.id)}" var="rwData">
																<tr>
																	<td style="font-weight: bolder">${rwData.columnLabel}</td>
																	<td class="${rwData.change eq '1' ? 'value-change' : ''}" style="text-align: left">
																			${fns:replaceEnter(rwData.value)}
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				
				<div class="sec-btn">
					<c:if test="${type eq configType && rowStatus eq row.configStatus && not empty taskReceive && taskReceive.receive == agent}">
						<a id="configBtn" href="javascript:void(0)" class="layui-yum-btn">配置完成并关闭</a>
					</c:if>
					<c:if test="${type eq checkType && rowStatus eq row.checkStatus && not empty taskReceive && taskReceive.receive == agent}">
						<a id="checkBtn" href="javascript:void(0)" class="layui-yum-btn">检查完成并关闭</a>
					</c:if>
				</div>
			</div>
		</div>
		<div class="layui-hide" id="dialogContent">
			<div class="layui-task-input">
				<c:choose>
					<c:when test="${type eq configType}">
						<div class="layui-task-info">确定配置完成并关闭？</div>
						<textarea name="inputVal" class="'layui-textarea layui-dialog-textarea" placeholder="请输入活动定义(ActivityId)">${inputVal}</textarea>
					</c:when>
					<c:otherwise>
						<div class="layui-task-info">确定检查完成并关闭？</div>
						<textarea name="inputVal" class="'layui-textarea layui-dialog-textarea" placeholder="请输入测试账户(TestAccount)">${inputVal}</textarea>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<script type="text/javascript">
            layui.use('element', function() {
                var element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
            });
			$(function(){
				var parentIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
				
				$("#configBtn").click(function(){
					closeCurrPage("${configType}", "确定配置完成并关闭？");
				});
				
				$("#checkBtn").click(function(){
					closeCurrPage("${checkType}", "确定检查完成并关闭？");
				});
				
				function closeCurrPage(type,msg){
                    layer.open({
                        title: "信息",
                        content: $("#dialogContent").html(),
                        btn:["确定","取消"],
                        area : ["400px", "300px"],
                        closeBtn : false,
                        success : function(index, layero){
                        },
                        btn1 : function(index, layero){
                            var inputVal = $(layero).find("textarea[name='inputVal']").val();

                            inputVal = inputVal.replace(/^\s+|\s+$/g,"");

                            if(inputVal && inputVal.length > 0){
                                $.ajax({
                                    url: "${ctx}/task/moduleActRow/updateStatus",
                                    type: "POST",
                                    data: {
                                        "type" : type,
                                        "rowId" : "${row.id}",
										"inputVal" : inputVal
                                    },
                                    dataType : "JSON",
                                    success: function (data) {
                                        var obj = data.obj;
                                        if(obj.errcode === '0'){
                                            parent.$('#${row.id}').html("已完成");
                                            parent.layer.close(parentIndex);
                                        } else {
                                            Common.myAlert(obj.errmsg);
                                        }
                                    }
                                });
                                layer.close(index);
                            } else {
                                Common.myAlert("请输入信息");
                                return false;
                            }
                        },
                        btn2 : function(){
                        }
                    });


                    /*layer.confirm(msg, function(index) {
                        $.ajax({
                            url: "/task/moduleActRow/updateStatus",
                            type: "POST",
                            data: {
                                "type" : type,
                                "rowId" : ""
                            },
                            dataType : "JSON",
                            success: function (data) {
                                var obj = data.obj;
                                if(obj.errcode === '0'){
                                    parent.$('#').html("已完成");
                                    parent.layer.close(parentIndex);
                                } else {
                                    Common.myAlert(obj.errmsg);
                                }
                            }
                        });
                        layer.close(index);
                    });*/
				}
			});
		</script>
	</body>
</html>