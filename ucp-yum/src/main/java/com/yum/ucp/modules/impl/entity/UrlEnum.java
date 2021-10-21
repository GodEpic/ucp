package com.yum.ucp.modules.impl.entity;
/**
 * 文件名称： UrlEnum</br>
 * 初始作者： doraemon.zhang</br>
 * 创建日期： 2019/2/25</br>
 * 功能说明： 枚举类 <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2018-2019 .All rights reserved.<br/>
 *
 */
public enum UrlEnum {

    URL_ADDMESSAGE("addMessage", "ucp/addMessage"),//短信页面
    URL_JIRASELECT("allCases", "ucp/allCases"),//Case查询
    URL_JIRADEAL("myCase","ucp/mycase"),//待处理
    URL_INFORMATION("searchInformation","ucp/searchInformation"),//搜索页面
    URL_ERROR("message", "ucp/error"),//error页面
    URL_JIRACASE_CREATE("createCase","createCase","issueTypeId");//创建CASE

    private String key;
    private String value;
    private String paramName;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private UrlEnum(String key, String value) {
        this.value = value;
        this.key = key;
    }
    private UrlEnum(String key, String value,String paramName) {
        this.value = value;
        this.key = key;
        this.paramName = paramName;
    }
}