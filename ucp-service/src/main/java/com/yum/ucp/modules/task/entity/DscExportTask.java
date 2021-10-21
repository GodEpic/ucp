package com.yum.ucp.modules.task.entity;


import com.yum.ucp.common.persistence.DataEntity;

import java.text.SimpleDateFormat;

public class DscExportTask extends DataEntity<DscExportTask> implements java.io.Serializable {
    //项目key
    private String  projectKey;
    //issueType id
    private String  issueTypeId;
    //成功数
    private int successNum;
    //失败数目
    private int failNum;
    //总数
    private int totalNum;
    //生成文件名
    private String fileName;
    private String fileNameError;
    //生成文件路径
    private String filePath;
    //生成文件路径错误
    private String filePathError;
    //任务状态，0=初始/1=执行/9=成功/-1=异常
    private int status;
    //备注
    private String remark;

    private String createName;

    public String getCreateName() {
        return createName;
    }

    public String getFileNameError() {
        return fileNameError;
    }

    public void setFileNameError(String fileNameError) {
        this.fileNameError = fileNameError;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getFilePathError() {
        return filePathError;
    }

    public void setFilePathError(String filePathError) {
        this.filePathError = filePathError;
    }

    public String getCreationDate() {
        if(null!=createDate){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(createDate);
        }
        return null;
    }
}
