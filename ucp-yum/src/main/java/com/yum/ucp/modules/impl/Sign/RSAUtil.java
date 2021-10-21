package com.yum.ucp.modules.impl.Sign;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;

/**
 * @author Vincent.Ding on 2018/1/17.
 * @version 1.0
 */

public class RSAUtil {

    private static String rsaPath = "rsa";

    /**
     * 公钥解密
     *
     * @param cipher
     * @return
     * @throws Exception
     */
    public static String decode(String cipher) throws Exception {

        byte[] bytes = RSAEncryptEntity.getInstance(rsaPath).decrypt(Base64.decode(cipher));
        return new String(bytes);

    }
    public static String encode(String plainText)  throws Exception{
        byte[] bytes = RSAEncryptEntity.getInstance(rsaPath).encrypt(plainText.getBytes());
        return Base64.encode(bytes).replaceAll("[\\s*\t\n\r]", "");
    }
    /**
     * 私钥加密
     *
     * @param appId
     * @param appSecret
     * @param timestamp
     * @return
     */
    public static String encodeForApp(String appId, String appSecret, long timestamp) throws Exception {
        String temp = "{0}&{1}&{2}";
        String plainText = MessageFormat.format(temp, appId, appSecret, String.valueOf(timestamp));
        return encode(plainText);
    }

    public static String encodeForToken(String userName,long timestamp) throws Exception {
        String temp = "{0}&{1}";
        String plainText = MessageFormat.format(temp, userName, String.valueOf(timestamp));
        return encode(plainText);
    }

    public final static String  PRIVATE_KEY_STR=
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANzXCo8ke8OtCL7N+shireTouq6KY3U6TAO4kQ4BVx" +
                    "T/9zsmc5Jv1rLa8N8z1hxx+XoNoWL3qod3bQBzFqdn7bOsJp70pMmu9cA5p9LezhZYrBuVJeJx1u3iXB2LmG6ySPsSL" +
                    "d4i8PxcFFprO0hReI4yiPVhYkm+l2KHIXLzAHrnAgMBAAECgYEAnwzBLha4jFUy1zjCRU/MrfBvgwVoXCWAWa2zysJs" +
                    "VVOEW7V1xyGFH4tPr3FF43aylpT6hqyYxLfeaVR6HFddeZLgGiyZrTjazqK5vxw7nIbUhdQBY+t4jwSdVxt9NRvb7EQ" +
                    "VBnkAA/fpMknn9dUGTPFzma7x5SJt8l4rCpMwfAECQQD3XR4GlTaL3FhqA+uCInenTrnOOPWY5fT1zo7etU2ayVfGft" +
                    "ZNtMdrc+p/exJ3jkSdt5YCZQawrT2WEsjNr/HxAkEA5IzcU6iwBWCGMoYKph1XLFNrn6l0U9ckVEY/ZcqRP6wGZeJYz" +
                    "yAbTAVktaGqU21803hGtjoShWS86fSVVAdiVwJAfm54v1Ka26eXBTDVueEWGUyyyYXxeeHnb4/RFGHEtZ29oHYJ4RPK" +
                    "bdCEhf4ItVThG1/Pa4/peHpDpSYavo70cQJAGfhjpOH7NnE0bpNKrHuTB0dnaFoiDNDaoPTmiVOOU7TheTxSKR8V2vq" +
                    "V6gm3yAkd8Q2uw1AwAJZk3RTczxKTuwJAd37DIqRB1WHQM8FR+xNUbEUWsxKj7+rEPVwrNQcJvvwQfh9+rk4G/adS1n" +
                    "wJH5MwyLuce/dkJ1m/x2Z++NmmxQ==";

    public static String sign(byte[] data) throws Exception {
        byte[] keyBytes = Base64.decode(PRIVATE_KEY_STR);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    public static void main(String[] args) throws Exception{
        byte [] test = "{phone=18761871478&businesstype=1}_baisheng/baisheng".getBytes();
        String sign =sign(test);
        System.out.println(sign);
    }

}
