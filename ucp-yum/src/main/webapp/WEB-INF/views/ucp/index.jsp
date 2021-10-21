<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>

<html lang="en">
<head lang="en">
    <title>YUM</title>
</head>
<body>
<section class="right-section">
    <div class="top">
        <iframe src="${ctx}/top" frameborder="0" width="100%" scrolling="no" id="top-nav"></iframe>
    </div>
    <iframe style="display:none" frameborder="0" width="100%" scrolling="no" name="contentFrame" id="contentFrame"></iframe>
    <div class="right-conatiner">
    </div>
    <iframe src="${ctx}/footer" frameborder="0" width="100%" height="35" scrolling="no" id="bottomFrame"></iframe>
</section>
<div class="clear"></div>
<div id="floatingCirclesG">
    <div class="f_circleG" id="frotateG_01"></div>
    <div class="f_circleG" id="frotateG_02"></div>
    <div class="f_circleG" id="frotateG_03"></div>
    <div class="f_circleG" id="frotateG_04"></div>
    <div class="f_circleG" id="frotateG_05"></div>
    <div class="f_circleG" id="frotateG_06"></div>
    <div class="f_circleG" id="frotateG_07"></div>
    <div class="f_circleG" id="frotateG_08"></div>
</div>
<div id="png" style="background: #000; left:0;top:0; display:none;opacity: 0.6;z-index: 2;width: 100%;height: 100%;position: absolute; "></div>

</body>

</html>
<script>
    $("#left-aside").height($(window).height());
</script>