<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
		<title>活动反馈报告</title>
		
		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
		<link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912" />
		<link rel="stylesheet" href="${static.resource.url}/css/qa.reject.css" />
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		
		<style type="text/css">
			.layui-input-block{
				line-height: 38px;
			}
		</style>
	</head>
	<body class="childBody">
		<div class="tab-con sec-case-form btn-active">
			<div class="sec">
				<article class="sec-title t2">反馈基本信息</article>
				<div class="sec-form">
					<div class="sec-tbl layui-module-row">
						<table class="layui-table" lay-data="{id:'moduleRowTab'}" lay-filter="moduleRowTab">
							<thead>
								<tr>
									<th lay-data="{type:'numbers', width:80, unresize:true}">序号</th>
									<th lay-data="{field:'configCertifiCenter', unresize:true}">具体项-配券中心</th>
									<th lay-data="{field:'approval', unresize:true}">通过</th>
									<th lay-data="{field:'reject', unresize:true}">不通过</th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach items="${moduleRowResult}" var="row">
									<tr>
										<td></td>
										<td>${row.configCertifiCenter}</td>
										<td>
											<c:choose>
												<c:when test="${row.qaResult > 0}">
													<input class="checkbox-disabled" type="checkbox" name="layTableCheckbox" lay-skin="primary" disabled>
												</c:when>
												<c:otherwise>
													<input class="checkbox-disabled" type="checkbox" name="layTableCheckbox" checked="checked" lay-skin="primary" disabled>
												</c:otherwise>
											</c:choose>
										</td>

										<td>
											<c:choose>
												<c:when test="${row.qaResult == 0}">
													<input class="checkbox-disabled" type="checkbox" name="layTableCheckbox" lay-skin="primary" disabled>
												</c:when>
												<c:otherwise>
													<input class="checkbox-disabled" type="checkbox" name="layTableCheckbox" checked="checked" lay-skin="primary" disabled>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="sec">
				<div class="sec-top">
					<article class="sec-title t2">问题券明细</article>
				</div>
				<div class="sec-form">
                    <div class="sec-form-file">
                        <div class="layui-form-item">
                            <label class="layui-form-label layui-form-title"><b class="label">*</b>检查表：</label>
                            <div class="layui-input-block">
                                <c:choose>
                                    <c:when test="${not empty reportAttach}">
										<a href="javascript:void(0)" data-orgName="${reportAttach.originalFileName}" data-fileName="${reportAttach.fileName}" onclick="downloadAttach(this)">${reportAttach.originalFileName}</a>
                                    </c:when>
                                    <c:otherwise>

                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </div>
					<div class="layui-collapse" lay-filter="ucpCollapse">
						<c:forEach items="${moduleRowResult}" var="row">
							<c:if test="${row.qaResult > 0}">
								<div class="layui-colla-item" data-rowId="${row.id}">
									<h2 class="layui-colla-title">
										${row.configCertifiCenter}
									</h2>
									<div class="layui-colla-content">
										<table class="layui-table layui-table-prob" lay-data="{id:'${row.id}'}" lay-filter="${row.id}">
											<thead>
											<tr>
												<th lay-data="{type:'numbers', width:80, unresize:true}">序号</th>
												<th lay-data="{field:'type', width:120, align:'center', unresize:true}">类型</th>
												<th lay-data="{field:'remark', width:300, unresize:true}">问题描述</th>
												<th lay-data="{field:'imgSrc', unresize:true}">图片附件</th>
												<c:if test="${activity.status eq actStatusNotPass && currUserId eq activity.createBy.id}">
													<th lay-data="{field:'tempId', width:200, align:'center', unresize:true}">操作</th>
												</c:if>
											</tr>
											</thead>

											<tbody>
												<c:forEach items="${qaCheckList}" var="qt" varStatus="qtIndex">
													<c:if test="${qt.couponId eq row.id}">
														<tr>
															<td></td>
															<td>${fns:getDictLabel(qt.type, 'qa_check_type', '')}</td>
															<td>${qt.description}</td>
															<td>
																<div class="layui-item-value layui-label-color">
																	<div class="layui-upload-list">
																		<div class="layer-photos attach-view ${qt.id}-${qtIndex.index}" data-rowId="${qt.id}-${qtIndex.index}">
																			<c:forEach items="${fns:findListByClassAndType(qaCheckClassName, qt.id, '')}" var="attach">
																				<div class="layui-upload-item">
																					<img alt="${attach.originalFileName}" src="${attach.filePath}" class="layui-upload-img">
																				</div>
																			</c:forEach>
																		</div>
																	</div>
																</div>
															</td>
															<c:if test="${activity.status eq actStatusNotPass && currUserId eq activity.createBy.id}">
																<td>
																	<c:choose>
																		<c:when test="${qt.ignore eq '0'}">
																			<div class="layui-edit-btn" data-rowId="${qt.id}" data-change="1">
																				<button type="button" class="layui-btn layui-btn-sm layui-btn-disabled" disabled>已忽略</button>
																			</div>
																		</c:when>
																		<c:otherwise>
																			<div class="layui-edit-btn" data-rowId="${qt.id}" data-change="${empty qt.feedback ? '0' : '1'}">
																				<button type="button" class="layui-btn layui-btn-sm layui-row-btn" data-type="saveIgnore">忽略</button>
																				<button type="button" class="layui-btn layui-btn-sm layui-row-btn" data-title="${row.configCertifiCenter}" data-type="saveFeedBack">反馈</button>
																				<input type="hidden" value="${qt.feedback}">
																			</div>
																		</c:otherwise>
																	</c:choose>
																</td>
															</c:if>
														</tr>
													</c:if>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
			<div class="sec">
				<div class="sec-btn">

					<c:if test="${activity.status eq actStatusNotPass && currUserId eq activity.createBy.id}">
						<div class="sec-btn-submit"><button type="button" class="layui-btn layui-btn-sm layui-row-btn" data-type="submitQa" title="保存">提交至QA</button></div>
					</c:if>
					<div class="sec-btn-close"><button type="button" class="layui-btn layui-btn-sm layui-row-btn" data-type="closeWindow" title="关闭">关闭</button></div>
				</div>
			</div>
		</div>
		
		
		<script type="text/javascript">
			layui.use(['table','element','form', 'layer'], function(){
			  var layer = layui.layer;
			  	
			  	$(".layui-table-prob").empty();
			  	
			  	var active = {
					closeWindow: function(){ //获取选中数据
						window.close();
					},
					saveIgnore: function(){
						var btn = $(this),
                            tr = btn.closest("tr");
                        tr.removeClass('layui-border');
						layer.confirm('确定要忽略该问题吗？', function(index) {
							var rowId = btn.parent().attr("data-rowId");
							var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
							$.ajax({
								url: "${ctx}/activity/qa/saveIgnoreCheck",
								type: "POST",
								data: {
									"qaCheckId" : rowId
								},
								async : false,
								success: function (data) {
									layer.close(mask);
									if(data.success || data.success === 'true'){
										btn.parent().attr('data-change', '1');
										btn.parent().empty().append('<button type="button" class="layui-btn layui-btn-sm layui-btn-disabled" disabled>已忽略</button>');
										layer.msg("保存成功");
									} else {
										layer.msg("保存失败");
									}
								},
								error:function(data){
									layer.close(mask);
									layer.msg("保存失败");
								}
							});
							layer.close(index);
						});
					},
					saveFeedBack :function(){
						var btn = $(this),
							tr = btn.closest("tr"),
							rowId = btn.parent().attr("data-rowId"),
							input = btn.next();

                        tr.removeClass('layui-border');

						layer.open({
							title: "具体项-配券中心【" + btn.attr("data-title") + "】问题【"+ tr.find(".laytable-cell-numbers").html() +"】反馈信息",
							shade: 0,
							area: ["60%", "40%"],
							maxmin: false,
							shade: [0.8, '#393D49'],
							content: "<textarea name='feedback' class='layui-textarea' style='height:80%;'>" + input.val() + "</textarea>",
							btn: ["保存", "关闭"],
							btn1 : function(layero, index){
								var feedBack = $(".layui-layer-content").find('textarea').val();
								var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
								$.ajax({
									url: "${ctx}/activity/qa/saveFeedBack",
									type: "POST",
									data: {
										"qaCheckId" : rowId,
										"feedBack" : feedBack
									},
									async : false,
									success: function (data) {
										layer.close(mask);
										if(data.success || data.success === 'true'){
											btn.parent().attr('data-change', '1');
											input.val(feedBack);
											layer.msg("保存成功");
										} else {
											layer.msg("保存失败");
										}
									},
									error:function(data){
										layer.close(mask);
										layer.msg("保存失败");
									}
								});
								layer.close(layero);
							},
							btn2 : function(layero, index){
								layer.close(layero);
							}
						});
					},
					submitQa :function(){
						layer.confirm('确定要提交至QA吗？', function(index) {
						    var allChange = true;
							$('.layui-edit-btn').each(function(){
							    var btnDiv = $(this);
								var dataChange = btnDiv.attr("data-change");
								if(dataChange == 0){
                                    allChange = false;
                                    var tr = btnDiv.closest('tr'),
										colla = btnDiv.closest('.layui-colla-content');

                                    tr.addClass('layui-border');
                                    if(!colla.hasClass('layui-show')){
                                        colla.addClass('layui-show');
									}
								}
							});

							if(allChange){
                                var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                                $.ajax({
                                    url: "${ctx}/activity/qa/submitToQa",
                                    type: "POST",
                                    data: {
                                        "actId" : "${actId}"
                                    },
                                    async : false,
                                    success: function (data) {
                                        layer.close(mask);
                                        if(data.success || data.success === 'true'){
                                            layer.open({
                                                title: '信息',
                                                skin: 'layui-layer-lan',
                                                content: "已提交至QA！",
                                                btn:["确定"],
                                                closeBtn : false,
                                                success : function(){

                                                },
                                                btn1 : function(ind){
                                                    layer.close(ind);
                                                    window.close();
                                                }
                                            });
                                        } else {
                                            layer.msg(data.message);
                                        }
                                    },
                                    error:function(){
                                        layer.close(mask);
                                        layer.msg("提交失败");
                                    }
                                });
							} else {
                                layer.msg("您还有未反馈的问题，请反馈完成之后再提交至QA");
							}
							layer.close(index);
						});
					}
				};
				
				$('.btn-active .layui-btn').on('click', function(){
					var type = $(this).data('type');
				    active[type] ? active[type].call(this) : '';
				});
			  	
			  	$(".layui-colla-item .layui-form .attach-view").each(function(){
			  		var rowId = "." + $(this).attr("data-rowId");
			  		layer.photos({photos: rowId, anim: 5});
			  	});
			});
		</script>
	</body>
</html>
