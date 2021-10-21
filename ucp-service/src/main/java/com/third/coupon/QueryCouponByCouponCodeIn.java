
package com.third.coupon;

import java.io.Serializable;
import java.util.Date;

public class QueryCouponByCouponCodeIn implements Serializable {
    private String couponCode;
    private Boolean needStoreInfo;
    private Date transTime;
    private String channelId;
    private String signature;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Boolean getNeedStoreInfo() {
        return needStoreInfo;
    }

    public void setNeedStoreInfo(Boolean needStoreInfo) {
        this.needStoreInfo = needStoreInfo;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String toString() {
        boolean needThru = true;
        StringBuilder builder = new StringBuilder();
        if (this.couponCode != null && !"".equals(this.couponCode.trim())) {
            needThru = false;
            builder.append("couponCode=").append(this.couponCode);
        }
        if (this.needStoreInfo != null) {
            builder.append("_").append("needStoreInfo=").append(this.needStoreInfo);
        }

        if(this.transTime != null) {
            builder.append("_").append("transTime=").append(this.transTime.getTime());
        }
        if(this.channelId != null && !"".equals(this.channelId.trim())) {
            builder.append("_").append("channelId=").append(this.channelId);
        }

        String result = "";
        if (needThru) {
            result = builder.substring(1);
        } else {
            result = builder.toString();
        }

        return "{" + result + "}";
    }
}