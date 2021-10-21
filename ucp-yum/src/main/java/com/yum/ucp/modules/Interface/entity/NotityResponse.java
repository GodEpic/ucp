package com.yum.ucp.modules.Interface.entity;

public class NotityResponse {

    private String uuid;

    private String errorCode;

    private String errorMsg;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static NotityResponse fail(String errorCode){

        return fail(errorCode,"系统异常");
    }
    public static NotityResponse fail(String errorCode,String errorMsg){
        NotityResponse notityResponse=new NotityResponse();
        notityResponse.setErrorCode(errorCode);
        notityResponse.setErrorMsg(errorMsg);
        return notityResponse;
    }

    public static NotityResponse success(){
        NotityResponse notityResponse=new NotityResponse();
        notityResponse.setErrorCode(NotifyErrorCode.ok.getCode());
        notityResponse.setErrorMsg(NotifyErrorCode.ok.getDesc());
        return notityResponse;
    }
}
