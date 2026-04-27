package com.leadnews.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * MD5鍔犲瘑
     * @param str
     * @return
     */
    public final static String encode(String str) {
        try {
            //鍒涘缓鍏锋湁鎸囧畾绠楁硶鍚嶇О鐨勬憳瑕?
            MessageDigest md = MessageDigest.getInstance("MD5");
            //浣跨敤鎸囧畾鐨勫瓧鑺傛暟缁勬洿鏂版憳瑕?
            md.update(str.getBytes());
            //杩涜鍝堝笇璁＄畻骞惰繑鍥炰竴涓瓧鑺傛暟缁?
            byte mdBytes[] = md.digest();
            String hash = "";
            //寰幆瀛楄妭鏁扮粍
            for (int i = 0; i < mdBytes.length; i++) {
                int temp;
                //濡傛灉鏈夊皬浜?鐨勫瓧鑺?鍒欒浆鎹负姝ｆ暟
                if (mdBytes[i] < 0)
                    temp = 256 + mdBytes[i];
                else
                    temp = mdBytes[i];
                if (temp < 16)
                    hash += "0";
                //灏嗗瓧鑺傝浆鎹负16杩涘埗鍚庯紝杞崲涓哄瓧绗︿覆
                hash += Integer.toString(temp, 16);
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeWithSalt(String numStr, String salt) {
        return encode(encode(numStr) + salt);
    }

    public static void main(String[] args) {
        System.out.println(encode("test"));//e10adc3949ba59abbe56e057f20f883e
        System.out.println(encodeWithSalt("123456","123456"));//5f1d7a84db00d2fce00b31a7fc73224f
    }
}
