
package com.third.coupon;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QueryCouponByUserOut implements Serializable {

    private List<CouponStandardInfo> CouponStandardInfos;
    private int RecordCount;
    private Date transTime;
    private String channelId;
    private String signature;

    public List<CouponStandardInfo> getCouponStandardInfos() {
        return CouponStandardInfos;
    }

    public void setCouponStandardInfos(List<CouponStandardInfo> couponStandardInfos) {
        CouponStandardInfos = couponStandardInfos;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
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
}