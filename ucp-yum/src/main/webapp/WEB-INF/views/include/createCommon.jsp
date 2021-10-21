<%@ page contentType="text/html;charset=UTF-8" %>
<%--<div class="form-inline">--%>

    <%--<div class="input-group">--%>
        <%--<label>附件： </label>--%>
        <%--<c:forEach items="${attachments}" var="attachment">--%>
            <%--<div><a--%>
                <%--href="${attachment.contentUri }">${attachment.filename}</a><span class="attachment_delete" self="${attachment.self}">删除</span>--%>
        <%--</div></c:forEach>--%>
        <%--<input type="file" name="attachment" id="file"/>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<div class="form-inline">--%>
    <%--<div class="input-group" id="comment_list">--%>
        <%--<label>备注： </label>--%>
        <%--<c:forEach items="${comments}" var="comment">--%>
            <%--<div><textarea id="${comment.id }"--%>
                    <%--name="comment">${comment.body }</textarea>--<span class="comment_update" self="${comment.self}">更新</span><span--%>
                    <%--class="comment_delete" self="${comment.self}">删除</span>--%>
            <%--</div>--%>
        <%--</c:forEach>--%>
        <%--<span id="add_comment"> 添加评论</span>--%>
    <%--</div>--%>
<%--</div>--%>


<div >
    <article class="sec-title t3">附件</article>
    <div class="sec no-border">
        <div class="sec-tbl">
            <table id="fixed-table" class="table table-striped table-hover table-condensed">
                <thead>
                <tr>
                    <th>用户</th>
                    <th>名称</th>
                    <th>上传时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="file_find">
                <c:forEach items="${attachments}" var="attachment">
                    <tr>
                        <td>${attachment.author.name}</td>
                        <td>
                            <a href="#" target="_self" onclick="downloadAttr('${attachment.contentUri}','${attachment.filename}');">${attachment.filename}</a>
                            <c:if test="${fn:containsIgnoreCase(attachment.filename,'.jpg') or fn:containsIgnoreCase(attachment.filename,'.png') or fn:containsIgnoreCase(attachment.filename,'.bmp') or fn:containsIgnoreCase(attachment.filename,'.jpeg') or fn:containsIgnoreCase(attachment.filename,'.gif')}">
                                <a href="javascript:void(0);" target="_self" onclick="viewPic('${attachment.contentUri}','${attachment.filename}')" style="cursor: pointer">
                                    <img src="${ctxStatic}/images/openBig.jpg"  class="highestImage"  style="width: 20px;height: 20px;" >
                                </a>
                            </c:if>
                        </td>
                        <td>${attachment.creationDate}</td>
                        <td><c:if test="${attachment.isDelete}"><span class="attachment_delete" self="${attachment.self}">删除</span></c:if></td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
        <input type="file" name="attachment" id="file"/>
    </div>
</div>

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
<div class="lbOverlay">
    <div id="cp-header" >
        <span class="cp-image-icon size-24 cp-file-icon"></span>
        <span id="imgTitle"></span>
        <span id="imgIndex" style="display: none"></span>
        <span class="fv-close-button">
            <button id="cp-control-panel-close" class="cp-icon" onclick="closeDiv();return false;"></button>
        </span>
    </div>

    <div id="cp-body" class="aui-group">
        <div id="cp-file-body" class="meta-banner">
            <div class="cp-viewer-layer" onclick="closeDiv()">
                <div id="cp-image-preview">
                    <div class="cp-scale-info" style="display: none;">100%</div>
                    <div class="cp-image-container cp-annotatable">
                        <img src="" id="viewPic" class="viewPicImg">
                    </div>
                    <span class="cp-baseline-extension"></span>
                </div>
            </div>
            <div class="cp-error-layer" style="display: none;"></div>
            <div class="cp-password-layer" style="display: none;"></div>
            <%--<div class="cp-toolbar-layer">--%>
            <%--<div class="cp-toolbar" style="margin-left: -80px; opacity: 0;">--%>
            <%--<button tabindex="10" class="cp-toolbar-minus cp-icon" original-title="Zoom out">Zoom out</button>--%>
            <%--<button tabindex="11" class="cp-toolbar-plus cp-icon" original-title="Zoom in">Zoom in</button>--%>
            <%--<button tabindex="12" class="cp-toolbar-fit cp-icon" original-title="Fit to page">Fit to page</button>--%>
            <%--<button tabindex="13" class="cp-toolbar-presentation cp-icon" original-title="Start Presentation">Start Presentation</button>--%>
            <%--</div>--%>
            <%--</div>--%>
            <div class="cp-waiting-layer" style="display: none;"></div>
            <div class="cp-spinner-layer">
                <div class="cp-spinner"></div>
            </div>
            <div class="cp-arrow-layer" style="">
                <button class="cp-nav" id="cp-nav-left" tabindex="20" disabled-title="You're viewing the least recent file" style="display: inline-block;" onclick="pollImg('previous',$('#imgIndex').text(imgIndex));return false;">Go to the previous file</button>
                <button class="cp-nav" id="cp-nav-right" tabindex="20" disabled-title="You're viewing the most recent file" style="display: inline-block;" onclick="pollImg('next',$('#imgIndex').text(imgIndex));return false;">Go to the next file</button>
            </div>
        </div>
        <div id="cp-sidebar" class="panel-view meta-banner"></div>
    </div>

