package com.leadnews.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * jdk搴忓垪鍖?
 */
public class JdkSerializeUtil {

    /**
     * 搴忓垪鍖?
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {

        if (obj  == null){
            throw new NullPointerException();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 鍙嶅簭鍒楀寲
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            T obj = (T)ois.readObject();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return  null;
    }



}

