<%@ page contentType="text/html;charset=UTF-8" %>
<%--<div class="form-inline">--%>

    <%--<div class="input-group">--%>
        <%--<label>附件： </label>--%>
        <%--<c:forEach items="${attachments}" var="attachment"><a--%>
                <%--href="${attachment.contentUri }">${attachment.filename}</a></c:forEach>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<div class="form-inline">--%>
    <%--<div class="input-group" id="comment_list">--%>
        <%--<label>备注： </label>--%>
        <%--<c:forEach items="${comments}" var="comment">--%>
            <%--<div><textarea id="${comment.id }"--%>
                    <%--name="comment">${comment.body }</textarea>--%>
            <%--</div>--%>
        <%--</c:forEach>--%>
    <%--</div>--%>
<%--</div>--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<div>
    <article class="sec-title t3">附件</article>
    <div class="sec no-border">
        <div class="sec-tbl">
            <table id="fixed-table">
                <thead>
                <tr>
                    <th>用户</th>
                    <th>名称</th>
                    <th>上传时间</th>
                </tr>
                </thead>
                <tbody id="content">
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
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div>
    <article class="sec-title t3">备注</article>
    <div class="sec no-border">
        <div class="sec-tbl">
            <table >
                <thead>
                <tr>
                    <th>用户</th>
                    <th>内容</th>
                    <th>评论时间</th>
                    <th></th>
                </tr>
                </thead>
                <tbody >
                <c:forEach items="${comments}" var="comment">
                    <tr>
                        <td>${comment.author.name}</td>
                        <td>${comment.body}</td>
                        <td>${comment.creationDate}</td>
                        <td></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div>
    <article class="sec-title t3">Case处理记录</article>
    <div class="sec no-border">
        <div class="sec-tbl">
            <table  >
                <thead>
                <tr>
                    <th>处理人</th>
                    <th>步骤</th>
                    <th>处理时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${changelogGroups}" var="changelog">
                    <tr>
                        <td>${changelog.author.name}</td>

                        <td>
                            <c:forEach items="${changelog.items}" var="item">
                                ${item.field}:  ${item.fromString}--->${item.toString}
                            </c:forEach>
                        </td>
                        <td>${changelog.created}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination"></div>
        </div>
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
        <span id="commonJiraId" style="display: none"></span>
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
    var jiraID = $("input[name='key']").val(); //显示页面获取不到
    var jiraIDNew = '${entity.key}';
    $("#commonJiraId").text(jiraIDNew);
</script>