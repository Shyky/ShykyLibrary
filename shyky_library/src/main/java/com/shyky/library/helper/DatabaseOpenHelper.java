package com.shyky.library.helper;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shyky.library.BaseApplication;
import com.shyky.library.constant.Config;
import com.shyky.library.constant.Constant;
import com.shyky.library.exception.NotOverrideToStringMethodException;
import com.shyky.library.exception.NotSupportToStringFormatException;
import com.shyky.util.TextUtil;

import java.util.ArrayList;

import static com.socks.library.KLog.d;

/**
 * SQLite数据库帮助类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/3
 * @since 1.0
 */
public final class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseOpenHelper";
    private static DatabaseOpenHelper instance;
    private static SQLiteDatabase database;
    /**
     * 建表的SQL语句集合
     */
    private ArrayList<StringBuilder> sqlList;

    /**
     * 构造方法
     *
     * @param clz 字节码
     */
    private <T> DatabaseOpenHelper(T... clz) {
        super(BaseApplication.getContext(), Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        try {
            sqlList = new ArrayList<>();
            buildSql(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拼接建表sql语句
     *
     * @param clz 字节码集合
     */
    private <T> void buildSql(T... clz) throws Exception {
        for (int x = 0; x < clz.length; x++) {
            //如果是字节码
            if (clz[x] instanceof Class) {
                Class clazz = (Class) clz[x];
                Object object = clazz.newInstance();
                // 实体没有重写toString方法,默认调用Object类的toString方法
                String suffix = '@' + Integer.toHexString(object.hashCode());
                String toStr = object.toString();
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
                        //循环结束后把拼接的sql建表语句加到集合中。
                        sqlList.add(buildSQL(toStr));
                    }
                }
            } else if (clz[x] instanceof String) {
                //如果是字符串
                StringBuilder sqlOfString = new StringBuilder();
                String tableName = (String) clz[x];
                sqlOfString.append("CREATE TABLE ").append(tableName).append("( _id INTEGER PRIMARY KEY AUTOINCREMENT ,id INTEGER UNIQUE, content TEXT , time TEXT );");
                sqlList.add(sqlOfString);
                d(TAG, "SQL = " + sqlOfString.toString());
            }
        }
    }

    /**
     * 根据toString方法拼接SQL语句
     *
     * @param toString toString的字符串
     * @return 拼接好的SQL语句
     */
    private StringBuilder buildSQL(String toString) {
        // 进行分割字符串处理
        String[] sps = toString.split(",");
        if (null == sps || (null != sps && sps.length == 0)) {
            throw new NotSupportToStringFormatException();
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE ").append(sps[0].substring(0, sps[0].indexOf("{"))).append(" (").append("_id INTEGER PRIMARY KEY AUTOINCREMENT ,");
            for (int j = 0; j < sps.length; j++) {
                String str = sps[j].trim();
                String var = "";
                // 截取出列名
                if (j == 0)
                    var = str.substring(str.indexOf("{") + 1, str.lastIndexOf("(")).trim();
                else
                    var = str.substring(0, str.lastIndexOf("(")).trim();
                if (!"_id".equals(var)) {
                    // 截取出数据类型
                    String type = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).trim();
                    sql.append(var);
                    //定义类型
                    if (type.contains(Constant.DATA_TYPE.STRING) || type.contains(Constant.DATA_TYPE.CHAR))
                        sql.append(" TEXT");
                    else if (type.contains(Constant.DATA_TYPE.INT))
                        sql.append(" INTEGER");
                    else if (type.contains(Constant.DATA_TYPE.DOUBLE))
                        sql.append(" DOUBLE");
                    else if (type.contains(Constant.DATA_TYPE.FLOAT))
                        sql.append(" FLOAT");
                    else if (type.contains(Constant.DATA_TYPE.LONG))
                        sql.append(" BIGINT");
                    //定义约束
                    if (type.contains("UNIQUE"))
                        sql.append(" UNIQUE");
                    if (type.contains("NOT NULL"))
                        sql.append(" NOT NULL");
                    else if (type.contains("NULL"))
                        sql.append(" NULL");
                    if (type.contains("PRIMARY KEY"))
                        sql.append(" PRIMARY KEY");
                    //定义默认值
                    if (type.contains("DEFAULT")) {
                        sql.append(" DEFAULT" + type.substring(type.indexOf("DEFAULT") + "DEFAULT".length(), type.length()));
                    }
                }
                if (j == sps.length - 1) {
                    sql.append(" );");
                } else {
                    sql.append(" ,");
                }
            }
            return sql;
        }
    }

    /**
     * 单例获取本类实例
     *
     * @param clz 字节码
     * @return 本类实例对象
     */
    public static <T> DatabaseOpenHelper getInstance(T... clz) {
        if (instance == null) {
            synchronized (DatabaseOpenHelper.class) {
                if (instance == null) {
                    instance = new DatabaseOpenHelper(clz);
                    database = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        //建立多个表
        if (sqlList != null && sqlList.size() != 0) {
            for (StringBuilder builder : sqlList) {
                d("sql:" + builder.toString());
                sqliteDatabase.execSQL(builder.toString());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //更新ChannelBean这个表
//                upgradeTable(sqliteDatabase, ChannelBean.class);
                break;
        }
    }

//    /**
//     * 需要更新的表
//     *
//     * @param sqliteDatabase 数据库
//     * @param clazz          要更新的表的字节码
//     */
//    private void upgradeTable(SQLiteDatabase sqliteDatabase, Class clazz) {
//        Cursor cursor = null;
//        try {
//            cursor = sqliteDatabase.rawQuery("select * from ChannelBean", new String[]{});
//            int count = cursor.getColumnCount();
//            List<ChannelBean> channelBeenList = new ArrayList<>();
//            while (cursor.moveToNext()) {
//                ChannelBean channelBean = new ChannelBean();
//                for (int x = 0; x < count; x++) {
//                    switch (x) {
//                        case 1:
//                            channelBean.setCha_id(cursor.getString(x));
//                            break;
//                        case 2:
//                            channelBean.setCode(cursor.getString(x));
//                            break;
//                        case 3:
//                            channelBean.setName(cursor.getString(x));
//                            break;
//                        case 4:
//                            channelBean.setType(cursor.getString(x));
//                            break;
//                    }
//                }
//                channelBeenList.add(channelBean);
//            }
//
//            //先删除这个表
//            String deleteSQL = "DROP TABLE IF EXISTS " + clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1, clazz.getName().length());
//            LogUtil.d("sql", deleteSQL);
//            sqliteDatabase.execSQL(deleteSQL);
//            //创建新表
//
//            String createSQL = buildSQL(clazz.newInstance().toString()).toString();
//            LogUtil.d("sql", createSQL);
//            sqliteDatabase.execSQL(createSQL);
//            //批量插入旧数据
//            String sql = "insert into ChannelBean(cha_id,code,name,type) values (#)";
//            StringBuilder stringBuilder = null;
//            for (int i = 0; i < channelBeenList.size(); i++) {
//                ChannelBean channelBean = channelBeenList.get(i);
//                for (int x = 0; x < count; x++) {
//                    stringBuilder = new StringBuilder();
//                    stringBuilder.append("'").append(channelBean.getCha_id()).append("','")
//                            .append(channelBean.getCode()).append("','")
//                            .append(channelBean.getName()).append("','")
//                            .append(channelBean.getType()).append("'");
//                }
//                String execSql = sql.replace("#", stringBuilder.toString());
//                sqliteDatabase.execSQL(execSql);
//                LogUtil.d("sql", execSql);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//    }
}