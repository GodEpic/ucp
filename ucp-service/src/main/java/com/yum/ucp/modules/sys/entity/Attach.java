/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 附件Entity
 * @author Zachary
 * @version 2019-08-15
 */
public class Attach extends DataEntity<Attach> {
	
	private static final long serialVersionUID = 1L;
	private String className;		// 模块类名
	private String classPk;		// 模块数据ID
	private String originalFileName;		// 文件原始名称
	private String fileSize;		// 文件大小
	private String extension;		// 文件后缀
	private String fileName;		// 上传后文件名
	private String filePath;		// 文件路径
	private String type;		// 文件类型
	
	public Attach() {
		super();
	}

	public Attach(String id){
		super(id);
	}
	
	public Attach(String className, String classPk, String type) {
		this.className = className;
		this.classPk = classPk;
		this.type = type;
	}

	@Length(min=0, max=255, message="模块类名长度必须介于 0 和 255 之间")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@Length(min=0, max=75, message="模块数据ID长度必须介于 0 和 75 之间")
	public String getClassPk() {
		return classPk;
	}

	public void setClassPk(String classPk) {
		this.classPk = classPk;
	}
	
	@Length(min=0, max=255, message="文件原始名称长度必须介于 0 和 255 之间")
	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	@Length(min=0, max=255, message="文件后缀长度必须介于 0 和 255 之间")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	@Length(min=0, max=255, message="上传后文件名长度必须介于 0 和 255 之间")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Length(min=0, max=2000, message="文件路径长度必须介于 0 和 2000 之间")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Length(min=0, max=255, message="文件类型长度必须介于 0 和 255 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}