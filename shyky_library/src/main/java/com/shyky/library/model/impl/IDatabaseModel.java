package com.shyky.library.model.impl;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.shyky.library.bean.response.base.BaseResponse;

import java.util.List;

/**
 * 数据库业务接口
 *
 * @author Shyky
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/3
 * @since 1.0
 */
public interface IDatabaseModel {
    interface DatabaseResultCallback<RESPONSE extends BaseResponse> {
        void onSuccess(@NonNull RESPONSE result);

        void onFailure(@NonNull String message);
    }

    interface ResultCallback {
        void onSuccess(@NonNull boolean result);
    }

    /**
     * 执行SQL语句
     *
     * @param sql SQL语句
     * @return 成功返回true，失败返回false
     */
    boolean execSQL(@NonNull String sql);

    boolean execSQL(@NonNull String sql, @NonNull Object[] bindArgs);

    boolean execSQL(@NonNull String sql, @NonNull String... bindArgs);

    /**
     * 插入数据库
     *
     * @param entity 插入的实体类
     * @return 是否执行成功
     */
    <T> boolean save(@NonNull T entity);

    /**
     * 批量插入数据到数据库
     *
     * @param entities 插入的实体集合
     * @param <T>      泛型参数
     * @return 成功返回true，失败返回false
     */
    <T> boolean save(@NonNull List<T> entities);

    /**
     * 异步插入批量数据
     *
     * @param entities       插入的实体集合
     * @param resultCallback 回调
     */
    <T> void save(@NonNull List<T> entities, @NonNull ResultCallback resultCallback);

    /**
     * 删除表中指定数据
     *
     * @param tableName 表名字节码
     * @param wh        条件 如 id = 5，为空表示删除全部
     * @return 是否执行成功
     */
    boolean delete(@NonNull Class tableName, String wh);

    /**
     * 清空表中全部数据
     *
     * @param tableName 表名字节码
     * @return 是否执行成功
     */
    boolean delete(@NonNull Class tableName);

    /**
     * 带条件更新表中的数据
     *
     * @param entity 要更新的实体类
     * @param wh     条件，可以为空
     * @return 是否执行成功
     */
    <T> boolean update(@NonNull T entity, String wh);

    /**
     * 更新表中多条数据
     *
     * @param entities 要更新的实体集合
     * @return 是否更新成功
     */
    <T> boolean update(@NonNull List<T> entities);

    /**
     * 插入或更新数据库
     *
     * @param entity 插入或更新的实体类
     * @return 是否执行成功
     */
    <T> boolean replace(@NonNull T entity);

    /**
     * 批量插入或更新数据到数据库
     *
     * @param entities 插入或更新的实体集合
     * @param <T>      泛型参数
     * @return 成功返回true，失败返回false
     */
    <T> boolean replace(@NonNull List<T> entities);

    /**
     * 异步插入或更新批量数据
     *
     * @param entities       插入或更新的实体集合
     * @param resultCallback 回调
     */
    <T> void replace(@NonNull List<T> entities, @NonNull ResultCallback resultCallback);

    /**
     * 同步带条件查询表中的数据
     *
     * @param tableName 表名字节码
     * @param whereCase 条件
     * @param clz       http请求返回的实体字节码
     */
    <RESPONSE extends BaseResponse> RESPONSE find(Class tableName, String whereCase, Class<RESPONSE> clz);

    /**
     * 异步带条件查询表中的数据
     *
     * @param tableName              表名字节码
     * @param whereCase              条件
     * @param clz                    http请求返回的实体字节码
     * @param databaseResultCallback 回调
     */
    <RESPONSE extends BaseResponse> void find(Class tableName, String whereCase, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback);

    /**
     * 带条件查询数据库数量，条件可以为空
     *
     * @param tableName 表名字节码
     * @param whereCase 条件
     * @return 数据库中的数量
     */
    int findCount(Class tableName, String whereCase);

    /**
     * 同步查询表中所有数据
     *
     * @param tableName 表名字节码
     * @param clz       实体字节码
     * @return 实体数据
     */
    <RESPONSE extends BaseResponse> RESPONSE findAll(Class tableName, Class<RESPONSE> clz);

    /**
     * 同步查询表中所有数据并按条件排序
     *
     * @param tableName 表名字节码
     * @param clz       实体字节码
     * @param orderBy   排序条件
     * @return 实体数据
     */
    <RESPONSE extends BaseResponse> RESPONSE findAll(Class tableName, Class<RESPONSE> clz, String orderBy);

    /**
     * 异步查询表中所有数据并按条件排序
     *
     * @param tableName              表名字节码
     * @param clz                    http请求返回的实体字节码
     * @param orderBy                排序条件
     * @param databaseResultCallback 回调
     */
    <RESPONSE extends BaseResponse> void findAll(Class tableName, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback, String orderBy);

    /**
     * 异步查询表中所有数据
     *
     * @param tableName              表名字节码
     * @param clz                    http请求返回的实体字节码
     * @param databaseResultCallback 回调
     */
    <RESPONSE extends BaseResponse> void findAll(Class tableName, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback);

    Cursor query(String sql, String... bindArgs);
}