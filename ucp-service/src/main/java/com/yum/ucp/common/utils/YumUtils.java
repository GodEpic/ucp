package com.yum.ucp.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.google.common.collect.Lists;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import javax.mail.internet.InternetAddress;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/4/7.
 */
public class YumUtils {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(YumUtils.class);

    /**
     * ???????????? ????????????
     *
     * @return
     */
    public String sendMsg(String cellPhoneNO, String content, String sn, String psw) {
        try {
            String account = ReadConfig.getSsoValue("dhst.account");//??????
            String password = ReadConfig.getSsoValue("dhst.password");//??????????????????md5(??????)
            String sign = null;
            sign = replaceContent(content);
            if (StringUtils.isNotEmpty(sign) && sign.equals("error")) {
                logger.warn("sms send fail.");
                return Global.SEND_FAILED;
            }
            content = content.replace("[" + sign + "]", "");
            content = content.replace("???" + sign + "???", "");
            String result = "";
            CloseableHttpClient client = HttpClients.createDefault();
            String url = ReadConfig.getSsoValue("dhst.url");
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("account", account);
            jsonParam.put("password", DigestUtils.md5Hex(password.getBytes("ascii")));//32???md5??????
            jsonParam.put("msgid", IdGen.uuid());//??????????????????(32???UUID)??????????????????????????????
            jsonParam.put("phones", cellPhoneNO);
            jsonParam.put("content", content);
            if (StringUtils.isNotEmpty(sign)) {
                jsonParam.put("sign", "???" + sign + "???");
            } else {
                jsonParam.put("sign", sign);
            }
            jsonParam.put("sendtime", DateUtils.getDateTime(new Date()));//????????????
            jsonParam.put("subcode", "");
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//????????????????????????
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            CloseableHttpResponse response = client.execute(httpPost);//??????????????????????????????????????????????????????
            HttpEntity entity2 = response.getEntity();//??????????????????
            if (entity2 != null) {
                result = EntityUtils.toString(entity2, "utf-8");//????????????????????????????????????String??????
            }
            EntityUtils.consume(entity2);
            response.close();//????????????

            if (result.contains("????????????")) {
                logger.info("sms send success.");
                return Global.SUCCESS;
            } else {
                logger.warn("sms send fail.");
                return Global.SEND_FAILED;
            }
        } catch (Exception e) {
            logger.info(e.getStackTrace().toString());
            logger.warn("sms send fail.");
            return Global.SEND_FAILED;
        }
        //return result;//??????????????????
    }

    public String replaceContent(String content) {
        try {
            if (content.indexOf("???") > -1) {
                String sign1 = content.substring(content.indexOf("???") + 1, content.indexOf("???"));
                if (StringUtils.isNotEmpty(sign1)) {
                    return sign1;
                }
            }
            if (content.indexOf("[") > -1) {
                String sign2 = content.substring(content.indexOf("[") + 1, content.indexOf("]"));
                if (StringUtils.isNotEmpty(sign2)) {
                    return sign2;
                }
            }
        } catch (Exception e) {
            logger.warn("sms send fail.");
            return "error";
        }

        return null;
    }

//    /**
//     * Yum ????????????
//     *
//     * @param cellPhoneNO
//     * @param content
//     * @param sn
//     * @param psw
//     *
//     * @return
//     */
    /*public String sendMsg(String cellPhoneNO, String content, String sn, String psw)
    {
        String pswMD5 = getMD5(psw);
        logger.info("send message params:sn={},pwd={},mobile={},content={},ext=,stime=,rrid=,msgfmt=", sn, pswMD5, cellPhoneNO, content);

        Future<HttpResponse> httpResponseFuture;
        String postReturn = null;
        try
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sn", sn));
            params.add(new BasicNameValuePair("pwd", pswMD5));
            params.add(new BasicNameValuePair("mobile", cellPhoneNO));
            params.add(new BasicNameValuePair("content", content));
            params.add(new BasicNameValuePair("ext", ""));
            params.add(new BasicNameValuePair("stime", ""));
            params.add(new BasicNameValuePair("rrid", ""));
            params.add(new BasicNameValuePair("msgfmt", ""));

            String url = ReadConfig.getSsoValue(Global.YUM_SMS_URL);
            httpResponseFuture = doAyncPost(url, params);
            InputStream inputStream = httpResponseFuture.get().getEntity().getContent();
            postReturn = IOUtils.readLines(inputStream).get(0);
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e)
        {
            logger.error("message send error,error message:", e);
        } catch (IOException e)
        {
            logger.error("message send error,error message:", e);
        }

        logger.info("sms system return message???{}", postReturn);
        if (postReturn == null || postReturn.length() <= 3)
        {
            logger.warn("sms send fail.");
            return Global.SEND_FAILED;

        }

        if (StringUtils.startsWith(postReturn, "-6"))
        {
            logger.warn("sms send fail.");
            String key = StringUtils.replace(postReturn, "-", "");
            return ReadConfig.init().getSMSCodeMap().get(key);
        }

        logger.info("sms send seccuss.");
        return Global.SUCCESS;
    }*/

