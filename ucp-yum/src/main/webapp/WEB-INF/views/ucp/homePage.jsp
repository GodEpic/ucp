<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/6/26
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>yum HomePage</title>
</head>
<body class="body_bg">
<style type="text/css">
    html, body, table {
        /*background-color: #f5f5f5;*/
        width: 100%;
        text-align: center;
    }
    body.body_bg{
        background: url(${ctxStatic}/images/login_bg.png) no-repeat;
        background-size: 100% 100%;
    }
    .form-signin-heading {
        font-family: Helvetica, Georgia, Arial, sans-serif, 黑体;
        font-size: 36px;
        margin-bottom: 20px;
        color: #0663a2;
    }

    .form-signin {
        position: absolute;
        right: 100px;
        top: 50%;
        margin-top: -103px;
        text-align: left;
        width: 300px;
        padding: 25px 29px 29px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
    }

    .form-signin .checkbox {
        margin-bottom: 10px;
        color: #0663a2;
    }

    .form-signin .input-label {
        font-size: 16px;
        line-height: 23px;
        color: #999;
    }

    .form-signin .input-block-level {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px;
        *width: 283px;
        *padding-bottom: 0;
        _padding: 7px 7px 9px 7px;
    }

    .form-signin .btn.btn-large {
        font-size: 16px;
    }

    .form-signin #themeSwitch {
        position: absolute;
        right: 15px;
        bottom: 10px;
    }

    .form-signin div.validateCode {
        padding-bottom: 15px;
    }

    .mid {
        vertical-align: middle;
    }

    .header {
        height: 60px;
        /*padding-top: 20px;*/
    }
    .header img{
        width:206px;
        margin-top:7px;
    }
    .alert {
        position: relative;
        width: 300px;
        margin: 0 auto;
        *padding-bottom: 0px;
    }

    label.error {
        background: none;
        width: 270px;
        font-weight: normal;
        color: inherit;
        margin: 0;
    }
    input.login_btn {
        width: 300px;
        height: 40px;
        background: #f1494a;
        color: #fff;
        border: none;
        font-size: 15px;
        font-family: "微软雅黑";
        font-weight: 400;
        border-radius: 5px;
    }
</style>

<div class="header">
    <div class="header-top">
        <div class="logo">
            <a href=""><img src="${ctxStatic}/images/logo.png" alt=""></a>
        </div>
        <div class="clear"></div>
    </div>
    <div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
        <button data-dismiss="alert" class="close">×</button>
        <label id="loginError" class="error">${message}</label>
    </div>
</div>
<form id="loginForm" class="form-signin" action="" method="post">
    <label class="input-label" style="display: inline-block; margin-left:35%;">普通登录</label>
    <input type="radio" class="hp" name="hp" value="1" checked style="margin-top: -2px;">
    <br>
    <label class="input-label" style="margin-top: 30px;display: inline-block;margin-left:35%;">SSO登录</label>
    <input type="radio" class="hp" name="hp" value="2" style="margin-top: -2px;">
    <br>
    <input class="login_btn" id="into" type="button" style="margin-top: 30px"
           value="登 录"/>
</form>
<%--<div class="footer" style="margin-left: 2%">--%>
    <%--<%@include file="/WEB-INF/views/include/footer.jsp" %>--%>
<%--</div>--%>
<script>
    $(document).ready(function () {
        function guid() {
            return 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }

        $("#into").click(function () {
            var hp = $('input:radio[name="hp"]:checked').val();
            if (hp == "1") {
                window.location.href = "${ctx}/login?homePage=" + guid();
            } else {
                window.location.href = "${sso_url}";
            }
        })
    })
</script>
</body>
</html>
