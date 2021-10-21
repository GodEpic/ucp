package com.third.coupon;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24.
 */
public class PHDIUserInfo implements Serializable
{

    private String userCode;
    private String level;
    private String oldUserCode;
    private String nickName;
    private String email;
    private Integer sex;
    private String birthday;
    private String identitycard;
    private Integer cityid;
    private String cityName;
    private String address;
    private Long createTime;
    private Long updateTime;
    private Integer isdel;
    private String headportrait;
    private String identityCard;

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getOldUserCode()
    {
        return oldUserCode;
    }

    public void setOldUserCode(String oldUserCode)
    {
        this.oldUserCode = oldUserCode;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getIdentitycard()
    {
        return identitycard;
    }

    public void setIdentitycard(String identitycard)
    {
        this.identitycard = identitycard;
    }

    public Integer getCityid()
    {
        return cityid;
    }

    public void setCityid(Integer cityid)
    {
        this.cityid = cityid;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Long createTime)
    {
        this.createTime = createTime;
    }

    public Long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getIsdel()
    {
        return isdel;
    }

    public void setIsdel(Integer isdel)
    {
        this.isdel = isdel;
    }

    public String getHeadportrait()
    {
        return headportrait;
    }

    public void setHeadportrait(String headportrait)
    {
        this.headportrait = headportrait;
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }
}
