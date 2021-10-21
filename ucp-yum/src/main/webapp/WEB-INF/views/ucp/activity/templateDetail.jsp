<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>

<html lang="en">
<head lang="en">
    <title>模板详情</title>

    <%--    <link rel="stylesheet" href="${static.resource.url}/css/task.css"/>--%>
    <%--    <link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">--%>
    <%--    <link rel="stylesheet" href="${static.resource.url}/css/activity.css">--%>

    <link rel="stylesheet" href="http://172.25.221.188:8081/static/js/plugs/layui-v2.5.4/css/layui.css">
    <link rel="stylesheet" href="http://172.25.221.188:8081/static/css/activity.css">
    <link rel="stylesheet" href="http://172.25.221.188:8081/static/css/qa.reject.css"/>

    <style type="text/css">
        .sec-case-form {
            width: 80%;
            margin: auto;
        }

        .layui-form-item {
            display: inline-block;
            float: none;
        }

        .layui-input-block {
            line-height: 36px;
            margin-left: 150px;
        }

        .layui-form-label {
            width: 120px;
        }

        .layui-table-view {
            border: none;
        }

        .layui-table-view .layui-table td, .layui-table-view .layui-table th {
            border: none;
        }

        .sec-form .table th {
            border: none;
        }

        .import-desc {
            font-size: 16px;
            color: red;
            font-weight: bolder;
        }
    </style>
</head>
<body>
<div class="tab-con sec-case-form">
    <div class="sec">
        <article class="sec-title t2">模板信息</article>
        <div class="sec-form layui-row">
            <div class="layui-col-xs10 layui-form-item">
                <label class="layui-form-label layui-form-title"><b class="label">*</b>模板名称：</label>
                <div class="layui-input-block">
                    ${template.templateName}
                </div>
            </div>
        </div>
        <div class="sec-form layui-row">
            <div class="layui-col-xs5 layui-form-item">
                <label class="layui-form-label layui-form-title"><b class="label">*</b>品牌：</label>
                <div class="layui-input-block">
                    ${fns:getDictLabel(template.brand, 'brand', '')}
                </div>
            </div>
            <div class="layui-col-xs5 layui-form-item">
                <label class="layui-form-label layui-form-title"><b class="label">*</b>附件：</label>
                <div class="layui-input-block">
                    <td><a href="${template.filePath}" title="${template.fileName}">${template.fileName}</a><br></td>
                </div>
            </div>
        </div>
    </div>
    <div class="sec">
        <div class="sec-btn">
            <div class="sec-btn-close">
                <a id="closeBtn" href="javascript:void(0)" class="btn btn-normal layui-coupon-btn">关闭</a>
            </div>
        </div>
    </div>
</div>
<%--<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>--%>
<script src="http://172.25.221.188:8081/static/js/plugs/layui-v2.5.4/layui.js"></script>
</body>
<script type="text/javascript">
    $("#closeBtn").click(function () {
        window.parent.close();
    });
</script>
</html>
