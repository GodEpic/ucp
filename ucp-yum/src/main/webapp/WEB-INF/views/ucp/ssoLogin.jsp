<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>yum 登录</title>
    <meta name="decorator" content="blank"/>
<body>
<%--<div class="footer">--%>
<%--<%@include file="/WEB-INF/views/include/footer.jsp" %>--%>
<%--</div>--%>
</body>
<script>
    window.location.href = "<%= (String) request.getAttribute("ssoAuthorizeUrl") %>";
</script>

</html>