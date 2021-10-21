package com.yum.ucp.modules.Interface.web;

import com.yum.ucp.common.utils.DownloadFileUtils;
import com.yum.ucp.modules.Interface.entity.Event;
import com.yum.ucp.modules.Interface.entity.NotityRequest;
import com.yum.ucp.modules.Interface.entity.NotityResponse;
import com.yum.ucp.modules.Interface.entity.NotifyErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping(value = "/file")
public class FileController {
    Logger log= LoggerFactory.getLogger(this.getClass());

    public static String getJarRootPath(){
        String path="/home/nfs/wzhu/";
        return path;
    }
    @RequestMapping(value = "/download/{path1}/{path2}",method = RequestMethod.GET)
    public String notify(@PathVariable("path1") String path1,@PathVariable("path2") String path2, HttpServletRequest request, HttpServletResponse response){
        String path=getJarRootPath()+path1+File.separator+path2+".xlsx";
        String fileName=path2+".xlsx";
        log.info("path1={},fileName={}",path,fileName);

        DownloadFileUtils.downloadFile(path,fileName,response,request);
        return null;
    }

}