</div>
<link href="${ctxStatic}/css/viewPic.css" type="text/css" rel="stylesheet">
<script src="${ctxStatic}/ucp/download.js"></script>
<script>
    var ctx = '${ctx}';
    var userName = '${userName}';
    var key = '${entity.key}';

    $(function () {
        $("#file_find").on("click", ".attachment_delete", function () {
            var obj = $(this);
            $.ajax({
                url:ctx + "/common/deleteAttachment?time=" + new Date().getTime(),
                type: 'POST',
                data: {uri: obj.attr("self")},
                success: function (msg) {
                    obj.parent().parent().remove();
                    Common.myAlert("删除成功");
                }

            });
        });

        $("#file").change(function () {
            var fileList = document.getElementById("file").files;

            var file = fileList[0];
            if (typeof (file) == 'undefined') {
                Common.myAlert("请上传文件");
                return false;
            }
            var formData = new FormData();
            formData.append("file", file);
            var date = getNowFormatDate();

            if('' == key || null == key){
                //先入库
                var param = $("form").serializeArray();
                param.push({name: "issueTypeId", value: $(window.parent.document).find("#contentFrame").attr("issue-type")});
                showLoad(true);
                $.ajax({
                    type: "post",
                    data: param,
                    async:false,
                    url: ctx + "/common/grade?time=" + new Date().getTime(),
                    success: function (msg) {
                        closeLoad();
                        if (msg.success) {
                            $("input[name='key']").val(msg.obj.key);
                            key = msg.obj.key;
                        } else {
                            Common.myAlert(msg.message, 2);
                        }
                    }
                });
            }

            showLoad(false);
            $.ajax({
                url:ctx + "/common/uploadAttachment?time=" + new Date().getTime() + "&key=" + key,
                type: 'POST',
                data: formData,
                dataType: "json",
                cache: false,
                contentType: false,
                processData: false,
                async:false,
                success: function (msg) {
                    closeLoad();
                    if (typeof (msg.obj) != 'undefined') {
                        var html = "<td>" + userName + "</td><td>";

                        var fileNameAll = msg.obj.fileName;
                        var index = fileNameAll.lastIndexOf(".");
                        var fileSuffix = fileNameAll.substring(index+1,fileNameAll.length);

                        if('jpg' == fileSuffix.toLowerCase() || 'png' == fileSuffix.toLowerCase() || 'bmp' == fileSuffix.toLowerCase() || 'jpeg' == fileSuffix.toLowerCase() || 'gif' == fileSuffix.toLowerCase()){
                            html += "<a href=\"#\" target=\"_self\" onclick=\"downloadAttr('"+ msg.obj.url +"','" + fileNameAll + "');\">" + fileNameAll + "</a>";
                            html += "<a href=\"javascript:void(0);\" target=\"_self\" onclick=\"viewPic('"+ msg.obj.url +"','" + fileNameAll + "')\" style=\"cursor: pointer\"><img src=\"${ctxStatic}/images/openBig.jpg\"  class=\"highestImage\"  style=\"width: 20px;height: 20px;\" ></a>";
                        }else{
                            html += "<a href=\"" + msg.obj.url + "\">" + fileNameAll + "</a>";
                        }

                        html += "</td><td>" + date + "</td><td><span class=\"attachment_delete\" self=\"" + msg.obj.self + "\">删除</span></td>";

                        $("#file_find").append("<tr></tr>");
                        if ($("#file_find").find("tr").length == 0) {
                            $("#file_find tr").append(html);
                        } else {
                            $("#file_find tr:last").append(html);
                        }
                    }
                }

            });
        });
    });
</script>