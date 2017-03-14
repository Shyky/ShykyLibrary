package com.shyky.library.util;

import android.support.annotation.NonNull;

import com.shyky.library.constant.Constant;
import com.shyky.library.exception.NotOverrideToStringMethodException;
import com.shyky.library.exception.NotSupportToStringFormatException;
import com.shyky.util.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.socks.library.KLog.d;

/**
 * JSON相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/4/13
 * @since 1.0
 */
public final class JsonUtil {
    /**
     * 构造方法私有化
     */
    private JsonUtil() {

    }

    public static JSONObject toJsonObject(String json) {
        if (isJsonObject(json)) {
            try {
                Object object = new JSONTokener(json).nextValue();
                return (JSONObject) object;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static JSONArray toJsonArray(String json) {
        if (isJsonArray(json)) {
            try {
                Object object = new JSONTokener(json).nextValue();
                return (JSONArray) object;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private static Map<Class<?>, ArrayList<fieldEntity>> method_map = new HashMap<>();

    /**
     * json字符串转换为bean
     *
     * @param clazz
     * @param json
     * @return T
     */
    public static <T> T json2Entity(Class<?> clazz, String json) {
        getMethod(clazz);
        T object = null;
        try {
            object = (T) json2Object(json, clazz.newInstance());
        } catch (Exception e) {
        }
        return object;
    }

    public static <T> T JsonToCollection(String str) {
        T object = null;
        try {
            object = (T) JsonToHashMap(str);
        } catch (Exception e) {
        }
        return object;
    }

    /**
     * json字符串转化为集合
     *
     * @param str
     * @return Object
     */
    private static Object JsonToHashMap(String str) {
        LinkedHashMap<String, Object> json = new LinkedHashMap<>();
        try {
            Object object = new JSONTokener(str).nextValue();
            if (object instanceof JSONArray) {
                JSONArray root = new JSONArray(str);
                ArrayList<Object> list = new ArrayList<>();
                if (root.length() > 0) {
                    for (int i = 0; i < root.length(); i++) {
                        list.add(JsonToCollection(root.getString(i)));
                    }
                    return list;
                }
                return list.add(str);
            } else if (object instanceof JSONObject) {
                JSONObject root = new JSONObject(str);
                if (root.length() > 0) {
                    @SuppressWarnings("unchecked")
                    Iterator<String> rootName = root.keys();
                    String name;
                    while (rootName.hasNext()) {
                        name = rootName.next();
                        json.put(name, JsonToCollection(root.getString(name)));
                    }
                }
                return json;
            } else {
                return str;
            }
        } catch (JSONException e) {
            System.out.println("错误字符串：" + str);
            return str;
        }
    }

    private static Object handle(Class<?> clz) {
        Object object = null;
        Field[] fields = clz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                String name = field.getName();
                Class<?> type = field.getType();
            }
        }
        return object;
    }

    private static void invoke(Object object, JSONObject jsonObject, String name, Class<?> type, Method method) {
        try {
            if (type == byte.class)
                method.invoke(object, Byte.parseByte(jsonObject.get(name).toString()));
            else if (type == int.class)
                method.invoke(object, jsonObject.getInt(name));
            else if (type == short.class)
                method.invoke(object, Short.parseShort(jsonObject.get(name).toString()));
            else if (type == long.class)
                method.invoke(object, jsonObject.getLong(name));
            else if (type == float.class)
                method.invoke(object, Float.parseFloat(jsonObject.get(name).toString()));
            else if (type == double.class)
                method.invoke(object, jsonObject.getDouble(name));
            else if (type == boolean.class)
                method.invoke(object, jsonObject.getBoolean(name));
            else if (type == String.class)
                method.invoke(object, jsonObject.getString(name));
            else if (type == List.class || type == ArrayList.class) {
//                Class<?> clz = field.getGenericType().getClass();
//                Object obj = json2Obj(jsonObject.getJSONArray(name).toString(), clz);
//                method.invoke(object, obj);
            } else {
//                Class<?> clz = field.getGenericType().getClass();
//                Object obj = json2Obj(jsonObject.getJSONObject(name).toString(), clz);
//                method.invoke(object, obj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void setValue(Object object, JSONObject jsonObject, String name, Class<?> type, Field field) {
        try {
            field.setAccessible(true);
            if (type == byte.class)
                field.setByte(object, Byte.parseByte(jsonObject.get(name).toString()));
            else if (type == int.class)
                field.setInt(object, jsonObject.getInt(name));
//                field.setInt(object, Integer.parseInt(jsonObject.get(name).toString()));
            else if (type == short.class)
                field.setShort(object, Short.parseShort(jsonObject.get(name).toString()));
            else if (type == long.class)
                field.setLong(object, jsonObject.getLong(name));
            else if (type == float.class)
                field.setFloat(object, Float.parseFloat(jsonObject.get(name).toString()));
            else if (type == double.class)
                field.setDouble(object, jsonObject.getDouble(name));
            else if (type == boolean.class)
                field.setBoolean(object, jsonObject.getBoolean(name));
            else if (type == String.class)
                field.set(object, jsonObject.getString(name));
            else if (type == List.class || type == ArrayList.class) {
                String genericType = field.getGenericType().toString();
                String className = genericType.substring(genericType.indexOf('<') + 1, genericType.lastIndexOf('>'));
                d("className", className);
                Object obj = json2List(jsonObject.getJSONArray(name).toString(), Class.forName(className));
                field.set(object, obj);
            } else {
                Class<?> clz = field.getGenericType().getClass();
                Object obj = json2Obj(jsonObject.getJSONObject(name).toString(), clz);
                field.set(object, obj);
            }
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断字符串是否时JSON串
     *
     * @param str 要检测的字符串
     * @return 是返回true, 否则返回false
     */
    public static boolean isJson(String str) {
        return !TextUtil.isEmptyAndNull(str) && (isJsonObject(str) || isJsonArray(str));
    }

    /**
     * 判断json串是否是json对象
     *
     * @param json json字符串
     * @return 是返回true, 否则返回false
     */
    public static boolean isJsonObject(String json) {
        if (TextUtil.isEmptyAndNull(json))
            return false;
        try {
            Object object = new JSONTokener(json).nextValue();
            if (object instanceof JSONObject)
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断json串是否是json数组
     *
     * @param json json字符串
     * @return 是返回true, 否则返回false
     */
    public static boolean isJsonArray(String json) {
        if (TextUtil.isEmptyAndNull(json))
            return false;
        try {
            Object object = new JSONTokener(json).nextValue();
            if (object instanceof JSONArray)
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> List<T> json2List(String json, Class<?> clz) {
        List<T> list = null;
        if (!TextUtil.isEmpty(json) && isJsonArray(json)) {
            try {
                list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(json);
                int len = jsonArray.length();
                for (int j = 0; j < len; j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    T object = json2Obj(jsonObject.toString(), clz);
                    list.add(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            d("final list size = " + list.size() + ", list = " + list);
        }
        return list;
    }

    public static <T> T json2Obj(String json, Class<?> clz) {
        T obj = null;
        if (!TextUtil.isEmpty(json) && isJsonObject(json)) {
            try {
                Object object = new JSONTokener(json).nextValue();
                d("new JSONTokener --> nextValue , object = " + object);
                if (object != null) {
                    d("class name = " + clz.getName());
                    obj = (T) clz.newInstance();
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json);
                        Iterator<String> iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String name = iterator.next();
                            d("jsonObject = " + jsonObject + " , key name = " + name);
//                        String firstWord = new String(name.charAt(0) + "");
//                        String methodName = "set" + firstWord.toUpperCase(Locale.CHINA) + name.substring(1, name.length());
//                        LogUtil.d(TAG, "method name = " + methodName);

//                        Field field;///clz.getField(name);
//                        if (field == null)
                            Field field = clz.getDeclaredField(name);
                            // 过滤实体类中没有的字段，避免错误
                            if (field == null)
                                continue;
                            Class<?> type = null;
                            if (field != null) {
                                type = field.getType();
                                d("field = " + field + " , type = " + type + " , genericType = " + field.getGenericType());
                            }
                            if (type != null) {
//                            Method method=clz.getDeclaredMethod(methodName);
//                            if(method!=null){
//                                invoke(object,jsonObject,name,type,method);
//                            }
                                setValue(obj, jsonObject, name, type, field);
                            }
                        }
                    } else if (object instanceof JSONArray) {
                        json2List(object.toString(), clz);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            d("final obj = " + obj);
        }
        return obj;
    }

    /**
     * json转为对象
     *
     * @param str    json数据字符串
     * @param entity 实体类对象
     * @return Object 返回entity实体类对象或者改对象的集合
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object json2Object(String str, Object entity) {
        try {
            Object object = new JSONTokener(str).nextValue();
            if (object instanceof JSONArray) {
                JSONArray root = new JSONArray(str);
                ArrayList<Object> list = new ArrayList<>();
                if (root.length() > 0) {
                    for (int i = 0; i < root.length(); i++) {
                        Object value = new JSONTokener(root.optString(i)).nextValue();
                        if (classes.contains(value.getClass())) {
                            list.add(root.opt(i));
                        } else {
                            list.add(json2Object(root.optString(i), entity.getClass().newInstance()));
                        }
                    }
                }
                if (list.size() <= 0) {
                    System.out.println("数组" + entity + "解析出错");
                }
                return list;
            } else if (object instanceof JSONObject) {
                JSONObject root = new JSONObject(str);
                if (root.length() > 0) {
                    Iterator<String> rootName = root.keys();
                    String name;
                    while (rootName.hasNext()) {
                        name = rootName.next();
                        boolean isHas = false;
                        Class template = entity.getClass();
                        while (template != null && !classes.contains(template)) {
                            ArrayList<fieldEntity> arrayList = method_map.get(template);
                            for (fieldEntity fieldEntity : arrayList) {
                                fieldEntity.field.setAccessible(true);
                                Object obj = null;
                                final String fieldName = fieldEntity.field.getName();
                                final String convertName = convertNameToField(name);
                                if (name.equals(fieldName) || convertName.equalsIgnoreCase(fieldName)) {
                                    isHas = true;
                                    if (fieldEntity.clazz == null || fieldEntity.clazz.getSuperclass() == JsonUtil.class) {
                                        Class clazz = fieldEntity.field.getType();
                                        obj = getObjectValue(clazz, root.opt(name));
                                    } else {
                                        Class value_class = fieldEntity.field.getType();
                                        String obj2Str = root.optString(name);
                                        if (obj2Str != null) {
                                            Object obj2 = new JSONTokener(obj2Str).nextValue();
                                            if (classes.contains(value_class)) {
                                                JSONArray array = (JSONArray) obj2;
                                                ArrayList<Object> list = new ArrayList<>();
                                                for (int i = 0; i < array.length(); i++) {
                                                    list.add(getObjectValue(fieldEntity.clazz, array.opt(i)));
                                                }
                                                obj = list;
                                            } else {
                                                try {
                                                    obj = json2Object(root.optString(name), fieldEntity.clazz.newInstance());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                    try {
                                        fieldEntity.field.set(entity, obj);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            template = template.getSuperclass();
                        }

                        if (!isHas) {
                            System.out.println("字段" + name + "在实体类" + entity + "不存在");
                        }
                    }
                } else {
                    System.out.println("数据长度不对 解析出错");
                }
                return entity;
            } else {
                return entity;
            }
        } catch (Exception e) {
            System.out.println("错误字符串：" + str);
            return entity;
        }
    }

    static Object getObjectValue(Class clazz, Object value) throws IllegalAccessException, InstantiationException {
        if (clazz == String.class) {
            return toString(value);
        } else if (clazz == int.class) {
            return toInteger(value);
        } else if (clazz == boolean.class) {
            return toBoolean(value);
        } else if (clazz == double.class) {
            return toDouble(value);
        } else if (clazz == float.class) {
            return toFloat(value);
        } else if (clazz == long.class) {
            return toLong(value);
        } else if (clazz == HashMap.class) {
            return JsonToHashMap(value.toString());
        } else if (clazz.getSuperclass() == HandlerJsonBean.class) {
            final HandlerJsonBean handlerJsonListener = (HandlerJsonBean) clazz.newInstance();
            return handlerJsonListener.perHandlerJson(value.toString());
//            return new Range((String) value);
        }
        return null;
    }

    public abstract class HandlerJsonBean {
        public abstract HandlerJsonBean perHandlerJson(String json);
    }

    /**
     * 将字符"aaa_bbb_ccc"转成"aaaBbbCcc"的形式
     *
     * @param name
     * @return
     */
    static String convertNameToField(String name) {
        final int len = name.length();
        final StringBuffer sb = new StringBuffer();
        String temp = "";
        char ch;
        for (int i = 0; i < len; i++) {
            ch = name.charAt(i);
            if (ch == '_') {
                ++i;
                temp = String.valueOf(name.charAt(i)).toUpperCase();
                ch = temp.charAt(0);
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    static HashSet<Class> classes = new HashSet<Class>() {
        {
            add(Object.class);
            add(Double.class);
            add(Float.class);
            add(Integer.class);
            add(Long.class);
            add(String.class);
            add(Boolean.class);
            add(int.class);
            add(boolean.class);
            add(double.class);
            add(long.class);
            add(float.class);
            add(HashMap.class);
        }
    };

    private static void getMethod(Class<?> clazz) {

        Class<?> template = clazz;
        while (template != null && template != Object.class) {
            if (method_map.get(template) != null && method_map.get(template).size() > 0) {
                return;
            }
            template = template.getSuperclass();
        }
        template = clazz;
        while (template != null && !classes.contains(template)) {
            // -----------------------------------解析变量------------------------------------------------
            ArrayList<fieldEntity> entities = new ArrayList<>();
            for (Field m : template.getDeclaredFields()) {
                Type type = m.getGenericType();
                int modifiers = m.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (type instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                    for (Type type2 : types) {
                        if (!classes.contains(m.getType())) {
                            getMethod((Class<?>) type2);
                            entities.add(new fieldEntity(m, (Class<?>) type2));
                        } else {
                            entities.add(new fieldEntity(m, null));
                        }
                        break;
                    }
                    continue;
                }
                if (!classes.contains(m.getType())) {
                    getMethod((Class<?>) type);
                    entities.add(new fieldEntity(m, (Class<?>) type));
                } else {
                    entities.add(new fieldEntity(m, null));
                }
            }
            method_map.put(template, entities);
            // -----------------------------------解析完毕------------------------------------------------
            template = template.getSuperclass();
        }
    }

    /**
     * 解析内部类
     */
    public static class fieldEntity {
        public Field field;
        public Class<?> clazz;

        public fieldEntity(Field field, Class<?> clazz) {
            this.field = field;
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return "fieldEntity [field=" + field.getName() + ", clazz=" + clazz + "]";
        }

    }


    /**
     * 将Object对象转成boolean类型
     *
     * @param value
     * @return 如果value不能转成boolean，则默认false
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, false);
    }

    /**
     * 将Object对象转成boolean类型
     *
     * @param value
     * @return 如果value不能转成boolean，则默认defaultValue
     */
    public static Boolean toBoolean(Object value, boolean defaultValue) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return defaultValue;
    }

    /**
     * 将Object对象转成Double类型
     *
     * @param value
     * @return 如果value不能转成Double，则默认0.00
     */
    public static Double toDouble(Object value) {
        return toDouble(value, 0.00);
    }

    /**
     * 将Object对象转成Double类型
     *
     * @param value
     * @return 如果value不能转成Double，则默认defaultValue
     */
    public static Double toDouble(Object value, double defaultValue) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * 将Object对象转成Double类型
     *
     * @param value
     * @return 如果value不能转成Float，则默认0.00
     */
    public static Float toFloat(Object value) {
        return toFloat(value, 0.00f);
    }

    /**
     * 将Object对象转成Double类型
     *
     * @param value
     * @return 如果value不能转成Float，则默认defaultValue
     */
    public static Float toFloat(Object value, float defaultValue) {
        if (value instanceof Double) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            try {
                return Float.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * 将Object对象转成Integer类型
     *
     * @param value
     * @return 如果value不能转成Integer，则默认0
     */
    public static Integer toInteger(Object value) {
        return toInteger(value, 0);
    }

    /**
     * 将Object对象转成Integer类型
     *
     * @param value
     * @return 如果value不能转成Integer，则默认defaultValue
     */
    public static Integer toInteger(Object value, int defaultValue) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * 将Object对象转成Long类型
     *
     * @param value
     * @return 如果value不能转成Long，则默认0
     */
    public static Long toLong(Object value) {
        return toLong(value, 0L);
    }

    /**
     * 将Object对象转成Long类型
     *
     * @param value
     * @return 如果value不能转成Long，则默认defaultValue
     */
    public static Long toLong(Object value, long defaultValue) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * 将Object对象转成String类型
     *
     * @param value
     * @return 如果value不能转成String，则默认""
     */
    public static String toString(Object value) {
        return toString(value, "");
    }

    /**
     * 将Object对象转成String类型
     *
     * @param value
     * @return 如果value不能转成String，则默认defaultValue
     */
    public static String toString(Object value, String defaultValue) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return defaultValue;
    }

    /**
     * 将实体类转换成json字符串
     *
     * @param entity   要转换的实体类
     * @param <ENTITY> 泛型参数
     * @return 成功返回json字符串，失败返回null
     */
    public static <ENTITY> String toJsonString(@NonNull ENTITY entity) {
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
                    StringBuilder json;
                    // 进行分割字符串处理
                    String[] sps = toStr.split(",");
                    if (null == sps || (null != sps && sps.length == 0)) {
                        throw new NotSupportToStringFormatException();
                    } else {
                        json = new StringBuilder("{");
                        for (int j = 0; j < sps.length; j++) {
                            String str = sps[j].trim();
                            d("str = " + str);
                            //  String var = (tag == null ? Constant.EMPTY_STR : tag);
                            String var;
                            final int last = str.lastIndexOf("(");
                            d("toString -- >" + toStr);
                            d("last = " + last);
                            if (last == -1)
                                throw new NotSupportToStringFormatException();

                            // 截取出变量名
                            if (j == 0) {
                                final int start = str.indexOf("{");
                                d("start = " + start);
                                if (start == -1)
                                    throw new NotSupportToStringFormatException();
                                var = str.substring(start + 1, last).trim();
                            } else
                                var = str.substring(0, last).trim();
                            d("var = " + var);
                            // 截取出数据类型
                            String type = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).trim();
                            d("type = " + type);
                            // 截取出变量值
                            String data;
                            if (Constant.DATA_TYPE.STRING.equals(type) || Constant.DATA_TYPE.CHAR.equals(type)) {
                                int start = str.indexOf("\'");
                                if (start == -1)
                                    start = str.indexOf("\"");
                                int end = str.lastIndexOf("\'");
                                if (end == -1)
                                    end = str.lastIndexOf("\"");
                                data = str.substring(start + 1, end).trim();
                            } else {
                                // 最后一个
                                if (j == sps.length - 1)
                                    data = str.substring(str.lastIndexOf("=") + 1, str.lastIndexOf("}")).trim();
                                else
                                    data = str.substring(str.lastIndexOf("=") + 1).trim();
                            }
                            d("data = " + data);
                            json.append("\"").append(var).append("\"").append(":");
                            if (Constant.DATA_TYPE.BOOLEAN.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.BYTE.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.CHAR.equals(type)) {
                                json.append("\"").append(data).append("\"");
                            } else if (Constant.DATA_TYPE.INT.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.SHORT.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.LONG.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.FLOAT.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.DOUBLE.equals(type)) {
                                json.append(data);
                            } else if (Constant.DATA_TYPE.STRING.equals(type)) {
                                json.append("\"").append(data).append("\"");
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
                            // 最后一个
                            if (j == sps.length - 1)
                                break;
                            json.append(",");
                        }
                        json.append("}");
                    }
                    final String jsonStr = json.toString();
                    d("final json string = " + jsonStr);
                    return jsonStr;
                }
            }
        }
        return null;
    }
}