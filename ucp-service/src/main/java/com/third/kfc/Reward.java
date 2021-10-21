package com.third.kfc;

import com.yum.ucp.common.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2019/3/25.
 */
public class Reward implements Serializable {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 原始订单编号
     */
    private String originalOrderId;

    /**
     * 品牌信息
     */
    private BrandInfo brandInfo;

    /**
     * 营销渠道
     */
    private String scene;
    /**
     * channel
     */
    private String channel;

    private Long timestamp;

    private String signature;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BrandInfo getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(BrandInfo brandInfo) {
        this.brandInfo = brandInfo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    @Override
    public String toString() {
        boolean needThru = true;
        StringBuilder builder = new StringBuilder();
        if (userCode != null && !"".equals(userCode.trim())) {
            needThru = false;
            builder.append("userCode=").append(this.userCode);
        }

        if (orderId != null && !"".equals(orderId.trim())) {
            builder.append("#").append("orderId=").append(orderId);
        }

        if (originalOrderId != null && !"".equals(originalOrderId.trim())) {
            builder.append("#").append("originalOrderId=").append(originalOrderId);
        }

        if (scene != null && !"".equals(scene.trim())) {
            builder.append("#").append("scene=").append(scene);
        }

        if (brandInfo != null) {
            builder.append("#brandInfo={");
            if (StringUtils.isNotEmpty(brandInfo.getCorpCode())) {
                builder.append("corpCode=").append(brandInfo.getCorpCode());
            }
            if (StringUtils.isNotEmpty(brandInfo.getBrandCode())) {
                builder.append("#").append("brandCode=").append(brandInfo.getBrandCode());
            }
            builder.append("}");
        }

        if (channel != null && !"".equals(channel.trim())) {
            builder.append("#").append("channel=").append(channel);
        }

        if (this.timestamp != null) {
            builder.append("#").append("timestamp=").append(timestamp);
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
