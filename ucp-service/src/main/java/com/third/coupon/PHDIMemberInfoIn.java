package com.third.coupon;

import com.alibaba.fastjson.annotation.JSONField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */
public class PHDIMemberInfoIn implements Serializable {

    private String channel;

    private String phone;

    private Long timestamp;

    private String signature;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("phone").append("=").append(this.phone).append("#")
                .append("channel").append("=").append(this.channel).append("#")
                .append("timestamp").append("=").append(this.timestamp)
                .append("}");
        return sb.toString();
    }
}
