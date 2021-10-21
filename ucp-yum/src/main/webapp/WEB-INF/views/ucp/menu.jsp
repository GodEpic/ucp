<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html lang="en">
<head lang="en">
    <title>YUM</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
<script>
    // 如果在框架或在对话框中，则弹出提示并跳转到首页
    if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
        Common.myAlert('token已过期,谢谢！');
        top.location = "${ctx}/menu";
    }
</script>
<style>
    html,body{
        background: #f0f0f0;
    }
    footer {
        position: absolute;
        bottom: 10px
    }

    .tab-con {
        background: transparent !important
    }
    .logo {
        margin-bottom: 7px;
    }
</style>
<header>
    <div class="header-top">
        <div class="logo">
            <a href=""><img src="${ctxStatic}/images/logo.png" alt=""></a>
        </div>
            <div class="clear"></div>
        </div>
        <div class="clear"></div>
    </div>
</header>
<div class="container level2" style="width: 1150px;background: #ffffff;margin: 0px auto;margin-top: 20px !important;">
    <aside class="left-aside"></aside>
    <section class="right-section" style="width:100%">
        <div style="overflow: hidden; width: 100%;">
            <div class="right-conatiner" id="right-conatiner" style="width: 100%;background: transparent">
                <div class="tab-con"></div>
                <div class="tab-con"></div>
                <div class="tab-con">
                    <div class="input-group">
                        <label style="margin-left:300px;">职责选择：<b>*</b></label>
                        <%--<c:if test="${count>2}">
                            <div class="radio-inline">
                                <label><input type="radio" checked value="2" class="role" name="role">LV2</label>
                                <label><input type="radio" value="1" class="role" name="role">LV1</label>
                            </div>
                        </c:if>
                        <c:if test="${count==1}">
                            <div class="radio-inline">
                            <label><input type="radio" checked value="1" class="role" name="role">LV1</label>
                        </div>
                        </c:if>--%>
                        <div class="radio-inline">
                            <select class="input-medium" id="roleType">
                                <option value="">请选择...</option>
                                <c:forEach items="${roleTypeList}" var="roleType">
                                    <option value="${roleType.value}">${roleType.label}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="tab-con tab-tbl">
                    <div class="work_header">
                        <li>选择</li>
                        <li>工号</li>
                        <li>角色名</li>
                        <div class="clear"></div>
                    </div>
                    <div class="tab-con work_code">
                    </div>
                    <div class="tab-con">
                    </div>
                    <%--<div class="tab-con">--%>
                    <%--</div>--%>
                    <div class="sec case-top" style="border-bottom: 0px;">
                        <li><span style="width:100px;cursor: pointer;" class="current" id="step">下一步</span></li>
                    </div>
                </div>

                <div class="tab-con">
                </div>
                <div class="tab-con">
                </div>
            </div>
        </div>
        <label id="dragg-btn"></label>

        <div class="clear"></div>
    </section>
    <div class="clear"></div>
</div>
<%@include file="/WEB-INF/views/include/footer.jsp" %>

</body>
</html>
<script>
    var ctx = '${ctx}';
    $("input[name='role']:checked").val()
</script>
<script src="${ctxStatic}/ucp/menu.js"></script>