package com.yum.ucp.modules.exception;

import com.yum.ucp.modules.Interface.entity.NotifyErrorCode;

public class NotifyException extends RuntimeException{
    private String errorCode;
    private String errorMsg;

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

    public NotifyException( String errorCode,String errorMsg){
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;

    }

    public NotifyException(NotifyErrorCode notifyErrorCode){
        this.errorCode=notifyErrorCode.getCode();
        this.errorMsg=notifyErrorCode.getDesc();

    }
}
