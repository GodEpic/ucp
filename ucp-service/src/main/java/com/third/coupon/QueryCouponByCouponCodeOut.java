
package com.third.coupon;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QueryCouponByCouponCodeOut implements Serializable {

    private CouponStandardInfo couponStandardInfo;
    private List<String> useStoreInfos;
    private Date transTime;
    private String channelId;
    private String signature;


    public List<String> getUseStoreInfos() {
        return useStoreInfos;
    }

    public void setUseStoreInfos(List<String> useStoreInfos) {
        this.useStoreInfos = useStoreInfos;
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

    public CouponStandardInfo getCouponStandardInfo() {
        return couponStandardInfo;
    }

    public void setCouponStandardInfo(CouponStandardInfo couponStandardInfo) {
        this.couponStandardInfo = couponStandardInfo;
    }
}