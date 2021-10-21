package com.yum.ucp.modules.sys.utils;

import java.io.Serializable;

public class ResponseMessage implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3035285950649126234L;
    
    private ResponseMessage()
    {
        
    }
    
    public static final String ERR_NOT_LOGGED = "1";
    
    public static final String ERR_UNAUTHORIZED = "2";
    
    public static final String ERR_NOT_FOUND = "3";
    
    public static final String ERR_DUPLICATE = "4";

    public static final String ERR_NOT_REGIST = "5";


    public static final String ERR_UNKNOWN = "99";
    
    public static final String WARNING = "100";
    
    private boolean success;
    
    private String code;
    
    private String message;

    private Object obj;

    private Object data;

    private String result;
    
    public static ResponseMessage success(Object obj)
    {
        return new ResponseMessage(true, obj);
    }
    
    public static ResponseMessage success(String message, Object obj)
    {
        return new ResponseMessage(true, message, obj);
    }
    
    public static ResponseMessage success(String message)
    {
        return new ResponseMessage(true, message);
    }
    
    public static ResponseMessage success()
    {
        return new ResponseMessage(true);
    }
    
    public static ResponseMessage warning(String message)
    {
        return new ResponseMessage(true, WARNING, message);
    }
    
    public static ResponseMessage error(String code, String message)
    {
        return new ResponseMessage(false, code, message);
    }
    
    public static ResponseMessage error(String message)
    {
        return new ResponseMessage(false, ERR_UNKNOWN, message);
    }
    
    public static ResponseMessage noLogin(String message)
    {
        return new ResponseMessage(false, ERR_NOT_LOGGED, message);
    }
    
    public static ResponseMessage noPermission(String message)
    {
        return new ResponseMessage(false, ERR_UNAUTHORIZED, message);
    }
    
    public static ResponseMessage noPermission(String message, Object obj)
    {
        return new ResponseMessage(false, ERR_UNAUTHORIZED, message, obj);
    }
    
    public static ResponseMessage noRegist(String message)
    {
        return new ResponseMessage(false, ERR_NOT_REGIST, message);
    }
    
    public static ResponseMessage error(String message, Object obj)
    {
        return new ResponseMessage(false, message, obj);
    }
    
    public static ResponseMessage error(int code)
    {
        return new ResponseMessage(false, code);
    }
    
    private ResponseMessage(boolean success, String message, Object obj)
    {
        this.success = success;
        this.message = message;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, String code, String message)
    {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private ResponseMessage(boolean success, String code, String message, Object obj)
    {
        this.success = success;
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, Object obj)
    {
        this.success = success;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }
    
    private ResponseMessage(boolean success)
    {
        this.success = success;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public Object getObj()
    {
        return obj;
    }
    
    public void setObj(Object obj)
    {
        this.obj = obj;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
