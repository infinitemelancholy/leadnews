package com.leadnews.utils.common;


import com.leadnews.model.wemedia.pojos.WmNews;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil {

    /**
     * 搴忓垪鍖?
     * @param t
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t){
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return ProtostuffIOUtil.toByteArray(t,schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
 
    }

    /**
     * 鍙嶅簭鍒楀寲
     * @param bytes
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte []bytes,Class<T> c) {
        T t = null;
        try {
            t = c.newInstance();
            Schema schema = RuntimeSchema.getSchema(t.getClass());
             ProtostuffIOUtil.mergeFrom(bytes,t,schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * jdk搴忓垪鍖栦笌protostuff搴忓垪鍖栧姣?
     * @param args
     */
    public static void main(String[] args) {
        long start =System.currentTimeMillis();
        for (int i = 0; i <1000000 ; i++) {
            WmNews wmNews =new WmNews();
            JdkSerializeUtil.serialize(wmNews);
        }
        System.out.println(" jdk 鑺辫垂 "+(System.currentTimeMillis()-start));

        start =System.currentTimeMillis();
        for (int i = 0; i <1000000 ; i++) {
            WmNews wmNews =new WmNews();
            ProtostuffUtil.serialize(wmNews);
        }
        System.out.println(" protostuff 鑺辫垂 "+(System.currentTimeMillis()-start));
    }

 
 
}

