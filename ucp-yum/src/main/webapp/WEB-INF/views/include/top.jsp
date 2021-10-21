<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<style>
    .tab-find {
        margin: 80px 0;
    }

    .yum-header {
        background: url("${ctxStatic}/images/top_bg.png") no-repeat;
        background-size: 100% 100%;
        height: 60px;
    }

    .header-top .logo {
        margin-top: 0;
        line-height: 60px;
        font-size: 22px;
        color: #FFF;
        font-weight: bolder;
        letter-spacing: 4px;
    }

    .top-right .login {
        font-size: 22px;
        font-weight: bolder;
        margin-top: 0;
        line-height: 60px;
    }

    .top-right .login a {
        font-size: 22px;
    }
</style>
<header class="yum-header">
    <div class="header-top">
        <div class="logo">
            统一配置平台
        </div>
        <div class="top-right">
            <!-- top菜单 -->
            <nav class="top-index">
                <c:choose>
                    <c:when test="${roleType==1}">
                        <%--<li class="current"><a class="deal_case" ref="${ctx}/task/ucpTask/dealListL1">待处理任务</a></li>
                        <li><a ref="${ctx}/task/ucpTask/allList" class="all_case">任务查询</a></li>
                        <div class="clear"></div>--%>
                    </c:when>
                    <c:when test="${roleType==2}">
                        <%--<li class="current"><a class="deal_case" ref="${ctx}/task/ucpTask/dealListL2">待处理任务</a></li>
                        <li><a ref="${ctx}/task/ucpTask/allList" class="all_case">任务查询</a></li>
                        <div class="clear"></div>--%>
                        <li class="current"><a class="deal_case" ref="${ctx}/task/ucpTask/dealListL2">任务列表</a></li>
                        <li><a class="deal_case" ref="${ctx}/task/ucpTask/dealKeyValueListL2">键值任务列表</a></li>
                        <div class="clear"></div>
                    </c:when>
                    <c:when test="${roleType==5}">
                        <%--<li class="current"><a class="deal_case" ref="${ctx}/task/ucpTask/dealListL2">待测试活动</a></li>
                        <div class="clear"></div>--%>
                        <li class="current"><a class="deal_case" ref="${ctx}/activity/dealListQA">测试列表</a></li>
                        <li><a class="deal_case" ref="${ctx}/activity/dealKeyValueListQA">键值任务测试列表</a></li>
                        <div class="clear"></div>
                    </c:when>
                    <c:when test="${roleType==7}">
                        <li class="current"><a class="deal_case" ref="${ctx}/task/ucpTask/dealListL3">任务列表</a></li>
                        <div class="clear"></div>
                    </c:when>
                    <c:otherwise>
                        <li class="current"><a class="deal_case" ref="${ctx}/activity/dealList">活动列表</a></li>
                        <li><a ref="${ctx}/activity/allList" class="all_case">活动台账</a></li>
                        <li><a class="deal_case" ref="${ctx}/activity/dealKeyValueList">键值列表</a></li>
                        <li><a class="deal_case" ref="${ctx}/activity/activityList">接收活动列表</a></li>
                        <li><a class="deal_case" ref="${ctx}/template/list">模板列表</a></li>
                        <div class="clear"></div>
                    </c:otherwise>

                </c:choose>
            </nav>
            <div class="login">
                欢迎您，${userName}
                <%--<img class="personal_icon" src="${ctxStatic}/images/person_icon.png" alt="">&nbsp;&nbsp;--%>
                <a onclick="logout()"><img src="${ctxStatic}/images/close.png" alt="退出"></a>
            </div>
            <span class="textAreaVal" style="display: none">${messageAgent}</span>
            <div class="clear"></div>
        </div>
        <div class="clear"></div>
    </div>
</header>

<div class="hot-line" style="width:50%;float: left;padding-top: 1px;position: absolute">
    <span class="phone hot_phone" style="font-size: 20px;">
    <span id="top_phone" style="font-size: 20px;"></span></span></br></br>
    <span class="phone" id="mobile_phone" style="float: left;font-size: 20px;"></span>
    <div class="clear"></div>
</div>
<div class="announce"
     style="position: absolute;/*float: right;*/margin-left: 35%;width: 50%;text-align: center;padding-top: 10px;">
    <span style="font-size: 20px;">${announce}</span>
</div>
<div class="clear"></div>

<script>
    // 加载第一个tab页的内容
    var html = "";
    if ('${roleType}' == 1) {//L1
        html = '<iframe src="${ctx}/task/ucpTask/dealListL1" frameborder="0" width="100%" scrolling="no" id="deal_case"></iframe>';
    } else if ('${roleType}' == 2) {//L2
        html = '<iframe src="${ctx}/task/ucpTask/dealListL2" frameborder="0" width="100%" scrolling="no" id="deal_case"></iframe>';
    } else if ('${roleType}' == 5) {//QA
        html = '<iframe src="${ctx}/activity/dealListQA" frameborder="0" width="100%" scrolling="no" id="deal_case"></iframe>';
    } else if ('${roleType}' == 7) {//键位配置专家
        html = '<iframe src="${ctx}/task/ucpTask/dealListL3" frameborder="0" width="100%" scrolling="no" id="deal_case"></iframe>';
    } else {
        html = '<iframe src="${ctx}/activity/dealList" frameborder="0" width="100%" scrolling="no" id="deal_case"></iframe>';
    }
    $(window.parent.document).find(".right-conatiner").append(html);

    $(".top-index a").click(function () {
        var parentDom = $(window.parent.document),
            thisDom = $(this);

        parentDom.find("iframe").hide();
        parentDom.find("#top-nav").show();
        parentDom.find("#left-aside").show();
        parentDom.find("#bottomFrame").show();

        var lis = $('.top-right li');
        lis.removeClass();

        thisDom.parent("li").attr("class", "current");

        var html = '<iframe src="' + thisDom.attr("ref") + '" frameborder="0" width="100%" scrolling="yes" id="' + thisDom.attr("class") + '"></iframe>';

        var currIframe = parentDom.find("#" + thisDom.attr("class"));
        if (currIframe.length > 0) {
            parentDom.find("#" + thisDom.attr("class")).remove();
        }
        parentDom.find(".right-conatiner").append(html);
        return false;
    });

    function logout() {
        window.parent.location.href = "${ctx}/logout";
    }

    $(".tab-find").on("click", ".close", function (e) {
        var iframeId = $(this).parent().attr("id");
        $(this).parent().remove();
        $(window.parent.document).find("#" + iframeId).remove();
        var lis = $('.top-right li');
        lis.removeClass();

        if (iframeId == 'case_create') {
            $(window.parent.frames["contentFrame"].document).html("");
            $(window.parent.frames["contentFrame"]).removeAttr("conName");
            $(window.parent.frames["contentFrame"]).hide();
        }

        $(".deal_case").parent("li").attr("class", "current");
        $(window.parent.document).find("#deal_case").show();
        $(".tab-find").find("a").eq(0).attr("class", "current");

    });
</script>