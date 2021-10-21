<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<link href="${ctxStatic}/summernote/dist/summernote.css" rel="stylesheet"/>
	<script src="${ctxStatic}/summernote/dist/summernote.js" type="text/javascript"></script>
	<script src="${ctxStatic}/summernote/dist/lang/summernote-zh-CN.js"></script>
	<script type="text/javascript">

		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/role/checkName?id=" + encodeURIComponent("${role.id}")}
				},
				messages: {
					name: {remote: "角色名已存在"},
				},
				submitHandler: function(form){
					var ids = [], nodes = tree.getCheckedNodes(true);
                    var code = $('.summernote').summernote('code');
                    $("#announce").val(code);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					if(ids.length==0){
						alert("请选择角色授权");
						return false;
					}
					$("#menuIds").val(ids);

					var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
					for(var i=0; i<nodes2.length; i++) {
						ids2.push(nodes2[i].id);
					}
					$("#officeIds").val(ids2);
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
			
			// 用户-菜单
			var zNodes=[
					<c:forEach items="${menuList}" var="menu">{id:"${menu.id}", pId:"${not empty menu.parent.id?menu.parent.id:0}", name:"${menu.name}"},
		            </c:forEach>];
			// 初始化树结构
			console.log(zNodes);
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 不选择父节点
			tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
			// 默认选择节点
			var ids = "${role.menuIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			tree.expandAll(true);

            $('.summernote').summernote({
                height: 200,
                tabsize: 2,
                lang: 'zh-CN'
            });
            var markupStr = $("#announce").val();
            $('.summernote').summernote('code', markupStr);
		})
			

	</script>
	<style>
		.modal{
			top:-500%;
		}
	</style>
	<script>
        $(function () {
            $('.modal ').remove();
            $('.modal-backdrop ').remove();
        })
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<li class="active"><a href="${ctx}/sys/role/form?id=${role.id}">角色<c:if test="${role.id!=null}">修改</c:if><c:if test="${role.id==null}">增加</c:if></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="announce"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">角色名称2:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${role.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">英文名称:</label>--%>
			<%--<div class="controls">--%>
				<%--<input id="oldEnname" name="oldEnname" type="hidden" value="${role.enname}">--%>
				<%--<form:input path="enname" htmlEscape="false" maxlength="50" class="required"/>--%>
				<%--<span class="help-inline"><font color="red">*</font>  </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">角色类型:</label>
			<div class="controls">
				<form:select path="roleType" class="input-medium">
					<form:option value="1">Level1</form:option>
					<form:option value="2">Level2</form:option>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否可用</label>
			<div class="controls">
				<form:select path="useable" class="input-medium">
					<form:option value="1">是</form:option>
					<form:option value="0">否</form:option>
				</form:select>
				<span class="help-inline">“是”代表此数据可用，“否”则表示此数据不可用</span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">角色授权:</label>
			<div class="controls">
				<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
				<form:hidden path="menuIds"/>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公告:</label>
			<div class="controls">
				<div class="m">
					<div class="summernote"></div>
				</div>
			</div>
		</div>
		<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
