/*
 * 文 件 名:  LocalUploadUtils.java
 * 版    权:  Movitech Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  本地文件操作工具类
 * 修 改 人:  Eddy.Xu
 * 修改时间:  2016年4月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.yum.ucp.modules.sys.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 本地文件操作工具类（上传、下载、删除、复制、写文件）
 *
 * @author yangdc
 * @date Apr 18, 2012
 * <p/>
 * <pre>
 * </pre>
 */
public class LocalUploadUtils {

    private static String basepath = ReadConfig.getSsoValue("file.basepath");
    private static Logger logger = LoggerFactory.getLogger(LocalUploadUtils.class);



    public static boolean saveFile(InputStream is, String path) {

        boolean flag = false;
        OutputStream os = null;
        try {
            makedir(path);
            File file = new File(basepath + path);
            os = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int readByte = 0;
            while ((readByte = is.read(buf)) != -1) {
                os.write(buf, 0, readByte);
            }
            flag = true;
        } catch (IOException e) {
            logger.error("local save file error:" + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static void downloadFile(String file, OutputStream os) {
        File f = new File(basepath + file);
        InputStream is = null;
        if (f.exists()) {
            try {
                byte[] buf = new byte[1024];
                int readbytes = 0;
                is = new FileInputStream(f);
                while ((readbytes = is.read(buf)) != -1) {
                    os.write(buf, 0, readbytes);
                }
            } catch (IOException e) {
                logger.error("local download file error:"+e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void copyFile(String srcFile, String destFile) {
        srcFile = UploadFileUtils.formatPath(srcFile);
        destFile = UploadFileUtils.formatPath(destFile);
        FileUtils.copyFileCover(basepath + srcFile, basepath + destFile, true);
    }

    public static boolean deleteFile(String file) {
        boolean flag = true;
        file = UploadFileUtils.formatPath(file);
        File f = new File(basepath + file);
        if (f.exists()) {
            flag = f.delete();
        }
        return flag;
    }

    public static boolean writeToFile(String content, String file) {
        boolean flag = false;
        try {
            FileUtils.write(new File(basepath + file), content, "utf-8", false);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static void makedir(String path) {
        path = path.substring(0, path.lastIndexOf("/"));
        File directry = new File(basepath + path);
        if (!directry.exists()) {
            directry.mkdirs();
        }

    }

    public static File createFile(String path) {
        String file = path.substring(0, path.lastIndexOf("/"));
        File fileWrite = new File(basepath + file);
        if (!fileWrite.exists()) {
            fileWrite.mkdirs();
        }
        fileWrite=new File(basepath + path);
        try {
            fileWrite.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileWrite;
    }
}
