package com.yum.ucp.modules.impl.Sign;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryptEntity {
   // private static Logger _log = LoggerFactory.getLogger(RSAEncryptEntity.class);
    private static Map<String, String> keyMap = new HashMap<>();
    private static String publicKey = "publicKey.keystore";
    private static String privateKey = "privateKey.keystore";
    private static  RSAEncryptEntity instance = null ;

    private RSAPublicKey RSA_PUBLIC_KEY ;
    private RSAPrivateKey RSA_PRIVATE_KEY ;

    public static RSAEncryptEntity getInstance(String relativePath){
        if(instance == null ){
            instance = new RSAEncryptEntity();
        }
        return instance;
    }


    private RSAEncryptEntity(){
        try {
            //获取公钥以及加密
            String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCND1oR3h6jmVwMPIRXZDVERzWeN4dKSfgtaz/V5yCu66Dq7oAt2sxMmhilEiRorHBkG9t5Dv60aHwl6fvjudUJ7zOFoKDFxdYEPmTVqIC0zzZJ1J0mRNoFjRBTgbI7OVo0cVjvpeJ8j8eWjqqX4osE8xceu/w4MG7fBrRjygWxIwIDAQAB";
            byte[] publicKeyStrByte = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyStrByte);
            RSA_PUBLIC_KEY = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        }catch (Exception e){
            e.printStackTrace();
         //   _log.error("初始化RSA Encrypt 错误",e);
        }
    }


    /**
     * 公钥加密过程
     *
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public  byte[] encrypt(byte[] plainTextData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, RSA_PUBLIC_KEY);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }


    /**
     * 私钥解密过程
     *
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public  byte[] decrypt( byte[] cipherData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, RSA_PRIVATE_KEY);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new Exception("密文数据已损坏");
        }
    }


    public static void main(String[] ars){
        RSAEncryptEntity entity = RSAEncryptEntity.getInstance("E:\\git_location\\springcloud\\yum-csp\\csp\\csp-server\\csp-api-gateway\\src\\main\\resources\\rsa");

        byte[] tokenBt = Base64.decode("ZX/p12GbNULJC6I8acVC81hxYZXWV/ntzq7mMnDqGj0YDjUh7ZD48LpXSGunmPV8ASVn7Y3Sn0mKaedyn6nrj4BLIf/Gi6YzBuDDToFaXXw++rw+89OAqlGLsJ2Am7n6qn5s0DGOuvy6iNQq8D5dT+eLgr3MUAobmAsnyq1UzZ0=");
        for(int i=1;i<100000;i++){
            try {
                System.out.println(new String( entity.decrypt(tokenBt)));

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
