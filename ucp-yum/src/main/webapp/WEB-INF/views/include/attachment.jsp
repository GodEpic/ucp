<%@ page contentType="text/html;charset=UTF-8" %>


<div class="l1_upload_attachment">
    <article class="sec-title t3">附件</article>
    <div class="sec no-border">
        <div class="sec-tbl ">
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
                        <td><a href="#" target="_slef" onclick="downloadAttr('${attachment_blank.contentUri}','${attachment.filename}');">${attachment.filename}</a></td>
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

<script src="${ctxStatic}/ucp/download.js"></script>