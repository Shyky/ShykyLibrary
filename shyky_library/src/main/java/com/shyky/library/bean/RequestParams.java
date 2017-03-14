package com.shyky.library.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shyky on 16-4-8.
 */
public final class RequestParams {
    private static final int TIME_UNIT = 1000;
    /**
     * 超时时间，默认为10秒
     */
    private int timeOutSecond;
    /**
     * 请求参数字符编码
     */
    private String charset;
    protected final ConcurrentHashMap<String, Object> urlParamsWithObjects = new ConcurrentHashMap<>();

    public RequestParams() {
        // 默认为10秒
        timeOutSecond = 10 * TIME_UNIT;
    }

    public RequestParams(String charset) {
        // 默认为10秒
        timeOutSecond = 10 * TIME_UNIT;
    }

    public void put(String key, String value) {

    }

    public void put(String key, int value) {

    }

    public void put(String key, long value) {

    }

    public void put(String key, short value) {

    }

    public void put(String key, byte value) {

    }

    public void put(String key, boolean value) {

    }

    public void put(String key, float value) {

    }

    public void put(String key, double value) {

    }

    public void put(String key, char value) {

    }

    public void put(String key, JSONObject value) {

    }

    public void put(String key, JSONArray value) {

    }

    public <T> void put(String key, T value) {

    }


    /**
     * Adds string value to param which can have more than one value.
     *
     * @param key   the key name for the param, either existing or new.
     * @param value the value string for the new param.
     */
    public void add(String key, String value) {
        if (key != null && value != null) {
            Object params = urlParamsWithObjects.get(key);
            if (params == null) {
                // Backward compatible, which will result in "k=v1&k=v2&k=v3"
                params = new HashSet<String>();
                this.put(key, params);
            }
            if (params instanceof List) {
                ((List<Object>) params).add(value);
            } else if (params instanceof Set) {
                ((Set<Object>) params).add(value);
            }
        }
    }
}