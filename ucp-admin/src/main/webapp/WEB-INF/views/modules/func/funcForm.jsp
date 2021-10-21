<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>功能管理</title>
	<meta name="decorator" content="default"/>

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/func/dscLinkFunction">功能管理</a></li>
		<li class="active"><a href="${ctx}/func/dscLinkFunction/form?id=${dscLinkFunction.id}">功能${not empty dscLinkFunction.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="dscLinkFunction" action="${ctx}/func/dscLinkFunction/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">功能名:</label>
			<div class="controls">
				<form:input path="funTitle" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">功能编码:</label>
			<div class="controls">
				<form:input path="funCode" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">功能类型:</label>
			<div class="controls">
				<form:select path="funType" class="input-medium">
					<%--<form:option value="0">接口模块</form:option>--%>
					<form:option value="1">外链系统</form:option>
					<%--<form:option value="2">知识库 </form:option>--%>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">外链地址:</label>
			<div class="controls">
				<form:input path="linkUrl" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">外链参数:</label>
			<div class="controls">
				<form:input path="linkParams" htmlEscape="false" maxlength="200" class="required" size="100"/>
			</div>
		</div><div class="control-group">
			<label class="control-label"> 知识库参数 :</label>
			<div class="controls">
				<form:input path="repoParams" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"> 外链图标 :</label>
			<div class="controls">
				<form:input path="linkImg" htmlEscape="false" maxlength="200" class="required" size="100"/>
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