package com.leadnews.utils.common;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectUtils {


    /**
     * 杞崲涓篗ap
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {
        PropertyDescriptor[] propertyDescriptorArray = getPropertyDescriptorArray(bean);
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
            Object value = getPropertyDescriptorValue(bean, propertyDescriptor);
            parameterMap.put(propertyDescriptor.getName(), value);
        }
        return parameterMap;
    }

    /**
     * 閫氳繃鍙嶅皠璁剧疆灞炴€?
     *
     * @param bean
     * @param key
     * @param value
     */
    public static void setPropertie(Object bean, String key, Object value) {
        if (null != bean && StringUtils.isNotEmpty(key)) {
            PropertyDescriptor[] descriptor = getPropertyDescriptorArray(bean);
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(descriptor, key);
            setPropertyDescriptorValue(bean, propertyDescriptor, value);
        }
    }

    /**
     * 閫氳繃鍙嶅皠璁剧疆灞炴€?
     *
     * @param bean
     * @param key
     * @param value
     * @param skipExist 鏄惁璺宠繃宸插瓨鍦ㄧ殑灞炴€?
     */
    public static void setPropertie(Object bean, String key, Object value, boolean skipExist) {
        if (null != bean && StringUtils.isNotEmpty(key)) {
            if (skipExist) {
                Object propValue = getPropertie(bean, key);
                if (null == propValue) {
                    setPropertie(bean, key, value);
                }
            } else {
                setPropertie(bean, key, value);
            }
        }
    }


    /**
     * 閫氳繃鍙嶅皠灏唌ap鐨刱ey value 鏄犲皠鍒板疄浣撶被涓?
     *
     * @param bean
     * @param skipExist 鏄惁璺宠繃宸插瓨鍦ㄧ殑灞炴€?
     */
    public static void setPropertie(Object bean, Map<String, Object> parameterMap, boolean skipExist) {
        if (null != bean && null != parameterMap && !parameterMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                setPropertie(bean, entry.getKey(), entry.getValue());
            }
        }

    }


    /**
     * 鑾峰彇灞炴€х殑鍊?
     *
     * @param bean
     * @param key
     * @return
     */
    public static Object getPropertie(Object bean, String key) {
        Object value = null;
        if (null != bean && StringUtils.isNotEmpty(key)) {
            PropertyDescriptor[] descriptor = getPropertyDescriptorArray(bean);
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(descriptor, key);
            value = getPropertyDescriptorValue(bean, propertyDescriptor);
        }
        return value;
    }


    public static Object getPropertyDescriptorValue(Object bean, PropertyDescriptor propertyDescriptor) {
        Object value = null;
        if (null != propertyDescriptor) {
            Method readMethod = propertyDescriptor.getReadMethod();
            value = invok(readMethod, bean, propertyDescriptor.getPropertyType(), null);
        }
        return value;
    }


    public static void setPropertyDescriptorValue(Object bean, PropertyDescriptor propertyDescriptor, Object value) {
        if (null != propertyDescriptor) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            invok(writeMethod, bean, propertyDescriptor.getPropertyType(), value);
        }
    }

    /**
     * 鑾峰彇 PropertyDescriptor 灞炴€?
     *
     * @param propertyDescriptorArray
     * @param key
     * @return
     */
    public static PropertyDescriptor getPropertyDescriptor(PropertyDescriptor[] propertyDescriptorArray, String key) {
        PropertyDescriptor propertyDescriptor = null;
        for (PropertyDescriptor descriptor : propertyDescriptorArray) {
            String fieldName = descriptor.getName();
            if (fieldName.equals(key)) {
                propertyDescriptor = descriptor;
                break;
            }
        }
        return propertyDescriptor;
    }


    /**
     * 鑾峰彇 PropertyDescriptor 灞炴€?
     *
     * @param bean
     * @param key
     * @return
     */
    public static PropertyDescriptor getPropertyDescriptor(Object bean, String key) {
        PropertyDescriptor[] propertyDescriptorArray = getPropertyDescriptorArray(bean);
        return getPropertyDescriptor(propertyDescriptorArray, key);
    }


    /**
     * invok 璋冪敤鏂规硶
     *
     * @param methodName
     * @param bean
     * @param targetType
     * @param value
     * @return
     */
    public static Object invok(String methodName, Object bean, Class<?> targetType, Object value) {
        Object resultValue = null;
        if (StringUtils.isNotEmpty(methodName) && null != bean) {
            Method method = getMethod(bean.getClass(), methodName);
            if (null != method) {
                resultValue = invok(method, bean, targetType, value);
            }
        }
        return resultValue;
    }

    /**
     * 璋冪敤 invok 鏂规硶
     *
     * @param method
     * @param bean
     * @param value
     */
    public static Object invok(Method method, Object bean, Class<?> targetType, Object value) {
        //  System.out.println("method:" + method.getName() + "   bean:" + bean.getClass().getName() + "     " + value);
        Object resultValue = null;
        if (null != method && null != bean) {
            try {
                int count = method.getParameterCount();
                if (count >= 1) {
                    if (null != value) {
                        value = ConvertUtils.convert(value, targetType);
                    }
                    resultValue = method.invoke(bean, value);
                } else {
                    resultValue = method.invoke(bean);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return resultValue;
    }

    /**
     * 鑾峰彇鍐呯渷鐨勫睘鎬?
     *
     * @param bean
     * @return
     */
    public static PropertyDescriptor[] getPropertyDescriptorArray(Object bean) {
        BeanInfo beanInfo = null;
        PropertyDescriptor[] propertyDescriptors = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (null != beanInfo) {
            propertyDescriptors = beanInfo.getPropertyDescriptors();
        }
        return propertyDescriptors;
    }

    /**
     * 鑾峰彇method 鏂规硶
     *
     * @param clazz
     * @param methodName
     * @return
     */
    private static Method getMethod(Class clazz, String methodName) {
        Method method = null;
        if (null != clazz) {
            try {
                method = clazz.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    private static Object getBean(Class clazz) {
        Object bean = null;
        if (null != clazz) {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 鍚屾 bean 涓殑鏁版嵁
     *
     * @param oldBean
     * @param newBean
     * @param <T>
     */
    public static <T> void syncBeanData(T oldBean, T newBean) {
        PropertyDescriptor[] descriptorArray = getPropertyDescriptorArray(newBean);
        for (PropertyDescriptor propertyDescriptor : descriptorArray) {
            Object newValue = getPropertyDescriptorValue(newBean, propertyDescriptor);
            Object oldValue = getPropertyDescriptorValue(oldBean, propertyDescriptor);
            if (null == newValue && oldValue != null) {
                setPropertyDescriptorValue(newBean, propertyDescriptor, oldValue);
            }
        }
    }


    /**
     * 閫氳繃鍙嶅皠鑾峰彇class瀛楄妭鐮佹枃浠?
     *
     * @param className
     * @return
     */
    public static Class getClassForName(String className) {
        Class clazz = null;
        if (StringUtils.isNotEmpty(className)) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }


    /**
     * 閫氳繃鍙嶅皠鑾峰彇瀵硅薄
     *
     * @param className
     * @return
     */
    public static Object getClassForBean(String className) {
        Object bean = null;
        Class clazz = getClassForName(className);
        if (null != clazz) {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 鑾峰彇灞炴€у瓧娈电殑娉ㄨВ灞炴€?
     *
     * @param bean
     * @param propertyDescriptor
     * @return
     */
    public static Annotation[] getFieldAnnotations(Object bean, PropertyDescriptor propertyDescriptor) {
        List<Field> fieldList = Arrays.asList(bean.getClass().getDeclaredFields()).stream().filter(f -> f.getName().equals(propertyDescriptor.getName())).collect(Collectors.toList());
        if (null != fieldList && fieldList.size() > 0) {
            return fieldList.get(0).getDeclaredAnnotations();
        }
        return null;
    }

    /**
     * 鑾峰彇灞炴€у瓧娈电殑娉ㄨВ灞炴€?
     *
     * @param bean
     * @param key
     * @return
     */
    public static Annotation[] getFieldAnnotations(Object bean, String key) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean, key);
        return getFieldAnnotations(bean, propertyDescriptor);
    }


}

