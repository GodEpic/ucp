package com.third.coupon;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */
public class PHDIMemberInfoOut implements Serializable {

    private Boolean success;
    private String message;
    private Integer result;
    private PHDIUserInfo data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public Integer getResult()
    {
        return result;
    }

    public void setResult(Integer result)
    {
        this.result = result;
    }

    public PHDIUserInfo getData()
    {
        return data;
    }

    public void setData(PHDIUserInfo data)
    {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
