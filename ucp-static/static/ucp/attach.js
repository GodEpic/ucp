function downloadAttach(_t){
    var originalFileName = $(_t).attr("data-orgName"),
        fileName = $(_t).attr("data-fileName");
    window.location.href= encodeURI("/ucp/common/downloadFtpAttach?originalFileName=" + originalFileName + "&fileName=" + fileName);
}