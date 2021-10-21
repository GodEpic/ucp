package com.yum.ucp.modules.common.entity;

/**
 * Created by Administrator on 2016/12/15.
 */
public class File {
    private String url;
    private String fileName;
    private String self;

    public    File(String url, String fileName,String self) {
        this.url = url;
        this.fileName = fileName;
        this.self = self;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
}
