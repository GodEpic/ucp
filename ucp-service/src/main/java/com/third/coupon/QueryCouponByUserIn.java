
package com.third.coupon;

import java.io.Serializable;
import java.util.Date;

public class
        QueryCouponByUserIn implements Serializable {

    private String userCode;
    private int couponStatus;
    private Boolean needStoreInfo;
    private String fromDate;
    private String toDate;
    private int offset;
    private int pageSize;
    private Date transTime;
    private String channelId;
    private String signature;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(int couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Boolean getNeedStoreInfo() {
        return needStoreInfo;
    }

    public void setNeedStoreInfo(Boolean needStoreInfo) {
        this.needStoreInfo = needStoreInfo;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
        if (this.userCode != null && !"".equals(this.userCode.trim())) {
            needThru = false;
            builder.append("userCode=").append(this.userCode);
        }
        builder.append("_").append("couponStatus=").append(this.couponStatus);
        if (this.needStoreInfo != null) {
            builder.append("_").append("needStoreInfo=").append(this.needStoreInfo);
        }
        if (this.fromDate != null) {
            builder.append("_").append("fromDate=").append(this.fromDate);
        }
        if (this.toDate != null) {
            builder.append("_").append("toDate=").append(this.toDate);
        }
            builder.append("_").append("offset=").append(this.offset);
            builder.append("_").append("pageSize=").append(this.pageSize);

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