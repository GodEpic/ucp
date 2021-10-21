<%@ page contentType="text/html;charset=UTF-8" %>
<body>
<div class="tab-con input-style" <%--style="display: none"--%>>
<div class="" style="display: none">
    <div id="case" style="display: block">
        <div class="sec-case-form">
            <form id="assigneeForm" action="" method="post" >
                <div class="sec">
                    <div class="sec-form">
                        <li>
                            <label >分配人：</label>
                            <select name="assignee" class="assignee" id="assignee">
                                <c:forEach items="${assigneeList}" var="assignee">
                                    <option value="${assignee.value}">${assignee.name}</option>
                                </c:forEach>
                            </select>
                        </li>
                        <div class="clear"></div>
                        <div class="forError" style="color: red;;margin-left:35%;padding-top:3px;display: none" >分配人不能为空</div>
                    </div>

                </div>
                <%--<input type="text" name="isCSP" style="display:none">--%>
            </form>
        </div>
    </div>
</div>

</body>
<script>
    var html = $("#assigneeForm").html();
    $("#assigneeForm").remove();
</script>
