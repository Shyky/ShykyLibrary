package com.shyky.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 反射操作相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/9
 * @since 1.0
 */
public final class ReflectUtil {
    public enum Type {
        CLASS, INTERFACE, ENUM, ANNOTATION
    }

    /**
     * 构造方法私有化
     */
    private ReflectUtil() {

    }

    public static String getUniqueName(Class<?> clz) {
        return clz.getName();
    }

    public static String getSimpleName(Class<?> clz) {
        return clz.getSimpleName();
    }

    public static String getSimpleName(Field field) {
        return field.getName();
    }

    public static String getSimpleName(Method method) {
        return method.getName();
    }

    public static String getClassUniqueName(Class<?> clz) {
        return getUniqueName(clz);
    }

    public static String getClassSimpleName(Class<?> clz) {
        return getSimpleName(clz);
    }

    public static Object getClassInstance(Class<?> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getSuperclass(Class<?> clz) {
        return clz.getSuperclass();
    }

    public static String getSuperclassName(Class<?> clz) {
        return clz.getSuperclass().getName();
    }

    public static String getSuperclassSimpleName(Class<?> clz) {
        return clz.getSuperclass().getSimpleName();
    }

    public static Type getType(Class<?> clz) {
        if (clz.isAnnotation())
            return Type.ANNOTATION;
        else if (clz.isEnum())
            return Type.ENUM;
        else if (clz.isInterface())
            return Type.INTERFACE;
        return Type.CLASS;
    }

    public static String getFieldSimpleName(Field field) {
        return getSimpleName(field);
    }

    public static Field[] getDeclaredFields(Class<?> clz) {
        return clz.getDeclaredFields();
    }

    public static List<Field> getDeclaredFieldList(Class<?> clz) {
        return Arrays.asList(clz.getDeclaredFields());
    }

    public static String getDeclaredFieldName(Class<?> clz, String fieldName) {
        try {
            return clz.getDeclaredField(fieldName).getName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getDeclaredFieldType(Class<?> clz, String fieldName) {
        try {
            return clz.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeclaredFieldTypeUniqueName(Class<?> clz, String fieldName) {
        return getUniqueName(getDeclaredFieldType(clz, fieldName));
    }

    public static String getDeclaredFieldTypeSimpleName(Class<?> clz, String fieldName) {
        return getSimpleName(getDeclaredFieldType(clz, fieldName));
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value     : 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            if (field != null) {
                //抑制Java对其的检查
                field.setAccessible(true);
                //将 object 中 field 所代表的值 设置为 value
                field.set(object, value);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */

    public static Object getFieldValue(Object object, String fieldName) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            if (field != null) {
                //抑制Java对其的检查
                field.setAccessible(true);
                //获取 object 中 field 所代表的属性值
                return field.get(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射获取对象的私有属性
     *
     * @param object    需要反射的对象
     * @param fieldName 属性名称
     * @return 返回当前对象的私有属性
     * @author dingpeihua
     * @date 2016/5/5 8:51
     * @version 1.0
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 反射获取类的私有属性
     *
     * @param clazz     需要反射的类
     * @param fieldName 属性名称
     * @return 返回当前对象的私有属性
     * @author dingpeihua
     * @date 2016/5/5 8:53
     * @version 1.0
     */
    public static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
    }
}