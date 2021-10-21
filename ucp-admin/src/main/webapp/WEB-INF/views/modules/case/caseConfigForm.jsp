<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>case配置管理</title>
	<meta name="decorator" content="default"/>

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/case/dscCaseConfig/">case配置列表</a></li>
		<li class="active"><a href="${ctx}/case/dscCaseConfig/form?id=${dscCaseConfig.id}">case配置${not empty dscCaseConfig.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="dscCaseConfig" action="${ctx}/case/dscCaseConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">caseCode:</label>
			<div class="controls">
				<form:input path="caseCode" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">查看url:</label>
			<div class="controls">
				<form:input path="viewPage" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">lv1处理页面:</label>
			<div class="controls">
				<form:input path="lv1Page" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">lv2处理页面:</label>
			<div class="controls">
				<form:input path="lv2Page" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">case类型:</label>
			<div class="controls">
				<form:select path="caseType" class="input-medium">
					<form:option value="0">紧急</form:option>
					<form:option value="1">非紧急</form:option>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>