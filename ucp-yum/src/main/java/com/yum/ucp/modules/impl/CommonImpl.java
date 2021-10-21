package com.yum.ucp.modules.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.third.coupon.QueryCouponByCouponCodeIn;
import com.third.coupon.QueryCouponByCouponCodeOut;
import com.yum.ucp.common.web.BaseImplController;
import com.yum.ucp.modules.sys.utils.CouponUtils;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/13.
 */
@Controller
@RequestMapping(value = "${adminPath}/third/common")
public class CommonImpl extends BaseImplController
{

    /**
     * 券号查券信息
     *
     * @param couponCode
     *
     * @return
     *
     * @throws Exception
     */
    @RequestMapping("queryCouponByCouponCode")
    @ResponseBody
    public ResponseMessage queryCouponByCouponCode(String couponCode) throws Exception
    {
        QueryCouponByCouponCodeOut out = null;
        String url = baseCouponUrl + "/queryCouponByCouponCodeForAdmin";
        logger.info("根据券号查券信息接口,接口地址：{}", url);
        String params = getQueryCouponByCouponCodeIn(couponCode);
        logger.info("根据券号查券信息接口,输入参数：{}", params);

        HttpResponse response = this.execute(url, params);

        //发送Post,并返回一个HttpResponse对象
        int responseStatus = response.getStatusLine().getStatusCode();
        logger.info("根据券号查券信息接口，返回状态码：{}", responseStatus);
        if (responseStatus == 200)
        {//如果状态码为200,就是正常返回
            String result = EntityUtils.toString(response.getEntity());
            logger.info("根据券号查券信息接口，返回结果：{}", result);
            out = JSON.toJavaObject(JSONObject.parseObject(new String(result.getBytes("ISO-8859-1"), "UTF-8")), QueryCouponByCouponCodeOut.class);
            if (null == out.getCouponStandardInfo())
            {
                return ResponseMessage.error("未查到相应信息");
            }
            return ResponseMessage.success(JSONObject.toJSON(out.getCouponStandardInfo()));
        } else
        {
            return ResponseMessage.error(ReadConfig.getErrorValue(String.valueOf(response.getStatusLine().getStatusCode())));
        }
    }


    public String getQueryCouponByCouponCodeIn(String couponCode)
    {
        QueryCouponByCouponCodeIn in = new QueryCouponByCouponCodeIn();
        in.setCouponCode(couponCode);
        in.setNeedStoreInfo(false);
        in.setTransTime(new Date());
        in.setChannelId(ReadConfig.getSsoValue("third_coupon_channel"));
        String key = in.toString() + ReadConfig.getSsoValue("third_coupon_key");
        logger.info("根据券号查券信息接口，签名加密前字符串：{}", key);
        in.setSignature(CouponUtils.getMd5Upper(key, null));
        return JSON.toJSONString(in);
    }

}
