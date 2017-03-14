package com.shyky.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import com.shyky.library.BaseApplication;
import com.shyky.library.bean.base.BaseBean;
import com.shyky.library.constant.Constant;
import com.shyky.library.exception.NotOverrideToStringMethodException;
import com.shyky.library.exception.NotSupportToStringFormatException;
import com.shyky.util.TextUtil;

import org.json.JSONObject;

import java.util.Iterator;

import static com.socks.library.KLog.d;

/**
 * SharedPreferences相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public final class SharedPreferencesUtil {
    /**
     * 配置文件名
     */
    private static final String FILE_NAME = "local_config";
    /**
     * SharedPreferences实例对象
     */
    private static SharedPreferences sharedPreferences;

    /**
     * 静态内部类实现单例模式
     */
    public static class Holder {
        public static final SharedPreferencesUtil INSTANCE = new SharedPreferencesUtil();
    }

    /**
     * 构造方法私有化
     */
    private SharedPreferencesUtil() {
        sharedPreferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取本类实例对象
     *
     * @return SharedPreferencesUtil实例对象
     */
    public static SharedPreferencesUtil getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 检查sharedPreferences是否初始化
     */
    private static void check() {
        if (sharedPreferences == null)
            SharedPreferencesUtil.getInstance();
    }

    /**
     * 保存boolean类型数据到配置文件中
     *
     * @param key   键名称
     * @param value boolean类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull boolean value) {
        check();
        final Editor editor = sharedPreferences.edit();
        if (null == editor || TextUtil.isEmpty(key)) {
            return false;
        }
        return editor.putBoolean(key, value).commit();
    }

    /**
     * 保存char类型数据到配置文件中
     *
     * @param key   键名称
     * @param value char类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull char value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putString(key, String.valueOf(value)).commit();
    }

    /**
     * 保存byte类型数据到配置文件中
     *
     * @param key   键名称
     * @param value byte类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull byte value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putInt(key, value).commit();
    }

    /**
     * 保存int类型数据到配置文件中
     *
     * @param key   键名称
     * @param value int类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull int value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putInt(key, value).commit();
    }

    /**
     * 保存short类型数据到配置文件中
     *
     * @param key   键名称
     * @param value short类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull short value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putInt(key, value).commit();
    }

    /**
     * 保存long类型数据到配置文件中
     *
     * @param key   键名称
     * @param value long类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull long value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putLong(key, value).commit();
    }

    /**
     * 保存float类型数据到配置文件中
     *
     * @param key   键名称
     * @param value float类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull float value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putFloat(key, value).commit();
    }

    /**
     * 保存double类型数据到配置文件中
     *
     * @param key   键名称
     * @param value double类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull double value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putFloat(key, (float) value).commit();
    }

    /**
     * 保存String类型数据到配置文件中
     *
     * @param key   键名称
     * @param value String类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull String key, @NonNull String value) {
        check();
        final Editor editor = sharedPreferences.edit();
        return !(null == editor || TextUtil.isEmpty(key)) && editor.putString(key, value).commit();
    }

    /**
     * 保存JSONObject类型数据到配置文件中，该方法会遍历JSONObject中的数据
     *
     * @param jsonObject JSONObject类型数据
     * @return 成功返回true，失败返回false
     */
    public synchronized static boolean save(@NonNull JSONObject jsonObject) {
        check();
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                d("key name", key);
            }
        }
        return false;
    }

    /**
     * 保存实体,不用反射实现,但是需要传入的实体重写toString方法,并且要有一定的返回格式,这个方法的原理就是通过返回格式进行解析的,注意:该实体必须实现显示提供无参构造方法
     *
     * @param entity   实体对象
     * @param <ENTITY> 泛型参数
     * @return 成功返回true，失败返回false
     */
    public synchronized static <ENTITY extends BaseBean> boolean save(@NonNull ENTITY entity) {
        check();
        if (entity != null) {
            // 实体没有重写toString方法,默认调用Object类的toString方法
            String suffix = '@' + Integer.toHexString(entity.hashCode());
            String toStr = entity.toString();
            // 非空判断
            if (TextUtil.isEmptyAndNull(toStr))
                throw new NullPointerException("toString方法返回空 , value = " + toStr);
            else {
                // 实体没有重写toString方法
                if (toStr.endsWith(suffix))
                    throw new NotOverrideToStringMethodException();
                else if (!toStr.contains("(") || !toStr.contains(")")) {
                    // 使用的时默认IDE生成的toString方法,但是不是这个方法所需要的,需要什么格式这里会抛出异常告诉调用处
                    throw new NotSupportToStringFormatException();
                } else {
                    // 进行分割字符串处理
                    String[] sps = toStr.split(",");
                    if (null == sps || (null != sps && sps.length == 0)) {
                        throw new NotSupportToStringFormatException();
                    } else {
                        for (int j = 0; j < sps.length; j++) {
                            String str = sps[j].trim();
                            //  String var = (tag == null ? Constant.EMPTY_STR : tag);
                            String var = null;
                            // 截取出变量名
                            if (j == 0)
                                var = str.substring(str.indexOf("{") + 1, str.lastIndexOf("(")).trim();
                            else
                                var = str.substring(0, str.lastIndexOf("(")).trim();
                            d("var" + var);
                            // 截取出数据类型
                            String type = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).trim();
                            d("type" + type);
                            // 截取出变量值
                            String data = null;
                            if (Constant.DATA_TYPE.STRING.equals(type) || Constant.DATA_TYPE.CHAR.equals(type))
                                data = str.substring(str.indexOf("\'") + 1, str.lastIndexOf("\'")).trim();
                            else {
                                // 最后一个
                                if (j == sps.length - 1)
                                    data = str.substring(str.lastIndexOf("=") + 1, str.lastIndexOf("}")).trim();
                                else
                                    data = str.substring(str.lastIndexOf("=") + 1).trim();
                            }
                            d("data" + data);
                            if (Constant.DATA_TYPE.BOOLEAN.equals(type)) {
                                save(var, Boolean.valueOf(data));
                            } else if (Constant.DATA_TYPE.BYTE.equals(type)) {
                                save(var, Byte.valueOf(data));
                            } else if (Constant.DATA_TYPE.CHAR.equals(type)) {
                                save(var, data);
                            } else if (Constant.DATA_TYPE.INT.equals(type)) {
                                save(var, Integer.valueOf(data));
                            } else if (Constant.DATA_TYPE.SHORT.equals(type)) {
                                save(var, Integer.valueOf(data));
                            } else if (Constant.DATA_TYPE.LONG.equals(type)) {
                                save(var, Long.valueOf(data));
                            } else if (Constant.DATA_TYPE.FLOAT.equals(type)) {
                                save(var, Float.valueOf(data));
                            } else if (Constant.DATA_TYPE.DOUBLE.equals(type)) {
                                save(var, Float.valueOf(data));
                            } else if (Constant.DATA_TYPE.STRING.equals(type)) {
                                save(var, data);
                            } else {
//                                try {
//                                    save(Class.forName(type).newInstance());
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                } catch (InstantiationException e) {
//                                    e.printStackTrace();
//                                } catch (IllegalAccessException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从配置文件中读取boolean类型数据
     *
     * @param key 键名称
     * @return boolean类型数据，默认如果没有该key返回false
     */
    public static boolean getBoolean(@NonNull String key) {
        check();
        return !(null == sharedPreferences || TextUtil.isEmpty(key)) && sharedPreferences.getBoolean(key, false);
    }

    /**
     * 从配置文件中读取boolean类型数据
     *
     * @param key          键名称
     * @param defaultValue 默认值
     * @return boolean类型数据
     */
    public static boolean getBoolean(@NonNull String key, @NonNull boolean defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return defaultValue;
        }
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static int getInt(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return 0;
        }
        return sharedPreferences.getInt(key, 0);
    }

    public static int getInt(@NonNull String key, @NonNull int defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return defaultValue;
        }
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static short getShort(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return Short.valueOf("0");
        }
        return Short.valueOf(sharedPreferences.getString(key, "0"));
    }

    public static short getShort(@NonNull String key, @NonNull short defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return defaultValue;
        }
        return Short.valueOf(sharedPreferences.getString(key, String.valueOf(defaultValue)));
    }

    public static long getLong(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return 0L;
        }
        return sharedPreferences.getLong(key, 0L);
    }

    public static long getLong(@NonNull String key, @NonNull long defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return defaultValue;
        }
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static float getFloat(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return 0.0f;
        }
        return sharedPreferences.getFloat(key, 0.0f);
    }

    public static float getFloat(@NonNull String key, @NonNull float defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmpty(key)) {
            return defaultValue;
        }
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static double getDouble(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmptyAndNull(key)) {
            return 0.0;
        }
        return Double.valueOf(sharedPreferences.getString(key, "0.0"));
    }

    public static double getDouble(@NonNull String key, @NonNull double defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmptyAndNull(key)) {
            return Double.valueOf(String.valueOf(defaultValue));
        }
        return Double.valueOf(sharedPreferences.getString(key, String.valueOf(defaultValue)));
    }

    public static String getString(@NonNull String key) {
        check();
        if (null == sharedPreferences || TextUtil.isEmptyAndNull(key)) {
            return Constant.EMPTY_STR;
        }
        return sharedPreferences.getString(key, Constant.EMPTY_STR).trim();
    }

    public static String getString(@NonNull String key, @NonNull String defaultValue) {
        check();
        if (null == sharedPreferences || TextUtil.isEmptyAndNull(key)) {
            return defaultValue;
        }
        return sharedPreferences.getString(key, defaultValue).trim();
    }

    /**
     * 删除
     *
     * @param key key名称
     * @return 成功返回true, 失败返回false
     */
    public static boolean remove(@NonNull String key) {
        check();
        final Editor editor = sharedPreferences.edit();
        if (null == editor || TextUtil.isEmpty(key)) {
            return false;
        }
        return editor.remove(key).commit();
    }

    /**
     * 清空
     *
     * @return 成功返回true, 失败返回false
     */
    public static boolean clear() {
        check();
        final Editor editor = sharedPreferences.edit();
        return editor.clear().commit();
    }
}