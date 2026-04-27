package com.leadnews.utils.common;

/**
 * 鍒嗙墖妗跺瓧娈电畻娉?
 */
public class BurstUtils {

    public final static String SPLIT_CHAR = "-";

    /**
     * 鐢?绗﹀彿閾炬帴
     * @param fileds
     * @return
     */
    public static String encrypt(Object... fileds){
        StringBuffer sb  = new StringBuffer();
        if(fileds!=null&&fileds.length>0) {
            sb.append(fileds[0]);
            for (int i = 1; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 榛樿绗竴缁?
     * @param fileds
     * @return
     */
    public static String groudOne(Object... fileds){
        StringBuffer sb  = new StringBuffer();
        if(fileds!=null&&fileds.length>0) {
            sb.append("0");
            for (int i = 0; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }
}

