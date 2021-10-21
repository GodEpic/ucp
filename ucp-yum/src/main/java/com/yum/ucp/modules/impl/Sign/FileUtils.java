package com.yum.ucp.modules.impl.Sign;

import com.yum.ucp.common.config.Global;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * Created by Edward.DengHua on 2018/2/8.
 */
public class FileUtils {


    /**
     * 以缓冲方式从jar,class目录下读取文件String
     *
     * @param classPathFile
     * @return
     */
    public static String readFileFromJar(String classPathFile) throws Exception {
        try {
            return readFile(getFileInputStreamFromJar(classPathFile));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 以缓冲方式从resource目录下读取文件String
     *
     * @param classPathFile - classes:xxx + / + filename.txt
     * @return
     */
    public static String readFileFromResource(String classPathFile) throws Exception {
        File file = null;
        try {
            file = ResourceUtils.getFile(classPathFile);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return readFile(file);
    }

    /**
     * 从jar包里读取classpath下的文件流
     * classPathFile = xxx/yyy/filename.txt, 即class下xxx目录下yyy目录里的filename.txt文件
     *
     * @param classPathFile
     * @return
     * @throws Exception
     */
    public static InputStream getFileInputStreamFromJar(String classPathFile) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(classPathFile);
        try {
            return classPathResource.getInputStream();
        } catch (IOException e) {
            throw new Exception("读取文件出错");
        }
    }

    /**
     * 以缓冲的方式从文件读取内容String
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String readFile(File file) throws Exception {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String readLine = null;
        while ((readLine = br.readLine()) != null) {
            sb.append(readLine);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 以缓冲的方式从文件流中读取内容String
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    private static String readFile(InputStream inputStream) throws Exception {
        StringBuilder sb = new StringBuilder();

        InputStreamReader ir = new InputStreamReader(inputStream);
        try {
            ir = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(ir);
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }

            br.close();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != ir) {
                ir.close();
            }
        }
        return sb.toString();
    }
}