/*
 * 文 件 名:  UploadFileUtils.java
 * 版    权:  Movitech Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  文件操作工具类
 * 修 改 人:  Eddy.Xu
 * 修改时间:  2016年4月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.yum.ucp.modules.sys.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * 文件操作工具类
 * <功能详细描述>
 * 
 * @author  Eddy.Xu
 * @version  [1.0, 2016年4月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UploadFileUtils
{
    private static int model = Integer.parseInt(ReadConfig.getSsoValue("file.model"));

    /**  
     * 文件保存
     * 根据配置文件将文件保存到本地服务器或者远程服务器
     * @param fileInputStream
     * @param destFile
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean saveFile(InputStream fileInputStream, String destFile)
    {
        destFile = UploadFileUtils.formatPath(destFile);
        boolean flag = false;
        if (Model.LOCAL == model)
        {
            flag = LocalUploadUtils.saveFile(fileInputStream,destFile);
        }
        return flag;
    }
    /**
     * 将内容保存成文件
     * 将内容保存成文件，根据配置文件将文件保存到本地服务器或者远程服务器
     * @param content
     * @param file
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean writeToFile(String content,String file){
        boolean flag = false;
        file = UploadFileUtils.formatPath(file);
        if (Model.LOCAL == model)
        {
            flag = LocalUploadUtils.writeToFile(content, file);
        }

        return flag;
    }
    
    /**
     * 文件获取
     * 根据配置文件
     * @param file
     * @param os
     * @see [类、类#方法、类#成员]
     */
    public static void downloadfile(String file,OutputStream os){
        file = UploadFileUtils.formatPath(file);
        if (Model.LOCAL == model)
        {
            LocalUploadUtils.downloadFile(file, os);
        }

    }
    private final class Model
    {       
        private static final int LOCAL = 0; //本地
        private static final int SFTP = 1;//sftp协议远程
        private static final int WEB = 2;//web接口
    } 
    /**
     * 文件路径修正
     * 修正文件路径
     * @param path
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String formatPath(String path)
    {
        path = path.replaceAll("\\\\", "/");
        if (!StringUtils.startsWith(path, "/"))
        {
            path = "/" + path;
        }
        return path;
    }
}
