/**
 * Created by Administrator on 2016/10/9.
 */
$(function () {
    function initZtree() {
        $.ajax({
            type: "get",
            // data: {"roleType": $("input[name='role']:checked").val()},
            data: {"roleType": $("#roleType").val()},
            url: ctx + "/menuZtree?time=" +new Date().getTime(),
            success: function (msg) {
                var html = "";
                $.each(msg.obj, function (index, item) {
                    var rid = "roleName_"+item.workCode;
                    if (index == 0) {
                        html += "<label><b><input type=\"radio\" checked value=\"" + item.workCode + "\"   name=\"workCode\"></b><span>" + item.workCode + "</span><span id="+rid+">" + item.roleName + "</span></label><div class='clear'></div>";
                    } else {
                        html += "<label><b><input type=\"radio\"   value=\"" + item.workCode + "\"   name=\"workCode\"></b><span>" + item.workCode + "</span><span>" + item.roleName + "</span></label><div class='clear'></div>";
                    }
                });
                $(".work_code").append(html);
            }
        });
    }

    initZtree();
    $("#roleType").change(function () {
        $(".work_code").html("");
        initZtree();
    });
    $("#step").click(function () {
        var workCode = $("input[name='workCode']:checked").val();
        var roleType =$("#roleType").val();
        var roleName =$("#roleName_"+workCode).text();
        if (null == workCode || $.trim(workCode) == '') {
            Common.myAlert("请选择工号");
            return false;
        }
        showLoad(false);
        $.ajax({
            type: "get",
            data: {workCode:workCode,roleType:roleType,roleName:roleName},
            url: ctx + "/menus?time=" +new Date().getTime(),
            success: function (msg) {
                closeLoad();
                if(msg.success){
                        window.location.href = ctx + "/index";
                }else{
                    Common.myAlert(msg.message);
                }
            }
        });
    });
});
