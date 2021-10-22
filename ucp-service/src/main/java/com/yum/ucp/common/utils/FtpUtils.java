package com.yum.ucp.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.config.Global;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class FtpUtils {

	private static Logger logger = Logger.getLogger(FtpUtils.class);

	/**
	 * 获取ftp连接
	 *
	 * @param ftpClient
	 * @return
	 * @throws Exception
	 */
	public static boolean connectFtp(FTPClient ftpClient) throws Exception {
		boolean flag = false;


		int reply;
		ftpClient.connect(Global.getConfig(Global.FTP_IP), Integer.parseInt(Global.getConfig(Global.FTP_PORT)));
		ftpClient.login(Global.getConfig(Global.FTP_USERNAME), Global.getConfig(Global.FTP_PASSWORD));
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			return flag;
		}
		ftpClient.enterLocalPassiveMode();
		String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");
		ftpClient.makeDirectory(createDirName);
		logger.info("FtpUtils.connectFtp--- before change working directory:" + ftpClient.printWorkingDirectory());
		ftpClient.changeWorkingDirectory(createDirName);
		logger.info("FtpUtils.connectFtp--- after change working directory:" + ftpClient.printWorkingDirectory());
		flag = true;
		return flag;
	}

	/**
	 * 关闭ftp连接
	 */
	public static void closeFtp(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ftp上传文件
	 *
	 * @param f
	 * @param  ftpClient
	 * @throws Exception
	 */
	public static void upload(File f, FTPClient ftpClient) throws Exception {
		if (f.isDirectory()) {
			ftpClient.makeDirectory(f.getName());
			ftpClient.changeWorkingDirectory(f.getName());
			String[] files = f.list();
			for (String fstr : files) {
				File file1 = new File(f.getPath() + "/" + fstr);
				if (file1.isDirectory()) {
					upload(file1, ftpClient);
					ftpClient.changeToParentDirectory();
				} else {
					File file2 = new File(f.getPath() + "/" + fstr);
					FileInputStream input = new FileInputStream(file2);
					ftpClient.storeFile(file2.getName(), input);
					input.close();
				}
			}
		} else {
			File file2 = new File(f.getPath());
			FileInputStream input = new FileInputStream(file2);
			ftpClient.storeFile(file2.getName(), input);
			input.close();
		}
	}
	
	/**
	 * 下载链接配置
	 *
	 * @param localBaseDir
	 *            本地目录
	 * @param remoteBaseDir
	 *            远程目录
	 * @throws Exception
	 */
	public static void startDown(String localBaseDir, String remoteBaseDir) throws Exception {
		FTPClient ftpClient = new FTPClient();
		if (FtpUtils.connectFtp(ftpClient)) {

			try {
				FTPFile[] files = null;
				boolean changedir = ftpClient.changeWorkingDirectory(remoteBaseDir);
				if (changedir) {
					ftpClient.setControlEncoding("GBK");
					files = ftpClient.listFiles();
					for (int i = 0; i < files.length; i++) {
						try {
							downloadFile(files[i], localBaseDir, remoteBaseDir);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("链接失败！");
		}

	}

	/**
	 * 下载FTP文件 当你需要下载FTP文件的时候，调用此方法 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
	 *
	 * @param ftpFile
	 * @param relativeLocalPath
	 * @param relativeRemotePath
	 */
	private static void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath) {
		FTPClient ftpClient = new FTPClient();
		if (ftpFile.isFile()) {
			if (ftpFile.getName().indexOf("?") == -1) {
				OutputStream outputStream = null;
				try {
					File locaFile = new File(relativeLocalPath + ftpFile.getName());
					// 判断文件是否存在，存在则返回
					if (locaFile.exists()) {
						return;
					} else {
						outputStream = new FileOutputStream(relativeLocalPath + ftpFile.getName());
						ftpClient.retrieveFile(ftpFile.getName(), outputStream);
						outputStream.flush();
						outputStream.close();
					}
				} catch (Exception e) {
					logger.info(e.getMessage());
				} finally {
					try {
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						logger.info("输出文件流异常");
					}
				}
			}
		} else {
			String newlocalRelatePath = relativeLocalPath + ftpFile.getName();
			String newRemote = new String(relativeRemotePath + ftpFile.getName().toString());
			File fl = new File(newlocalRelatePath);
			if (!fl.exists()) {
				fl.mkdirs();
			}
			try {
				newlocalRelatePath = newlocalRelatePath + '/';
				newRemote = newRemote + "/";
				String currentWorkDir = ftpFile.getName().toString();
				boolean changedir = ftpClient.changeWorkingDirectory(currentWorkDir);
				if (changedir) {
					FTPFile[] files = null;
					files = ftpClient.listFiles();
					for (int i = 0; i < files.length; i++) {
						downloadFile(files[i], newlocalRelatePath, newRemote);
					}
				}
				if (changedir) {
					ftpClient.changeToParentDirectory();
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}
	
	/**
	 * ftp上传文件
	 *
	 * @param file
	 * @throws Exception
	 */
	public static JSONObject uploadSignalFile(MultipartFile file) throws Exception {
		JSONObject object = new JSONObject();
		FTPClient ftpClient = new FTPClient();
		try {
			connectFtp(ftpClient);

			InputStream input = file.getInputStream();
			String originalFileName = file.getOriginalFilename();
			String fileExt = FilenameUtils.getExtension(originalFileName);
			object.put("originalFileName", originalFileName);
			object.put("fileSize", input.available());
			object.put("ext", fileExt);

			String fileName = SnowFlakeUtils.getNextId() + "." + fileExt;
			ftpClient.storeFile(fileName, input);
			input.close();

			String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");

			String ftpPathUrl = Global.getConfig(Global.FTP_PATH_URL);
			if(!ftpPathUrl.endsWith("/")){
				ftpPathUrl = ftpPathUrl + "/";
			}

			object.put("fileName", fileName);
			object.put("filePath", ftpPathUrl + createDirName + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONObject();
		} finally {
			closeFtp(ftpClient);
		}
		return object;
	}


	/**
	 * ftp上传文件
	 *
	 * @param file
	 * @throws Exception
	 */
	public static JSONObject uploadSignalFile(InputStream inputStream,String fName) throws Exception {
		JSONObject object = new JSONObject();
		FTPClient ftpClient = new FTPClient();
		try {
			connectFtp(ftpClient);

			InputStream input = inputStream;
			String fileExt = FilenameUtils.getExtension(fName);
			object.put("fileSize", input.available());
			object.put("ext", fileExt);

			String fileName = SnowFlakeUtils.getNextId() + "." + fileExt;
			ftpClient.storeFile(fileName, input);
			input.close();

			String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");

			String ftpPathUrl = Global.getConfig(Global.FTP_PATH_URL);
			if(!ftpPathUrl.endsWith("/")){
				ftpPathUrl = ftpPathUrl + "/";
			}

			object.put("fileName", fileName);
			object.put("filePath", ftpPathUrl + createDirName + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONObject();
		} finally {
			closeFtp(ftpClient);
		}
		return object;
	}


    /**
     * ftp上传文件
     *
     * @param file
     * @throws Exception
     */
    public static JSONObject uploadTemplateSignalFile(MultipartFile file, String dirName) throws Exception {
        JSONObject object = new JSONObject();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("utf-8");
            connectFtp(ftpClient);
            InputStream input = file.getInputStream();
            String originalFileName = file.getOriginalFilename();
            String fileExt = FilenameUtils.getExtension(originalFileName);
            object.put("originalFileName", originalFileName);
            object.put("fileSize", input.available());
            object.put("ext", fileExt);
            String makeDirectory = SnowFlakeUtils.getNextId() + dirName;
            ftpClient.makeDirectory(makeDirectory);
            ftpClient.changeWorkingDirectory(makeDirectory);
            String fileName = originalFileName;
            ftpClient.storeFile(fileName, input);
            input.close();
            String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");

            String ftpPathUrl = Global.getConfig(Global.FTP_PATH_URL);
            if (!ftpPathUrl.endsWith("/")) {
                ftpPathUrl = ftpPathUrl + "/";
            }

            object.put("fileName", fileName);
            object.put("filePath", ftpPathUrl + createDirName + "/" + makeDirectory + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            object = new JSONObject();
        } finally {
            closeFtp(ftpClient);
        }
        return object;
    }




	/**
	 * ftp上传文件
	 * @param inputStream
	 * @param fName
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static JSONObject uploadSignalFileByFileNameAndFilePath(InputStream inputStream,String fName,String filePath) throws Exception {
		JSONObject object = new JSONObject();
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setControlEncoding("utf-8");
			connectFtp(ftpClient);

			InputStream input = inputStream;
			String fileExt = FilenameUtils.getExtension(fName);
			object.put("fileSize", input.available());
			object.put("ext", fileExt);
			ftpClient.makeDirectory(filePath);
			ftpClient.changeWorkingDirectory(filePath);
			String fileName = fName;
			ftpClient.storeFile(fileName, input);
			input.close();

			String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");

			String ftpPathUrl = Global.getConfig(Global.FTP_PATH_URL);
			if(!ftpPathUrl.endsWith("/")){
				ftpPathUrl = ftpPathUrl + "/";
			}

			object.put("fileName", fileName);
			object.put("filePath", ftpPathUrl + createDirName + "/"+filePath+"/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONObject();
		} finally {
			closeFtp(ftpClient);
		}
		return object;
	}
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean delFtpFile(String filePath) {
		FTPClient ftpClient = new FTPClient();
		try {
			if(connectFtp(ftpClient)){
				filePath = filePath.replace(Global.getConfig(Global.FTP_PATH_URL), "");
				File file = new File(filePath);
				String baseDir = filePath.replace(file.getName(), "");
				// 切换FTP目录
				ftpClient.changeWorkingDirectory(baseDir);
				ftpClient.dele(file.getName());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeFtp(ftpClient);
		}
		return false;
	}

	/**
	 * 获取ftp连接
	 *
	 * @param ftpClient
	 * @return
	 * @throws Exception
	 */
	public static boolean connectFtpTest(FTPClient ftpClient, long dirName) throws Exception {
		boolean flag = false;
		int reply;
		ftpClient.connect("172.29.165.53", 21);
		ftpClient.login("ftpucp", "gY2eFG7Qqb3S");
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			return flag;
		}
		ftpClient.enterLocalPassiveMode();
		String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");
		ftpClient.makeDirectory(createDirName + "/" + String.valueOf(dirName));
		ftpClient.changeWorkingDirectory(createDirName + "/" + dirName);
		flag = true;
		return flag;
	}

	/**
	 * ftp上传文件
	 *
	 * @param file
	 * @throws Exception
	 */
	public static JSONObject uploadSignalFileTest(File file) throws Exception {
		JSONObject object = new JSONObject();
		FTPClient ftpClient = new FTPClient();
		try {
			long dirName = SnowFlakeUtils.getNextId();
			connectFtpTest(ftpClient, dirName);
			InputStream input = new FileInputStream(file);
			String originalFileName = file.getName();
			String fileExt = FilenameUtils.getExtension(originalFileName);
			object.put("originalFileName", originalFileName);
			object.put("fileSize", input.available());
			object.put("ext", fileExt);

			String uploadFileName=new String(originalFileName.getBytes("GBK"),"iso-8859-1");

			ftpClient.storeFile(uploadFileName, input);
			input.close();

			String createDirName = DateUtils.formatDate(new Date(), "yyyyMMdd");

			String ftpPathUrl = "http://172.29.165.53/data";
			if(!ftpPathUrl.endsWith("/")){
				ftpPathUrl = ftpPathUrl + "/";
			}
			object.put("fileName", originalFileName);
			object.put("dirName", dirName);
			object.put("filePath", ftpPathUrl + createDirName + "/" + dirName + "/" + originalFileName);
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONObject();
		} finally {
			closeFtp(ftpClient);
		}
		return object;
	}


}