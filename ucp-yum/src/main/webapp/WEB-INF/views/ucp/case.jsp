<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<body>
<div class="tab-con case-tab-top">
    <div id="case">
        <div class="sec case-top">
            <li>
                <%--<article class="sec-title t2">Case登记:</article>--%>
                <div class="clear"></div>
                <c:forEach items="${issueTypes}" var="item">
                    <div class="tab-con">
                        <label>${item.name}:</label>
                        <c:forEach items="${item.menus}" var="menu">
                            <span name="common_case_create" issueTypeId="${menu.id}" rel="${ctx}${menu.href}">${menu.name}</span>
                        </c:forEach>
                    </div>
                    <br/>
                </c:forEach>
            </li>
        </div>
    </div>
</div>

<section class="right-section sec">
    <iframe style="display:none" frameborder="0" width="100%" scrolling="no" name="contentFrame" id="contentFrame"></iframe>
</section>
</body>
<script>
    var index = 0;
    $("#current_case_html").remove();
    $("span[name='common_case_create']").click(function () {
        debugger;
        var result = true;
        if (index != 0) {
            result = confirm("是否确认切换页面?");
        }
        if (result) {
            $("#current_case_html").remove();
            var lis = $('.case-top li span');
            lis.removeClass();
            $(window.document).find("#contentFrame").attr("src", $(this).attr("rel"));
            $(window.document).find("#contentFrame").attr("issue-type", $(this).attr("issueTypeId"));
            $(window.document).find("#contentFrame").attr("conName", "");
            $(window.document).find("#contentFrame").show();
            $(this).attr("class", "current");
            $(".case-top").after("  <div  id=\"current_case_html\"> <li> <label>您当前登记的case是:" + $(this).html() + "</label> </li> </div>");
            index++;
            return false;
        }
    });
    $(function () {
        $(window.parent.document).find("#case_create").height($("#case").height() + 70);
    });

</script>
