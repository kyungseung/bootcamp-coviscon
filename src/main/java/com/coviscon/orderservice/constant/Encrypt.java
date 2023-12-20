package com.coviscon.orderservice.constant;

import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    private static String privateKey_256 = WebConstants.PRIVATE_KEY;

    // 암호화
    public static String aesCBCEncode(String plainText) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec iv = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrpytionByte = c.doFinal(plainText.getBytes("UTF-8"));

        return Hex.encodeHexString(encrpytionByte);
    }

    // 복호화
    public static String aesCBCDecode(String encodeText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec iv = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());
        return new String(c.doFinal(decodeByte), "UTF-8");
    }


}
