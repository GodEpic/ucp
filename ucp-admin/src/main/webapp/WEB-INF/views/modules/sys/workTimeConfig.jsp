<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<style>
    .addMessage{
        padding:0px;
        height: 100%;
    }
    #checkForm{
        margin-left: 50px;
    }
    li{
        list-style:none;
    }
    label{
        display: block;
    }
    input{
        min-height: 30px;
    }
</style>
<body>
<form id="checkForm">
    <div class="tab-con input-style">
        <div  class="addMessage">
            <div class="sec-case-form">
                <form >
                    <div class="">
                        <div class="sec-form" style="margin-top: 20px;color: #8895b4">
                            <li>
                                <span>工作开始时间：</span>
                                <input class="Wdate" type="text" id="startDate" value="${workTimeConfig.startDate}" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                            </li>
                            <li>
                                <span>工作结束时间：</span>
                                <input class="Wdate" type="text" id="endDate" value="${workTimeConfig.endDate}" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'23:59:59'})"/>
                            </li>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <div class="form-submit">
                        <div class="btn-container">
                            <input id="workTimeButton" class="btn btn-primary" type="button" value="保 存" style="margin-left: 80px;margin-top: 15px">
                        </div>
                    </div>
                    <input type="hidden" name="key"/>
                </form>
            </div>
        </div>
    </div>
</form>

</body>
<script>
    var ctx = '${ctx}';
    var num=1;
</script>
<script>
    $(function () {
        $("#workTimeButton").click(function () {
            var startDate = $("#startDate").val();
            var endDate =$("#endDate").val();
            $.ajax({
                type: "get",
                url:ctx + "/sys/menu/workTimeConfig/"+startDate+"/"+endDate,
                success: function (msg) {
                    if (msg.success) {
                        alert(msg.message);
                    } else {
                        alert(msg.message);
                    }
                }
            });
        })
    })
</script>
