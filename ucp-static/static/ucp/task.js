/**
 * Created by admin on 2019/7/2.
 */
// 根据加载的页面调整iframe的高度
$(window.parent.document).find("#contentFrame").height($(".tab-con").height() + 150);

function generateTask(id) {
    var parentLayer = parent.layer;
    parentLayer.confirm('是否确认发送该任务？', function(index) {
        var mask = parentLayer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
        $.ajax({
            type: "post",
            data: {id: id},
            url: ctx + "/task/ucpTask/createTask?time=" + new Date().getTime(),
            success: function (msg) {
                parentLayer.close(mask);
                if(msg.success) {
                    $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                } else {
                    parentLayer.msg(msg.message);
                }
            },
            error : function(){
                parentLayer.close(mask);
                parentLayer.msg("发送失败");
            }
        });
        parentLayer.close(index);
    });
}

// 任务认领
function receive(id, self, roleType) {
    var parentLayer = parent.layer;
    parentLayer.confirm('是否确认认领该任务？', function(index) {
        var mask = parentLayer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
        $.ajax({
            type: "post",
            data: {id: id},
            url: ctx + "/task/ucpTask/receive" + roleType + "?time=" + new Date().getTime(),
            success: function (msg) {
                parentLayer.close(mask);
                parentLayer.msg(msg.message);
                $(self).html("继续");
                if(roleType == 'L1') {
                    $(self).attr("href", ctx+"/task/config/detail?id="+id);
                }
                if(roleType == 'L2') {
                    $(self).attr("href", ctx+"/task/check/detail?id="+id);
                }
                $(self).attr("target", "_blank");
                $(self).removeAttr("onclick");
                $(self).parent().prev().html("未完成");
            },
            error : function(){
                parentLayer.close(mask);
                parentLayer.msg("认领失败");
            }
        });
        parentLayer.close(index);
    });
}

// QA任务认领
function receiveQA(id, self) {
    var parentLayer = parent.layer;
    parentLayer.confirm('是否确认认领该活动？', function(index) {
        var mask = parentLayer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
        $.ajax({
            type: "post",
            data: {id: id},
            url: ctx + "/activity/receive" + roleType + "?time=" + new Date().getTime(),
            success: function (msg) {
                parentLayer.close(mask);
                parentLayer.msg(msg.message);
                $(self).html("继续");
                $(self).attr("target", "_blank");
                $(self).removeAttr("onclick");
                $(self).parent().prev().html("测试中");
            },
            error : function(){
                parentLayer.close(mask);
                parentLayer.msg("认领失败");
            }
        });
        parentLayer.close(index);
    });
}

// 提交至QA
function sendToQA(actId) {
    var parentLayer = parent.layer;
    parentLayer.confirm('是否确认发送至QA？', function(index) {
        var mask = parentLayer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
        $.ajax({
            type: "post",
            data: {id: id},
            url: ctx + "/task/ucpTask/sendToQA" + actId + "?time=" + new Date().getTime(),
            success: function (msg) {
                parentLayer.close(mask);
                parentLayer.msg(msg.message);
                $(self).html("继续");
                if(roleType == 'L1') {
                    $(self).attr("href", ctx+"/task/config/detail?id="+id);
                }
                if(roleType == 'L2') {
                    $(self).attr("href", ctx+"/task/check/detail?id="+id);
                }
                $(self).attr("target", "_blank");
                $(self).removeAttr("onclick");
                $(self).parent().prev().html("未完成");
            },
            error : function(){
                parentLayer.close(mask);
                parentLayer.msg("发送失败");
            }
        });
        parentLayer.close(index);
    });
}

