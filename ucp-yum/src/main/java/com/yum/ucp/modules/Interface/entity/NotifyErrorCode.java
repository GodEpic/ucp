package com.yum.ucp.modules.Interface.entity;

public enum NotifyErrorCode {
    ok("0000","成功"),
    system_error("1000","系统异常"),
    param_error("1001","参数错误"),
    activity_repetition("2000","活动编号已经申请"),
    upload_error("2001","excel上传失败"),
    not_retraction("3000","无可撤回的券"),
    activity_accept("3001","券已上架"),
     not_reject("4000","无可退回的券"),
    status_error("4002","券状态错误，不可退回"),
    sync_status_error("5001","同步状态错误")


    ;
    String code;
    String desc;


    NotifyErrorCode(String code,String desc){
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


    //    String ok="0000";
//
//    String system_error="1000";
//
//    /**
//     * 参数错误
//     */
//    String param_error="1001";
//    /**
//     * 活动编号已经申请
//     */
//    String activity_repetition="2000";



}
