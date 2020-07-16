package com.yusys.kafka.app.producer.userbehavior.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

public class AESUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AESUtil.class);
    // 算法名称
    private static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    private static final Integer keyLength = 128;
    // CBC模式需要初始向量
    static byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

    public static String encrypt(String sSrc,  String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password), ivParameterSpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(encrypted);// 此处使用BASE64做转码。
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException:{}", e);
        } catch (InvalidKeyException e) {
            LOG.error("InvalidKeyException:{}", e);
        } catch (InvalidAlgorithmParameterException e) {
            LOG.error("InvalidAlgorithmParameterException:{}", e);
        } catch (NoSuchPaddingException e) {
            LOG.error("NoSuchPaddingException:{}", e);
        } catch (BadPaddingException e) {
            LOG.error("BadPaddingException:{}", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException:{}", e);
        } catch (IllegalBlockSizeException e) {
            LOG.error("IllegalBlockSizeException:{}", e);
        }
        return null;
    }
    public static String decrypt(String src, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password), ivParameterSpec);
            byte[] encrypted = Base64.getDecoder().decode(src.getBytes(CHARSET));//先用base64解码
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original, CHARSET);
            return originalString;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException:{}", e);
        } catch (InvalidKeyException e) {
            LOG.error("InvalidKeyException:{}", e);
        } catch (InvalidAlgorithmParameterException e) {
            LOG.error("InvalidAlgorithmParameterException:{}", e);
        } catch (NoSuchPaddingException e) {
            LOG.error("NoSuchPaddingException:{}", e);
        } catch (BadPaddingException e) {
            LOG.error("BadPaddingException:{}", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException:{}", e);
        } catch (IllegalBlockSizeException e) {
            LOG.error("IllegalBlockSizeException:{}", e);
        }
        return null;
    }
    /**
     * 生成加解密秘钥
     * @return
     */
    public static Key getSecretKey(final String password) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 密钥长度
            kg.init(keyLength, new SecureRandom(password.getBytes(CHARSET)));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            Key result = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
            return result;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException:{}", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException:{}", e);
        }
        return null;
    }

    /*public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String password = "88888888";
        String src = "我爱中国,i love china 1949";
        String mw = AESUtil.encrypt(src, password);
        String mingwen = AESUtil.decrypt(mw, password);
        System.out.println(mw);
        System.out.println(mingwen);
        System.out.println(System.currentTimeMillis() - start);
    }*/
}
