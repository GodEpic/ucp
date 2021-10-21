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
			.layui-table-cell {
				white-space: normal;
				word-break: break-all;
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
										<table class="layui-table layui-table-prob" lay-data="{id:'${row.id}',unresize:true}" lay-filter="${row.id}">
											<thead>
											<tr>
												<th lay-data="{type:'numbers', width:80, unresize:true}">序号</th>
												<th lay-data="{field:'type', width:120, align:'center', unresize:true}">类型</th>
												<th lay-data="{field:'remark', width:300, unresize:true}">问题描述</th>
												<th lay-data="{field:'imgSrc', unresize:true}">图片附件</th>
												<th lay-data="{field:'tempId', width:200, align:'center', unresize:true}">反馈情况</th>
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
															<td>
																<c:choose>
																	<c:when test="${qt.ignore eq '0'}">
																		<div class="layui-edit-btn" data-rowId="${qt.id}" data-change="1">
																			<button type="button" class="layui-btn layui-btn-sm layui-btn-disabled">已忽略</button>
																		</div>
																	</c:when>
																	<c:otherwise>
																		<div class="layui-edit-btn" data-rowId="${qt.id}" data-change="${empty qt.feedback ? '0' : '1'}">
																				${qt.feedback}
																		</div>
																	</c:otherwise>
																</c:choose>
															</td>
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
