package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class AesUtil {
    private static SecretKeySpec keySpec;
    private static IvParameterSpec iv;
    private static Cipher cipher;

    static{
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init(byte[] aesKey) {
        if ((aesKey.length == 16)||(aesKey.length==24)||(aesKey.length==32)) {
            keySpec = new SecretKeySpec(aesKey, "AES");
            iv = new IvParameterSpec(Md5Util.encrypt(aesKey));
        }
        else{
            throw new RuntimeException("密钥长度无效，只能是16、24或32字节");
        }
    }

    /**
     * AES加密
     * @param key 密钥
     * @param primitivaValue 原始值
     * @return 加密值
     */
    public static byte[] encrypt(byte[] key,byte[] primitivaValue) {
        init(key);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return  cipher.doFinal(primitivaValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     * @param key 密钥
     * @param encryptedValue 加密值
     * @return 原始值
     * @throws UnsupportedEncodingException 
     */
//    public static byte[] decrypt(byte[] key,byte[] encryptedValue) {
//        init(key);
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
//            return cipher.doFinal(encryptedValue);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
    public static byte[] decrypt(byte[] content, String password) throws UnsupportedEncodingException {
        try {
                 KeyGenerator kgen = KeyGenerator.getInstance("AES");
                 kgen.init(128, new SecureRandom(password.getBytes("UTF-8")));
                 SecretKey secretKey = kgen.generateKey();
                 byte[] enCodeFormat = password.getBytes("UTF-8");
                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
                byte[] result = cipher.doFinal(content);
                return result; // 加密
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (NoSuchPaddingException e) {
                e.printStackTrace();
        } catch (InvalidKeyException e) {
                e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
        } catch (BadPaddingException e) {
                e.printStackTrace();
        }
        return null;
    }
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                        hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
                return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
