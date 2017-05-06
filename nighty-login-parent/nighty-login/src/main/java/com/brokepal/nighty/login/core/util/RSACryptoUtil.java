package com.brokepal.nighty.login.core.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACryptoUtil {
    public static class KeyPairOfString{
        private String publicKey;
        private String privateKey;
        public KeyPairOfString(String publicKey, String privateKey){
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }
    public static KeyPairOfString makeBothKeyOfString() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
        String publicKey = Base64.encode(rsaPublicKey.getEncoded());
        String privateKey = Base64.encode(rsaPrivateKey.getEncoded());
        return new KeyPairOfString(publicKey,privateKey);
    }
    /**
     * 字符串转为RSA共有密钥
     * @param key publicKey经过Base64加密后的字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    /**
     * 字符串转为RSA私有密钥
     * @param key privateKey经过Base64加密后的字符串
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    /**
     * 公钥加密
     * @param rsaPublicKey RSA公钥
     * @param src 源字符串（明文）
     * @return RSA公钥加密后的byte数组再经过Base64加密后生成的字符串
     * @throws Exception
     */
    public static String RSAEncodeWithPublicKey(RSAPublicKey rsaPublicKey,String src) throws Exception{
        com.sun.org.apache.xml.internal.security.Init.init();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] RSACryptograph = cipher.doFinal(src.getBytes());
        String cryptograph = Base64.encode(RSACryptograph);
        return cryptograph;
    }
    /**
     * 私钥解密
     * @param rsaPrivateKey RSA私钥
     * @param cryptograph 经过RSA公钥加密后再经过Base64加密生成的字符串
     * @return 源字符串（明文）
     * @throws Exception
     */
    public static String RSADecodeWithPrivateKey(RSAPrivateKey rsaPrivateKey,String cryptograph) throws Exception{
        com.sun.org.apache.xml.internal.security.Init.init();
        byte[] RSACryptograph = Base64.decode(cryptograph);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] src = cipher.doFinal(RSACryptograph);
        return new String(src);
    }
    /**
     * 私钥加密
     * @param rsaPrivateKey RSA私钥
     * @param src 源字符串（明文）
     * @return RSA私钥加密后的byte数组再经过Base64加密后生成的字符串
     * @throws Exception
     */
    public static String RSAEncodeWithPrivateKey(RSAPrivateKey rsaPrivateKey,String src) throws Exception{
        com.sun.org.apache.xml.internal.security.Init.init();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] RSACryptograph = cipher.doFinal(src.getBytes());
        String cryptograph = Base64.encode(RSACryptograph);
        return cryptograph;
    }
    /**
     * 公钥解密
     * @param rsaPublicKey RSA公钥
     * @param cryptograph 经过RSA私钥加密后再经过Base64加密生成的字符串
     * @return 源字符串（明文）
     * @throws Exception
     */
    public static String RSADecodeWithPublicKey(RSAPublicKey rsaPublicKey,String cryptograph) throws Exception{
        com.sun.org.apache.xml.internal.security.Init.init();
        byte[] RSACryptograph = Base64.decode(cryptograph);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] src = cipher.doFinal(RSACryptograph);
        return new String(src);
    }
}
