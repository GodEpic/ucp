package com.yum.ucp.modules.Interface.entity;

public enum Event {
    add("1","新增"),
    revocation("2","撤回"),
    reject("3","退回"),
    sync("4","同步状态");

    private String code;
    private String desc;

    Event(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
