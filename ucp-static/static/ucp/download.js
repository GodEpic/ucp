/* global ctx */

function downloadAttr(fielUri, filename) {
    showLoad(false);
    jQuery('<form action=' + ctx + '/common/downloadAttachment?time=' + new Date().getTime() + '" method="post">' + // action请求路径及推送方法
            '<input type="text" name="uri" value="' + fielUri + '"/>' + // 文件路径
            '<input type="text" name="fileName" value="' + filename + '"/>' + // 文件名称
            '</form>')
            .appendTo('body').submit().remove();
    closeLoad();
}

function viewPic(fielUri, filename, i) {
    // $(".lbOverlay").css({"height":window.screen.availHeight});
    $(".lbOverlay").show();

    var st=$(document).scrollTop(); //页面滑动高度
    var objH=$(".hidden_pro_au").height();//浮动对象的高度
    var ch=$(window).height();//屏幕的高度
    var objT=Number(st)+(Number(ch)-Number(objH))/2;   //思路  浮动高度+（（屏幕高度-对象高度））/2
    $(".hidden_pro_au").css("top",objT);

    var sl=$(document).scrollLeft(); //页面滑动左移宽度
    var objW=$(".hidden_pro_au").width();//浮动对象的宽度
    var cw=$(window).width();//屏幕的宽度
    var objL=Number(sl)+(Number(cw)-Number(objW))/2; //思路  左移浮动宽度+（（屏幕宽度-对象宽度））/2
    $(".hidden_pro_au").css("left",objL);
    $(".hidden_pro_au").slideDown("20000");//这里显示方式多种效果
    $("#imgTitle").text(filename);

    var strings = fielUri.split("/");
    var imgIndex = strings[strings.length-2];
    $("#imgIndex").text(imgIndex);

    showLoad(false);
    $.ajax({
        type: "post",
        data: {uri:fielUri, fileName:filename},
        url: ctx + "/common/viewPic?time=" + new Date().getTime(),
        success: function (msg) {
            closeLoad();
            document.getElementById("viewPic").src="data:image/jpeg;base64,"+msg;
        }
    });
}

function closeDiv(){
    $(".lbOverlay").hide();
    $(".hidden_pro_au").hide();
    document.getElementById("viewPic").src="";
}

var jiraID =  $("input[name='key']").val();

function pollImg(flag, imgIndex) {

    //获取附件-这一行只有JSP有效
    //var data = '${fns:toJson(attachments)}';
    var data = '';
    jiraID =  $("input[name='key']").val();

    if(null == jiraID || 'null' == jiraID || 'undefined' == jiraID){
        //解决显示页面获取不到jiraid
        jiraID =  $("#commonJiraId").text();
    }

    $.ajax({
        type: "post",
        data: {jiraID:jiraID},
        async:false,
        url: ctx + "/common/getIssueAttachments?time=" + new Date().getTime(),
        success: function (msg) {
            if (msg.success) {
                data = msg.obj;
            } else {
                Common.myAlert("获取附件失败！");
            }
        }
    });

    var count;
    for (var i=0; i<data.length; i++){
        var fileUrl = data[i].contentUri;
        var strings = fileUrl.split("/");
        var listImgIndex = strings[strings.length-2];
        if(listImgIndex == imgIndex){
            count = i;
            break;
        }
    }

    var nextImg;
    if('next' == flag){
        if(0==count){
            count = data.length;
        }
        for ( var k = count - 1 ; k >= 0 ; k-- ){
            var fileNameAll = data[k].filename;
            var index = fileNameAll.lastIndexOf(".");
            var fileSuffix = fileNameAll.substring(index+1,fileNameAll.length);
            if('jpg' == fileSuffix.toLowerCase() || 'png' == fileSuffix.toLowerCase() || 'bmp' == fileSuffix.toLowerCase() || 'jpeg' == fileSuffix.toLowerCase() || 'gif' == fileSuffix.toLowerCase()){
                nextImg = data[k];
                break;
            }
        }
    }else{
        //向前查看逻辑
        if(count == data.length - 1){
            count = -1;
        }
        for ( var k = count + 1 ; k < data.length; k++ ){
            var fileNameAll = data[k].filename;
            var index = fileNameAll.lastIndexOf(".");
            var fileSuffix = fileNameAll.substring(index+1,fileNameAll.length);
            if('jpg' == fileSuffix.toLowerCase() || 'png' == fileSuffix.toLowerCase() || 'bmp' == fileSuffix.toLowerCase() || 'jpeg' == fileSuffix.toLowerCase() || 'gif' == fileSuffix.toLowerCase()){
                nextImg = data[k];
                break;
            }
        }
    }

    //重新展示
    closeDiv();
    viewPic(nextImg.contentUri,nextImg.filename);
}