<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
		<title>活动测试报告</title>
		
		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
		<link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912" />
		<link rel="stylesheet" href="${static.resource.url}/css/qa.approval.css" />
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		
		<style type="text/css">
			.layui-input-block{
				line-height: 38px;
			}
		</style>
	</head>
	<body class="childBody">
		<form class="layui-form">
			<input name="actId" type="hidden" value="${activity.id}"/>
			<div class="tab-con sec-case-form btn-active">
				<div class="sec">
					<article class="sec-title t2">测试报告</article>
					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title"><b class="label">*</b>核销是否通过：</label>
							<div class="layui-input-block">
								<c:choose>
									<c:when test="${qaReport.verif eq '0'}">
										<input type="radio" name="verif" value="0" lay-skin="primary" title="通过" checked="checked">
										<input type="radio" name="verif" value="1" lay-skin="primary" title="不通过" disabled="disabled">
									</c:when>
									<c:otherwise>
										<input type="radio" name="verif" value="0" lay-skin="primary" title="通过" disabled="disabled">
										<input type="radio" name="verif" value="1" lay-skin="primary" title="不通过" checked="checked">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.verifRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title"><b class="label">*</b>展现是否通过：</label>
							<div class="layui-input-block">
								<c:choose>
									<c:when test="${qaReport.emerge eq '0'}">
										<input type="radio" name="emerge" value="0" lay-skin="primary" title="通过" checked="checked">
										<input type="radio" name="emerge" value="1" lay-skin="primary" title="不通过" disabled="disabled">
									</c:when>
									<c:otherwise>
										<input type="radio" name="emerge" value="0" lay-skin="primary" title="通过" disabled="disabled">
										<input type="radio" name="emerge" value="1" lay-skin="primary" title="不通过" checked="checked">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.emergeRemark}
							</div>
						</div>
					</div>
				</div>
				<div class="sec">
					<article class="sec-title t2">核销类信息</article>
					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">堂食：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="eatinImage" value="0"/>
									<div class="layer-photos attach-view eatinImage" data-image="eatinImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'eatinImage'}">
												<c:set var="eatinImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && eatinImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=eatinImage&fileName=堂食核销图片" class="layui-yum-btn">堂食核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.eatinRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">手机：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="mobileImage" value="0"/>
									<div class="layer-photos attach-view mobileImage" data-image="mobileImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'mobileImage'}">
												<c:set var="mobileImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && mobileImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=mobileImage&fileName=手机核销图片" class="layui-yum-btn">手机核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.mobileRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">外送：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="outwardImage" value="0"/>
									<div class="layer-photos attach-view outwardImage" data-image="outwardImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'outwardImage'}">
												<c:set var="outwardImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && outwardImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=outwardImage&fileName=外送核销图片" class="layui-yum-btn">外送核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.outwardRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">外带：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="takeoutImage" value="0"/>
									<div class="layer-photos attach-view takeoutImage" data-image="takeoutImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'takeoutImage'}">
												<c:set var="takeoutImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && takeoutImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=takeoutImage&fileName=外带核销图片" class="layui-yum-btn">外带核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.takeoutRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">Kiosk：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="kioskImage" value="0"/>
									<div class="layer-photos attach-view kioskImage" data-image="kioskImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'kioskImage'}">
												<c:set var="kioskImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && kioskImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=kioskImage&fileName=Kiosk核销图片" class="layui-yum-btn">Kiosk核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.kioskRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">银二代不满足条件报错图：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="silverSecondImage" value="0"/>
									<div class="layer-photos attach-view silverSecondImage" data-image="silverSecondImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'silverSecondImage'}">
												<c:set var="silverSecondImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && silverSecondImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=silverSecondImage&fileName=银二代不满足条件报错图核销图片" class="layui-yum-btn">银二代不满足条件报错图核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.silverSecondRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">POS小票：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="posImage" value="0"/>
									<div class="layer-photos attach-view posImage" data-image="posImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'posImage'}">
												<c:set var="posImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && posImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=posImage&fileName=POS小票核销图片" class="layui-yum-btn">POS小票核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.posRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">发票：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="invoiceImage" value="0"/>
									<div class="layer-photos attach-view invoiceImage"  data-image="invoiceImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'invoiceImage'}">
												<c:set var="invoiceImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && invoiceImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=invoiceImage&fileName=发票核销图片" class="layui-yum-btn">发票核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.invoiceRemark}
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">其他：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
									<c:set var="otherImage" value="0"/>
									<div class="layer-photos attach-view otherImage"  data-image="otherImage">
										<c:forEach items="${attachList}" var="ath">
											<c:if test="${ath.type eq 'otherImage'}">
												<c:set var="otherImage" value="1"/>
												<div class="layui-upload-item">
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<c:if test="${not empty qaReport && otherImage eq 1}">
									<div class="layui-download-zip">
										<a href="${ctx}/common/downloadZipAttach?reportId=${qaReport.id}&type=otherImage&fileName=其他核销图片" class="layui-yum-btn">其他核销图片下载</a>
									</div>
								</c:if>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								${qaReport.otherRemark}
							</div>
						</div>
					</div>
				</div>
				<div class="sec">
					<div class="sec-btn">
						<div class="sec-btn-close"><button type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="closeWindow" title="关闭">关闭</button></div>
					</div>
				</div>
			</div>
		</form>
		<script type="text/javascript">
			layui.use(['element','form', 'layer'], function(){
			  var table = layui.table,
			  	  elem = layui.element,
			  	  form = layui.form,
			  	  layer = layui.layer;
				
				var active = {
					    closeWindow: function(){ //获取选中数据
					      	window.close();
					    }
				  	};
				
				$('.btn-active .layui-btn').on('click', function(){
					var type = $(this).data('type');
				    active[type] ? active[type].call(this) : '';
				});
				
				$(".layer-photos").each(function(){
					var photoClass = "." + $(this).attr("data-image");
					layer.photos({photos: photoClass, anim: 5});
				});
			});
		</script>
	</body>
</html>
