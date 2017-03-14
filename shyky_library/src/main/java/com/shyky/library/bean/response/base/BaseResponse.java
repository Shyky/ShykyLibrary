package com.shyky.library.bean.response.base;

import com.shyky.library.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 基础的返回数据对应的实体类，子类需要继承此类进行手动解析如json、xml等数据
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/4/28
 * @since 1.0
 */
public abstract class BaseResponse {
    private String response;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    /**
     * 将返回的数据转换成BaseResponse2的子类，这个方法不需要再做数据格式非空和非法判断，在HttpModel中已经处理了
     */
    public void convert() {

    }

    /**
     * 将返回的数据转换成BaseResponse2的子类，这个方法不需要再做数据格式非空和非法判断，在HttpModel中已经处理了
     *
     * @param response 返回的字符串数据
     */
    final public void convert(String response) {
        if (JsonUtil.isJsonObject(response))
            convert(JsonUtil.toJsonObject(response));
        else if (JsonUtil.isJsonArray(response))
            convert(JsonUtil.toJsonArray(response));
    }

    /**
     * 将返回的数据转换成BaseResponse2的子类，这个方法不需要再做数据格式非空和非法判断，在HttpModel中已经处理了
     *
     * @param response 返回的JSONObject数据
     */
    public abstract void convert(JSONObject response);

    /**
     * 将返回的数据转换成BaseResponse2的子类，这个方法不需要再做数据格式非空和非法判断，在HttpModel中已经处理了
     *
     * @param response 返回的JSONArray数据
     */
    public void convert(JSONArray response) {

    }
}