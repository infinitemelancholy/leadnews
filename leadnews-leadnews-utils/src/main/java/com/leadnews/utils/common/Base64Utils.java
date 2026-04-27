package com.leadnews.utils.common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils {

    /**
     * 瑙ｇ爜
     * @param base64
     * @return
     */
    public static byte[] decode(String base64){
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64瑙ｇ爜
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 璋冩暣寮傚父鏁版嵁
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 缂栫爜
     * @param data
     * @return
     * @throws Exception
     */
    public static String encode(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
