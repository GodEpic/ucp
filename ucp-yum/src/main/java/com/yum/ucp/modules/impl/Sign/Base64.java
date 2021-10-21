package com.yum.ucp.modules.impl.Sign;


import java.nio.charset.Charset;

/**
 * Base encoder
 * @author Vincent.Ding on 2018/1/17.
 * @version 1.0
 */
public class Base64 {

    private static java.util.Base64.Encoder base64Encoder =  java.util.Base64.getEncoder();
    private static java.util.Base64.Decoder base64Decoder = java.util.Base64.getDecoder();

    public static String encode(byte[] b){
        return new String(base64Encoder.encode(b), Charset.forName("UTF-8"));
    }

    public static byte[] decode(String s){
        try {
            return base64Decoder.decode(s);
        }catch (Exception e){
            return null;
        }
    }

}