    private String getMD5(String str) {
        String s;
        try {
            s = DigestUtils.md5Hex(str.getBytes("ascii")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            logger.error("MD5????????????????????????????????????", e);
            s = "";
        }
        return s;
    }

    private Future<HttpResponse> doAyncPost(String url, List<NameValuePair> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("ContentType", "application/x-www-form-urlencoded");
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        this.closeableHttpAsyncClient.start();
        return this.closeableHttpAsyncClient.execute(httpPost, null);
    }

    private void initializePool() {
        try {
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .setConnectTimeout(5000)
                    .setSoTimeout(5000)
                    .setTcpNoDelay(false)
                    .build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                }
            };

            ctx.init(null, new TrustManager[]
                    {
                            tm
                    }, null);

            // Allow TLSv1 protocol only
            SSLIOSessionStrategy sslSessionStrategy = new SSLIOSessionStrategy(
                    ctx,
                    new String[]
                            {
                                    "TLSv1"
                            },
                    null,
                    SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", sslSessionStrategy)
                    .build();

            PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor, registry);
            cm.setMaxTotal(100);
            cm.setDefaultMaxPerRoute(30);

            System.setProperty("jsse.enableSNIExtension", "false");

            this.closeableHttpAsyncClient = HttpAsyncClients.custom()
                    .setConnectionManager(cm).setMaxConnTotal(1000).setThreadFactory(NioThreadFactory.getInstance())
                    .build();
            this.closeableHttpAsyncClient.start();

            IdleConnectionEvictor idleConnectionEvictor = new IdleConnectionEvictor(cm);
            idleConnectionEvictor.setName("IdleConnectionEvictor");
            idleConnectionEvictor.start();
        } catch (IOReactorException | NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("pool initialize has error,error message:", e);
        }
    }

    private YumUtils() {
    }

    private volatile static YumUtils utils;

    public static YumUtils initUtils() {
        if (utils == null) {
            synchronized (YumUtils.class) {
                if (utils == null) {
                    utils = new YumUtils();
                    utils.initializePool();
                }
            }
        }
        return utils;
    }

    private CloseableHttpAsyncClient closeableHttpAsyncClient;

    private void deleteTmpFile(List<File> fileList) {
        for (File file : fileList) {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            logger.debug("?????????????????????{}", file.getAbsolutePath());
        }
    }



    public String sendEmail4Yum(String toEmail, String ccEmail, String subject, String body, String emailCount, String emailPassword) {
        YumHtmlEmail hemail = new YumHtmlEmail();
        String result = Global.SEND_FAILED;
        try {
            hemail.setHostName(ReadConfig.getSsoValue(Global.YUM_EMAIL_HOST));
            hemail.setSmtpPort(Integer.parseInt(ReadConfig.getSsoValue(Global.YUM_EMAIL_PORT)));
            hemail.setCharset("utf-8");
            hemail.addTo(toEmail);
            if (StringUtils.isNotEmpty(ccEmail)) {
                //????????????????????????--??????????????????
                String[] ccEmailList = ccEmail.split(";");
                for (String ccEmailOne : ccEmailList) {
                    //?????????setcc
                    hemail.addCc(ccEmailOne);
                }
            }
            hemail.setSSL(Boolean.valueOf(ReadConfig.getSsoValue(Global.YUM_EMAIL_SSL)));
            hemail.setFrom(emailCount, ReadConfig.getSsoValue(Global.YUM_EMAIL_HOSTNAME));
            hemail.setAuthentication(emailCount, emailPassword);
            hemail.setSubject(subject);
            hemail.setMsg(StringEscapeUtils.unescapeHtml4(body));
            List<File> fileList = new ArrayList<File>();
            hemail.setBoolHasAttachments(true);
            hemail.send();
            if (fileList.size() > 0) {
                deleteTmpFile(fileList);
            }
            result = Global.SUCCESS;
            logger.info("email send true!");
        } catch (Exception e) {
            logger.error("email send has error,error message:", e);
        }
        return result;
    }
}
