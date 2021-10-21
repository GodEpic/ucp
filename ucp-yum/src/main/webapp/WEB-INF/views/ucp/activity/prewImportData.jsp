<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>



<html lang="en" style="background-color: #FFF;">
	<head lang="en">
	    <title>预览导入信息</title>

	    <link rel="stylesheet" href="${static.resource.url}/css/task.css"/>
	    <link rel="stylesheet" href="${static.resource.url}/css/activity.css">
		<style type="text/css">
			.prew-data .prew-col th{
				min-width: auto;
			}

			table .prew-col th, table tbody td{
				word-break: keep-all;
				white-space: nowrap;
				padding: 5px 10px !important;
				min-width: auto;
				max-width: 350px;
			}
			table tbody td div{
				word-break: initial;
				white-space: nowrap;
				padding: 5px 10px;
			}

		</style>
	</head>
	<body style="background-color: #FFF;">
	
		<div class="tab-con sec-case-form">
			<div class="sec sec-form prew-data" id="table-prew">
				<c:choose>
					<c:when test="${empty sysLists}">
                        <div style="font-weight: bolder;text-align: center;color: coral;">暂无导入信息</div>
					</c:when>
					<c:otherwise>
						<table class="table table-bordered">
							<thead>
								<tr class="prew-sys">
									<th></th>
									<c:forEach items="${sysLists}" var="sys">
										<th colspan="${sys.sysCount}">${sys.sysName}</th>
									</c:forEach>
								</tr>
								<tr class="prew-mod">
									<th></th>
									<c:forEach items="${modLists}" var="md">
										<th colspan="${md.modCount}">${md.modName}</th>
									</c:forEach>
								</tr>
								<tr class="prew-col">
									<th>具体项-配券中心</th>
									<c:forEach items="${columnLists}" var="cmn">
										<th>${cmn.label}</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${rowLists}" var="rwData">
									<tr>
										<td class="${rwData.change eq '1' ? 'value-change' : ''}">${rwData.configCertifiCenter}</td>
										<c:forEach items="${fns:findRowDataByRowId(rwData.id)}" var="cmnData">
											<td class="${cmnData.change eq '1' ? 'value-change' : ''}" >
												<c:choose>
													<c:when test="${cmnData.columnType eq dateType}">
														${cmnData.value}
													</c:when>
													<c:otherwise>
														<div title="${fns:escapeHtml(cmnData.value)}">${fns:abbr(cmnData.value,50)}</div>
													</c:otherwise>
												</c:choose>
											</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</body>
</html>