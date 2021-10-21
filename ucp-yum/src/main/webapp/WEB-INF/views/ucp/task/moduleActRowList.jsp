<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>

<div class="sec-case-form">
	<div class="sec">
		<article class="sec-title t2">券信息</article>
		<div class="sec-form">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>序号</th>
						<th>查看详情</th>
						<th>配置情况</th>
						<th>具体项-配券中心</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lists}" var="rw" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td><a href="${ctx}/task/moduleActRow/rowDetail?id=${rw.id}">查看</a></td>
							<td>未完成</td>
							<td>${rw.configCertifiCenter}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>