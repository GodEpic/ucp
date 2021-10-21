
package com.third.coupon;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CouponStandardInfo implements Serializable {


    private String couponCode;
    private String userCode;
    private Boolean isCallOuterApi;
    private String channelRemark;
    private String pictureUrl;
    private int placeStatus;
    private int activatedStatus;
    private int useStatus;
    private int validStatus;
    private Date validStartTime;
    private Date validEndTime;
    private Date placeTime;
    private Date activatedTime;
    private Date usedTime;
    private String activityId;
    private String activityCode;
    private String activity;
    private String activityDesc;
    private int couponUseType;
    private List<String> useChannelIds;
    private String placeChannelId;
    private Boolean storeIdLimit;
    private List<String> useStoreIds;
    private Boolean allowGiving;
    private String couponType;
    private Long couponValue;
    private Long financialValue;
    private String useRuleCode;
    private int reusableCount;
    private int usedSeqId;
    private int useRuleType;
    private int isExcluded;
    private String useBrand;
    private List<String> useMarketings;
    private String useRegions;
    private String orderId;
    private String merchantBrandId;
    private String storeId;
    private String merchantPosId;
    private String transTime;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Boolean getIsCallOuterApi() {
        return isCallOuterApi;
    }

    public void setIsCallOuterApi(Boolean isCallOuterApi) {
        this.isCallOuterApi = isCallOuterApi;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getPlaceStatus() {
        return placeStatus;
    }

    public void setPlaceStatus(int placeStatus) {
        this.placeStatus = placeStatus;
    }

    public int getActivatedStatus() {
        return activatedStatus;
    }

    public void setActivatedStatus(int activatedStatus) {
        this.activatedStatus = activatedStatus;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public int getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(int validStatus) {
        this.validStatus = validStatus;
    }

    public String getValidStartTime() {
        if (null == validStartTime) {
            return null;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(validStartTime);
    }

    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    public String getValidEndTime() {
        if (null == validEndTime) {
            return null;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(validEndTime);
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public Date getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(Date placeTime) {
        this.placeTime = placeTime;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public int getCouponUseType() {
        return couponUseType;
    }

    public void setCouponUseType(int couponUseType) {
        this.couponUseType = couponUseType;
    }

    public List<String> getUseChannelIds() {
        return useChannelIds;
    }

    public void setUseChannelIds(List<String> useChannelIds) {
        this.useChannelIds = useChannelIds;
    }

    public String getPlaceChannelId() {
        return placeChannelId;
    }

    public void setPlaceChannelId(String placeChannelId) {
        this.placeChannelId = placeChannelId;
    }

    public Boolean getStoreIdLimit() {
        return storeIdLimit;
    }

    public void setStoreIdLimit(Boolean storeIdLimit) {
        this.storeIdLimit = storeIdLimit;
    }

    public List<String> getUseStoreIds() {
        return useStoreIds;
    }

    public void setUseStoreIds(List<String> useStoreIds) {
        this.useStoreIds = useStoreIds;
    }

    public Boolean getAllowGiving() {
        return allowGiving;
    }

    public void setAllowGiving(Boolean allowGiving) {
        this.allowGiving = allowGiving;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public Long getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Long couponValue) {
        this.couponValue = couponValue;
    }

    public Long getFinancialValue() {
        return financialValue;
    }

    public void setFinancialValue(Long financialValue) {
        this.financialValue = financialValue;
    }

    public String getUseRuleCode() {
        return useRuleCode;
    }

    public void setUseRuleCode(String useRuleCode) {
        this.useRuleCode = useRuleCode;
    }

    public int getReusableCount() {
        return reusableCount;
    }

    public void setReusableCount(int reusableCount) {
        this.reusableCount = reusableCount;
    }

    public int getUsedSeqId() {
        return usedSeqId;
    }

    public void setUsedSeqId(int usedSeqId) {
        this.usedSeqId = usedSeqId;
    }

    public int getUseRuleType() {
        return useRuleType;
    }

    public void setUseRuleType(int useRuleType) {
        this.useRuleType = useRuleType;
    }

    public int getIsExcluded() {
        return isExcluded;
    }

    public void setIsExcluded(int isExcluded) {
        this.isExcluded = isExcluded;
    }

    public String getUseBrand() {
        return useBrand;
    }

    public void setUseBrand(String useBrand) {
        this.useBrand = useBrand;
    }

    public List<String> getUseMarketings() {
        return useMarketings;
    }

    public void setUseMarketings(List<String> useMarketings) {
        this.useMarketings = useMarketings;
    }

    public String getUseRegions() {
        return useRegions;
    }

    public void setUseRegions(String useRegions) {
        this.useRegions = useRegions;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantBrandId() {
        return merchantBrandId;
    }

    public void setMerchantBrandId(String merchantBrandId) {
        this.merchantBrandId = merchantBrandId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMerchantPosId() {
        return merchantPosId;
    }

    public void setMerchantPosId(String merchantPosId) {
        this.merchantPosId = merchantPosId;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
}