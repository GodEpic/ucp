<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>yum 登录</title>
    <meta name="decorator" content="blank"/>
    <style type="text/css">
        html, body, table {
            /*background-color: #f5f5f5;*/
            width: 100%;
            text-align: center;
        }
        body.body_bg{
            background: url(${ctxStatic}/images/login_bg.png) no-repeat;
            background-size: 100% 100%;
            height: 100%;
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
            margin-top: -140px;
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
            font-size: 15px;
            line-height: 23px;
            color: #666;
            text-align: center;
            font-weight: 600;
            margin-bottom: 15px;
        }

        .form-signin .input-block-level {

            font-size: 14px;
            height: 40px;
            margin-bottom: 15px;
            padding: 7px 7px 7px 45px;
        }
        .input-div{
            position: relative;
        }
        .form-signin .input_username:before{
            content: '';
            display: block;
            position: absolute;
            background: url(${ctxStatic}/images/ico_person.png) no-repeat;
            background-size: 20px 20px;
            height:20px;
            width:20px;
            top:8px;
            left:12px;
            z-index:99;
        }
        .form-signin .input_pwd:before{
            content: '';
            display: block;
            position: absolute;
            background: url(${ctxStatic}/images/ico_pwd.png) no-repeat;
            background-size: 20px 20px;
            height:20px;
            width:20px;
            top:8px;
            left:12px;
            z-index:99;
        }
        .form-signin .btn.btn-large {
            font-size: 15px;
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
            height: 80px;
            padding-top: 20px;
        }


        .alert {
            position: absolute;
            top: 30%;
            right: 105px;
            width: 300px;
            z-index: 22;
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
        input.ssologin_btn {
            border: none;
            margin-top: 15px;
            text-align: right;
            width: 75px;
            color: #666;
            font-size: 13px;
            float: right;
            cursor: pointer;
        }
        input.ssologin_btn:hover{
            color:#f1494a;
        }

        .login-logo{
            width: 400px;
            margin-top: 2%;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#loginForm").validate({
                rules: {
                    validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
                },
                messages: {
                    username: {required: "请填写用户名."}, password: {required: "请填写密码."},
                    validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
                },
                errorLabelContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    error.appendTo($("#loginError").parent());
                }
            });

            $(".ssologin_btn").click(function () {
                window.location.href = "${sso_url}";
            })
        });
        // 如果在框架或在对话框中，则弹出提示并跳转到首页
        if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
            alert('未登录或登录超时。请重新登录，谢谢！');
            top.location = "${ctx}";
        }
    </script>
</head>
<body class="body_bg">
<!--[if lte IE 6]><br/>
<div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a>
    <h4>温馨提示：</h4>
    <p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的
        Chrome、Firefox、Safari 等。</p></div><![endif]-->
<div class="header">
    <div class="header-top">
        <div class="logo login-logo">
            <a href=""><img src="${ctxStatic}/images/logo_big.png" alt=""></a>
        </div>
        <div class="clear"></div>
    </div>
    <div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
        <button data-dismiss="alert" class="close">×</button>
        <label id="loginError" class="error">${message}</label>
    </div>
</div>

<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
    <label class="input-label" for="username">用户登录</label>
    <div class="input-div input_username">
        <input type="text" id="username" name="username" class="input-block-level required" placeholder="登录名" value="${username}">
    </div>
    <div class="input-div input_pwd">
        <input type="password" id="password" name="password"  placeholder="密码" class="input-block-level required">
    </div>
    <%--<label class="input-label" for="password">密码</label>--%>

    <input class="login_btn" type="submit" value="登 录"/>
    <input class="ssologin_btn" type="bbutton" value="SSO 登 录 >">
    <%--<input class="btn btn-large btn-primary pull-right" type="reset" value="清除"/>--%>
</form>

<%--<div class="footer">--%>
    <%--<%@include file="/WEB-INF/views/include/footer.jsp" %>--%>
<%--</div>--%>
</body>
</html>