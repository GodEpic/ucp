<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
	<head lang="en">
		<title>测试报告</title>
		
		<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
		<link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912" />
		<link rel="stylesheet" href="${static.resource.url}/css/qa.approval.css" />
		<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
		
		<style>
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
		</style>
	</head>
	<body class="childBody">
		<form class="layui-form">
			<input name="actId" type="hidden" value="${activity.id}"/>
            <input name="id" type="hidden" value="${qaReport.id}"/>
			<div class="tab-con sec-case-form btn-active">
				<div class="sec">
					<article class="sec-title t2">测试报告</article>
					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title"><b class="label">*</b>核销是否通过：</label>
							<div class="layui-input-block">
								<c:choose>
									<c:when test="${empty qaReport || qaReport.verif eq '0'}">
										<input type="radio" name="verif" value="0" lay-skin="primary" title="通过" checked="checked">
										<input type="radio" name="verif" value="1" lay-skin="primary" title="不通过">
									</c:when>
									<c:otherwise>
										<input type="radio" name="verif" value="0" lay-skin="primary" title="通过">
										<input type="radio" name="verif" value="1" lay-skin="primary" title="不通过" checked="checked">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								<textarea name="verifRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.verifRemark}</textarea>
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title"><b class="label">*</b>展现是否通过：</label>
							<div class="layui-input-block">
								<c:choose>
									<c:when test="${empty qaReport || qaReport.verif eq '0'}">
										<input type="radio" name="emerge" value="0" lay-skin="primary" title="通过" checked="checked">
										<input type="radio" name="emerge" value="1" lay-skin="primary" title="不通过">
									</c:when>
									<c:otherwise>
										<input type="radio" name="emerge" value="0" lay-skin="primary" title="通过">
										<input type="radio" name="emerge" value="1" lay-skin="primary" title="不通过" checked="checked">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">备注：</label>
							<div class="layui-input-block">
								<textarea name="emergeRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.emergeRemark}</textarea>
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
													<a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
													<img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
												</div>
											</c:if>
										</c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
									</div>
									<input name="eatinImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="eatinRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.eatinRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>

									<input name="mobileImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="mobileRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.mobileRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="outwardImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="outwardRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.outwardRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="takeoutImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="takeoutRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.takeoutRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="kioskImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="kioskRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.kioskRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="silverSecondImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="silverSecondRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.silverSecondRemark}</textarea>
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
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="posImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="posRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.posRemark}</textarea>
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">发票：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
                                    <c:set var="invoiceImage" value="0"/>
                                    <div class="layer-photos attach-view invoiceImage" data-image="invoiceImage">
                                        <c:forEach items="${attachList}" var="ath">
                                            <c:if test="${ath.type eq 'invoiceImage'}">
                                                <c:set var="invoiceImage" value="1"/>
                                                <div class="layui-upload-item">
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="invoiceImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="invoiceRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.invoiceRemark}</textarea>
							</div>
						</div>
					</div>

					<div class="sec-form">
						<div class="layui-form-item">
							<label class="layui-form-label layui-form-title">其他：</label>
							<div class="layui-input-block">
								<div class="layui-upload-list">
                                    <c:set var="otherImage" value="0"/>
                                    <div class="layer-photos attach-view otherImage" data-image="otherImage">
                                        <c:forEach items="${attachList}" var="ath">
                                            <c:if test="${ath.type eq 'otherImage'}">
                                                <c:set var="otherImage" value="1"/>
                                                <div class="layui-upload-item">
                                                    <a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
                                                    <img alt="${ath.originalFileName}" src="${ath.filePath}" class="layui-upload-img">
                                                    <input alt="${ath.originalFileName}" type="hidden" value='{"fileName":"${ath.fileName}","filePath":"${ath.filePath}","originalFileName":"${ath.originalFileName}","fileSize":${ath.fileSize},"ext":"${ath.extension}"}'/>
                                                </div>
                                            </c:if>
                                        </c:forEach>

                                        <div class="upload_btn">
                                            <button type="button" class="layui-upload-btn"></button>
                                        </div>
                                    </div>
									<input name="otherImage" type="hidden" class="layui-upload-input"/>
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
								<textarea name="otherRemark" placeholder="请输入内容" class="layui-textarea">${qaReport.otherRemark}</textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="sec">
					<div class="sec-btn">
						<c:if test="${activity.receiveUser eq agent && (activity.status eq actStatusTesting || activity.status eq actStatusNotPass || activity.status eq actStatusTestFeedBack)}">
							<div class="sec-btn-submit"><button type="button" class="layui-btn layui-btn-sm layui-coupon-btn" lay-submit lay-filter="saveDraftApproval" title="保存">保存</button></div>
						</c:if>

						<c:if test="${activity.receiveUser eq agent && (activity.status eq actStatusTesting || activity.status eq actStatusTestFeedBack)}">
							<div class="sec-btn-submit"><button type="button" class="layui-btn layui-btn-sm layui-coupon-btn" lay-submit lay-filter="saveApproval" title="结案">结案</button></div>
						</c:if>

						<div class="sec-btn-close"><button id="closeWindow" type="button" class="layui-btn layui-btn-sm layui-btn-close" title="关闭">关闭</button></div>
					</div>
				</div>
			</div>
		</form>
		<script type="text/javascript">
            sessionStorage.setItem("need-refresh", true);

			layui.use(['form', 'upload', 'layer'], function(){
				var form = layui.form,
					upload = layui.upload,
					layer = layui.layer;
				
				$(".layui-upload-list").find(".layui-upload-btn").each(function(){
					var _t = $(this);
					var mask = "";
					var up = upload.render({
						elem: _t.parent(),
						url: '${ctx}/common/uploadFtpAttach',
						multiple: true,
						accept:"images",
						exts:"jpg|png|gif|bmp|jpeg|xlsx|msg|zip",
						acceptMime:'image/*|application/*',
						auto : true,
						before:function(obj){
							 mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
						},
						done : function(res){
							layer.close(mask);
							var obj = res.obj;
							if(obj.errcode === '0'){
								var item = _t.parent().parent();
								_t.parent().before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><img src="'+ obj.filePath +'" class="layui-upload-img" onclick="previewImage(this)"></div>');
								
								var lastUpload = _t.parent().parent().find(".layui-upload-item:last");
								var input = "<input alt='" + obj.originalFileName + "' type='hidden' value='" + JSON.stringify(obj) + "'>";
								lastUpload.append(input);
								
								previewImage(item);
								getAttach();
								layer.msg('上传成功');
		                    } else {
		                    	layer.msg('上传失败');
		                    }
							up.config.elem.next()[0].value = '';
						},
						error: function(index, upload){
							layer.close(mask);
		                	layer.msg('上传失败');
		                	up.config.elem.next()[0].value = '';
						}
					});
				});

                form.on('submit(saveDraftApproval)', function(data) {
                    layer.confirm('确定要保存吗？', function(index) {
                        var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        $.ajax({
                            type: "POST",
                            url: "${ctx}/activity/qa/saveDraftApproval",
                            data: data.field,
                            async : false,
                            dataType: "json",
                            success: function (data) {
                                layer.close(mask);
                                if(data.success || data.success === 'true'){
                                    layer.open({
                                        title: '信息',
                                        skin: 'layui-layer-lan',
                                        content: "保存成功！",
                                        btn:["确定"],
                                        closeBtn : false,
                                        success : function(){

                                        },
                                        btn1 : function(){
                                            window.close();
                                        }
                                    });
                                } else {
                                    if(data.message){
                                        layer.alert(data.message);
                                    } else {
                                        layer.msg("保存失败");
                                    }
                                }
                            },
                            error:function(data){
                                layer.close(mask);
                                layer.msg("保存失败");
                            }
                        });
                        layer.close(index);
                    });
                });
				
				form.on('submit(saveApproval)', function(data) {
                    layer.confirm('确定要结案吗？', function(index) {
                        var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                        $.ajax({
                            type: "POST",
                            url: "${ctx}/activity/qa/saveApproval",
                            data: data.field,
                            async : false,
                            dataType: "json",
                            success: function (data) {
                                layer.close(mask);
                                if(data.success || data.success === 'true'){
                                    layer.open({
                                        title: '信息',
                                        skin: 'layui-layer-lan',
                                        content: "结案成功！",
                                        btn:["确定"],
                                        closeBtn : false,
                                        success : function(){

                                        },
                                        btn1 : function(){
                                            window.close();
                                        }
                                    });
                                } else {
                                    if(data.message){
                                        layer.alert(data.message);
                                    } else {
                                        layer.msg("结案失败");
                                    }
                                }
                            },
                            error:function(data){
                                layer.close(mask);
                                layer.msg("结案失败");
                            }
                        });
                        layer.close(index);
                    });
				});

                $(".layer-photos").each(function(){
                    var photoClass = "." + $(this).attr("data-image");
                    layer.photos({photos: photoClass, anim: 5});
                });

                getAttach();
			});
			
			$(function(){
				$("#closeWindow").click(function(){
					if (navigator.userAgent.indexOf("Firefox") != -1 || navigator.userAgent.indexOf("Chrome") !=-1) {
				        window.location.href="about:blank";
				        window.close();
				    } else {
				        window.opener = null;
				        window.open("", "_self");
				        window.close();
				    }
				});
			});
			
			function previewImage(item){
                var photoClass = "." + item.attr("data-image");
                layer.photos({photos: photoClass, anim: 5});
			}
			
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
				getAttach();
			}
			
			function getAttach(){
				$(".layui-upload-list").each(function(){
					var lists = $(this);
					var imgArray = new Array();
					lists.find(".layui-upload-item input").each(function(index){
						imgArray[index] = JSON.parse($(this).val());
					});
					lists.find('.layui-upload-input').val(JSON.stringify(imgArray));
				});
			}
		</script>
	</body>
</html>
