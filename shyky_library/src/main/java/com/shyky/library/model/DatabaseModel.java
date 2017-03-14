package com.shyky.library.model;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.shyky.library.bean.response.base.BaseResponse;
import com.shyky.library.constant.Constant;
import com.shyky.library.exception.NotOverrideToStringMethodException;
import com.shyky.library.exception.NotSupportToStringFormatException;
import com.shyky.library.helper.DatabaseOpenHelper;
import com.shyky.library.model.impl.IDatabaseModel;
import com.shyky.util.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 数据库业务操作
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/3
 * @since 1.0
 */
public final class DatabaseModel implements IDatabaseModel {
    private static final int STATE_FAILURE = 11;
    private static final int STATE_SUCCESS = 22;
    private DatabaseResultCallback databaseResultCallback;
    private ResultCallback resultCallback;
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == STATE_SUCCESS) {
                    if (databaseResultCallback != null)
                        databaseResultCallback.onSuccess((BaseResponse) msg.obj);
                    if (resultCallback != null)
                        resultCallback.onSuccess((Boolean) msg.obj);
                } else if (msg.what == STATE_FAILURE) {
                    if (databaseResultCallback != null)
                        databaseResultCallback.onFailure(msg.obj.toString());
                }
            }
        }
    };

    public static final <T> boolean createTable(T... classes) {
        try {
            DatabaseOpenHelper.getInstance(classes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static final <T> boolean createTable(T clz) {
//        return createTable(clz);
//    }

    @Override
    public boolean execSQL(String sql) {
        try {
            d("sql", sql);
            DatabaseOpenHelper.getDatabase().execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean execSQL(String sql, Object[] bindArgs) {
        try {
            d("sql", sql);
            DatabaseOpenHelper.getDatabase().execSQL(sql, bindArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean execSQL(String sql, String... bindArgs) {
        try {
            d("sql", sql);
            DatabaseOpenHelper.getDatabase().execSQL(sql, bindArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T> boolean save(@NonNull T entity) {
        return execSQL(buildSQL(entity, ExecType.INSERT, null));
    }

    @Override
    public <T> boolean save(@NonNull List<T> entities) {
        boolean status = false;
        // 使用事务处理，把多条数据插入到表中，解决插入数据慢问题
        if (entities != null && !entities.isEmpty()) {
            DatabaseOpenHelper.getDatabase().beginTransaction(); // 手动设置开始事务
            for (T entity : entities)
                save(entity);
            DatabaseOpenHelper.getDatabase().setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
            DatabaseOpenHelper.getDatabase().endTransaction(); // 处理完成
            status = true;
        }
        return status;
    }

    @Override
    public <T> void save(@NonNull final List<T> entity, ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = save(entity);
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = STATE_SUCCESS;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public boolean delete(Class tableName, String wh) {
        if (TextUtil.isEmptyAndNull(wh)) {
            return execSQL("delete from " + class2String(tableName));
        } else {
            return execSQL("delete from " + class2String(tableName) + " where " + wh + " ;");
        }
    }

    @Override
    public boolean delete(@NonNull Class tableName) {
        return execSQL("delete from " + class2String(tableName));
    }

    @Override
    public <T> boolean replace(@NonNull T entity) {
        return execSQL(buildSQL(entity, ExecType.REPLACE, null));
    }

    @Override
    public <T> boolean replace(@NonNull List<T> entities) {
        boolean status = false;
        // 使用事务处理，把多条数据插入到表中，解决插入数据慢问题
        if (entities != null && !entities.isEmpty()) {
            DatabaseOpenHelper.getDatabase().beginTransaction(); // 手动设置开始事务
            for (T entity : entities)
                replace(entity);
            DatabaseOpenHelper.getDatabase().setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
            DatabaseOpenHelper.getDatabase().endTransaction(); // 处理完成
            status = true;
        }
        return status;
    }

    @Override
    public <T> void replace(@NonNull final List<T> entities, @NonNull ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = replace(entities);
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = STATE_SUCCESS;
                handler.sendMessage(msg);
            }
        }).start();
    }
//    @Override
//    public void delete(@NonNull final String tableName, final String wh, @NonNull OnSuccess onSuccess) {
//        this.onSuccess = onSuccess;
//        new Thread() {
//            @Override
//            public void run() {
//                boolean result = delete(tableName, wh);
//                Message msg = Message.obtain();
//                msg.obj = result;
//                msg.what = STATE_ONSUCCESS;
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }

    @Override
    public <T> boolean update(T entity, String wh) {
        return execSQL(buildSQL(entity, ExecType.UPDATE, wh));
    }


    @Override
    public <T> boolean update(@NonNull List<T> entities) {
        boolean status = false;
        // 使用事务处理，把多条数据插入到表中，解决插入数据慢问题
        if (entities != null && !entities.isEmpty()) {
            DatabaseOpenHelper.getDatabase().beginTransaction(); // 手动设置开始事务
            for (T entity : entities)
                update(entity, null);
            DatabaseOpenHelper.getDatabase().setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
            DatabaseOpenHelper.getDatabase().endTransaction(); // 处理完成
            status = true;
        }
        return status;
    }

//    @Override
//    public <T> void update(@NonNull final T entity, final String wh, @NonNull OnSuccess onSuccess) {
//        this.onSuccess = onSuccess;
//        new Thread() {
//            @Override
//            public void run() {
//                boolean result = update(entity, wh);
//                Message msg = Message.obtain();
//                msg.obj = result;
//                msg.what = STATE_ONSUCCESS;
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }

    @Override
    public <RESPONSE extends BaseResponse> RESPONSE find(Class tableName, String whereCase, Class<RESPONSE> clz) {
        return query(class2String(tableName), whereCase, null, clz);
    }

    @Override
    public <RESPONSE extends BaseResponse> void find(Class tableName, String whereCase, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback) {
        asyncQueryDataBase(class2String(tableName), whereCase, clz, databaseResultCallback);
    }

    @Override
    public int findCount(Class tableName, String whereCase) {
        String sql = "select count(*) from " + class2String(tableName);
        String selectionArgs = null;
        if (!TextUtil.isEmpty(whereCase)) {
            String[] sps = whereCase.split("=");
            sql += " where " + sps[0] + "=? ;";
            selectionArgs = sps[1];
        } else {
            sql += ";";
        }
        Cursor cursor = DatabaseOpenHelper.getDatabase().rawQuery(sql, selectionArgs == null ? null : new String[]{selectionArgs});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    /**
     * 异步查询数据库
     *
     * @param tableName
     * @param whereCase
     * @param clz
     * @param databaseResultCallback
     * @param <RESPONSE>
     */
    private <RESPONSE extends BaseResponse> void asyncQueryDataBase(final String tableName, final String whereCase, final Class<RESPONSE> clz, final DatabaseResultCallback<RESPONSE> databaseResultCallback) {
        this.databaseResultCallback = databaseResultCallback;
        //异步
        new Thread(new Runnable() {
            @Override
            public void run() {
                query(tableName, whereCase, null, clz);
            }
        }).start();
    }

    /**
     * 异步查询数据库
     *
     * @param tableName
     * @param whereCase
     * @param clz
     * @param databaseResultCallback
     * @param <RESPONSE>
     */
    private <RESPONSE extends BaseResponse> void asyncQueryDataBase(final String tableName, final String whereCase, final Class<RESPONSE> clz, final DatabaseResultCallback<RESPONSE> databaseResultCallback, final String orderBy) {
        this.databaseResultCallback = databaseResultCallback;
        //异步
        new Thread(new Runnable() {
            @Override
            public void run() {
                query(tableName, whereCase, orderBy, clz);
            }
        }).start();
    }

    @Override
    public <RESPONSE extends BaseResponse> RESPONSE findAll(Class tableName, Class<RESPONSE> clz) {
        return query(class2String(tableName), null, null, clz);
    }

    @Override
    public <RESPONSE extends BaseResponse> RESPONSE findAll(Class tableName, Class<RESPONSE> clz, String orderBy) {
        return query(class2String(tableName), null, orderBy, clz);
    }

    @Override
    public <RESPONSE extends BaseResponse> void findAll(Class tableName, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback) {
        asyncQueryDataBase(class2String(tableName), null, clz, databaseResultCallback);
    }

    @Override
    public <RESPONSE extends BaseResponse> void findAll(Class tableName, Class<RESPONSE> clz, DatabaseResultCallback<RESPONSE> databaseResultCallback, String orderBy) {
        asyncQueryDataBase(class2String(tableName), null, clz, databaseResultCallback, orderBy);
    }

    @Override
    public Cursor query(String sql, String... bindArgs) {
        return DatabaseOpenHelper.getDatabase().rawQuery(sql, bindArgs);
    }

    /**
     * 操作数据库类型枚举
     */
    private enum ExecType {
        INSERT, REPLACE, DELETE, UPDATE, QUERY
    }

    /**
     * 将实体类拆分
     *
     * @param entity   实体类
     * @param execType 操作数据库类型
     * @param wh       条件，如 id = 5
     * @throws NotSupportToStringFormatException
     */
    private <T> String buildSQL(T entity, ExecType execType, String wh) throws NotSupportToStringFormatException {
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
                        StringBuilder columns = new StringBuilder();
                        StringBuilder sql = new StringBuilder();
                        String tableName = sps[0].substring(0, sps[0].indexOf("{"));
                        switch (execType) {
                            case INSERT:
                                sql.append("insert into ").append(tableName).append("(#columns) values(");
                                break;
                            case UPDATE:
                                if (TextUtil.isEmptyAndNull(wh)) {
                                    sql.append("update ").append(tableName).append(" set ").append("#columns").append(";");
                                } else {
                                    sql.append("update ").append(tableName).append(" set ").append("#columns").append(" where ").append(wh).append(";");
                                }
                                break;
                        }
                        for (int j = 0; j < sps.length; j++) {
                            String str = sps[j].trim();
                            String var;
                            // 截取出变量名
                            if (j == 0)
                                var = str.substring(str.indexOf("{") + 1, str.lastIndexOf("(")).trim();
                            else
                                var = str.substring(0, str.lastIndexOf("(")).trim();
                            // 截取出数据类型
                            String type = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).trim();
                            // 截取出变量值
                            String data;
                            if (Constant.DATA_TYPE.STRING.equals(type) || Constant.DATA_TYPE.CHAR.equals(type))
                                data = str.substring(str.indexOf("\'") + 1, str.lastIndexOf("\'")).trim();
                            else {
                                // 最后一个
                                if (j == sps.length - 1)
                                    data = str.substring(str.lastIndexOf("=") + 1, str.lastIndexOf("}")).trim();
                                else
                                    data = str.substring(str.lastIndexOf("=") + 1).trim();
                            }
                            if (execType.equals(ExecType.INSERT)) {
                                if (j == sps.length - 1)
                                    columns.append(var);
                                else
                                    columns.append(var).append(",");

                                if (Constant.DATA_TYPE.CHAR.equals(type) || Constant.DATA_TYPE.STRING.equals(type)) {
                                    if (j == sps.length - 1)
                                        sql.append("'").append(data).append("'");
                                    else
                                        sql.append("'").append(data).append("'").append(",");
                                } else {
                                    if (j == sps.length - 1)
                                        sql.append(data);
                                    else
                                        sql.append(data).append(",");
                                }
                            } else if (execType.equals(ExecType.UPDATE)) {
                                if (!"'null'".equals(data) && !TextUtil.isEmptyAndNull(data)) {
                                    if (Constant.DATA_TYPE.CHAR.equals(type) || Constant.DATA_TYPE.STRING.equals(type)) {
                                        if (j == sps.length - 1)
                                            columns.append(var).append("=").append("'").append(data).append("'");
                                        else
                                            columns.append(var).append("=").append("'").append(data).append("'").append(",");
                                    } else {
                                        if (j == sps.length - 1)
                                            columns.append(var).append("=").append(data);
                                        else
                                            columns.append(var).append("=").append(data).append(",");
                                    }
                                }
                            }
                        }
                        if (execType.equals(ExecType.INSERT))
                            sql.append(");");

                        return sql.replace(sql.indexOf("#"), sql.indexOf("#") + "#columns".length(), columns.toString()).toString();
                    }
                }
            }
        }
        return null;
    }

    private <RESPONSE> RESPONSE query(final String tableName, final String whereCase, final String orderBy, final Class<RESPONSE> clz) {
        StringBuilder sql = new StringBuilder();
        Cursor cursor;
        if (TextUtil.isEmpty(whereCase)) {
            sql = sql.append("select * from ").append(tableName);
            if (!TextUtil.isEmpty(orderBy)) {
                sql = sql.append(" order by ").append(orderBy);
            }
            cursor = query(sql.toString(), new String[]{});
        } else {
            String[] where;
            if (!whereCase.contains("=")) {
                sql = sql.append("select ").append(whereCase).append(" from ").append(tableName);
                if (!TextUtil.isEmpty(orderBy)) {
                    sql = sql.append(" order by ").append(orderBy);
                }
                cursor = query(sql.toString());
            } else {
                where = whereCase.split("=");
                sql = sql.append("select * from ").append(tableName).append(" where ").append(where[0]).append("=?");
                if (!TextUtil.isEmpty(orderBy)) {
                    sql = sql.append(" order by ").append(orderBy);
                }
                cursor = query(sql.toString(), new String[]{where[1]});
            }
        }
        int count = cursor.getColumnCount();
        JSONArray jsonArray = new JSONArray();
        while (cursor.moveToNext()) {
            final JSONObject jsonObject = new JSONObject();
            for (int j = TextUtil.isEmpty(whereCase) || whereCase.contains("=") ? 1 : 0; j < count; j++) {
                final String columnName = cursor.getColumnName(j);
                Object value = null;
                try {
                    value = cursor.getString(j);
                } catch (Exception stringe) {
                    try {
                        value = cursor.getInt(j);
                    } catch (Exception inte) {
                        try {
                            value = cursor.getFloat(j);
                        } catch (Exception floate) {
                            try {
                                value = cursor.getDouble(j);
                            } catch (Exception doublee) {
                                try {
                                    value = cursor.getLong(j);
                                } catch (Exception longe) {

                                }
                            }
                        }
                    }
                }

                if (value != null) {
                    try {
                        jsonObject.put(columnName, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            jsonArray.put(jsonObject);
        }
        RESPONSE response = null;
        try {
            response = clz.newInstance();
            if (response != null && response instanceof BaseResponse) {
                ((BaseResponse) response).convert(jsonArray);
            }
            if (handler != null) {
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = STATE_SUCCESS;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null) {
                Message msg = Message.obtain();
                msg.obj = e.getMessage();
                msg.what = STATE_FAILURE;
                handler.sendMessage(msg);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return response;
    }

    /**
     * 表名字节码转表名字符串
     *
     * @param clz 表名字节码
     * @return 表名字符串
     */
    private String class2String(Class clz) {
        return clz.getName().substring(clz.getName().lastIndexOf(".") + 1, clz.getName().length());
    }
}