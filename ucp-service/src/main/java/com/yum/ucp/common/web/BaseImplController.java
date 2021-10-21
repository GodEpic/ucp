/**
 * Copyright &copy; 2012-2014
 * <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yum.ucp.common.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.card.credit.core.pojo.QueryBalanceByMobileIn;
import com.card.credit.core.pojo.QueryBalanceByMobileOut;
import com.card.credit.core.util.CreditUtil;
import com.yum.ucp.modules.sys.utils.ReadConfig;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * 控制器支持类
 *
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseImplController
{

    //public static String baseCreditUrl = ReadConfig.getSsoValue("third_credit_url");
    public static String baseCouponUrl = ReadConfig.getSsoValue("third_coupon_url");
    public static String baseKFCUrl =  ReadConfig.getSsoValue("third.kfc.url");
    public static String basePHDIUrl = ReadConfig.getSsoValue("third.phdi.url");
    public static String basePointUrl = ReadConfig.getSsoValue("kgold_point_url");
    /**
     * 日志对象
     */
    public Logger logger = LoggerFactory.getLogger(getClass());

    /*
    *  根据手机号获取userCode
    *
     */
    /*public ResponseMessage getQueryBalanceByMobileOutInfo(String mobilePhone) throws Exception
    {
        QueryBalanceByMobileOut out = null;
        String url = baseCreditUrl + "/queryBalanceByMobile";
        logger.info("KFC，根据手机号获取用户标识接口，接口地址：{}", url);
        String params = getQueryBalanceByMobileInParam(mobilePhone);
        logger.info("KFC，根据手机号获取用户标识接口，输入参数：{}", params);

        //设置编码
        HttpResponse response = this.execute(url, params);
        //发送Post,并返回一个HttpResponse对象
        int responseStatus = response.getStatusLine().getStatusCode();
        logger.info("KFC，根据手机号获取用户标识接口，返回状态码：{}", responseStatus);
        if (responseStatus == 200)
        {
            String result = EntityUtils.toString(response.getEntity());
            logger.debug("KFC，根据手机号获取用户标识接口，返回结果：{}", result);
            out = JSON.toJavaObject(JSONObject.parseObject(result), QueryBalanceByMobileOut.class);
            return ResponseMessage.success(out);
        }
        return ResponseMessage.error(ReadConfig.getErrorValue(String.valueOf(response.getStatusLine().getStatusCode())));
    }*/

    public String getQueryBalanceByMobileInParam(String mobilePhone)
    {
        QueryBalanceByMobileIn in = new QueryBalanceByMobileIn();
        in.setMobile(mobilePhone);
        in.setTransTime(new Date());
        in.setChannelId(ReadConfig.getSsoValue("third_credit_channel"));
        String key = in.toString() + ReadConfig.getSsoValue("third_credit_key");
        logger.info("KFC，根据手机号获取用户标识接口，签名加密前字符串：{}", key);
        in.setSignature(CreditUtil.getMd5Upper(key, null));
        return JSON.toJSONString(in);
    }

    protected HttpResponse execute(String url, String params) throws IOException
    {
        //POST的URL
        HttpPost httppost = new HttpPost(url);
        //添加参数
        httppost.addHeader("Content-type", "application/json");
        httppost.setHeader("Accept", "application/json");
        httppost.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
        //设置编码
        return new DefaultHttpClient().execute(httppost);
    }

    protected HttpResponse get(String url) throws IOException
    {
        //POST的URL
        HttpGet httpget = new HttpGet(url);
        //添加参数
        httpget.addHeader("Content-type", "application/x-www-form-urlencoded");
        httpget.setHeader("Accept", "application/json");
        //设置编码
        return new DefaultHttpClient().execute(httpget);
    }
}
