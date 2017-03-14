package com.shyky.library.constant;

/**
 * 各种常量类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/1/20
 * @since 1.0
 */
public class Constant {
    /**
     * 空白字符串
     */
    public static final String EMPTY_STR = "";

    /**
     * 配置文件中Key名称
     */
    public static class SHARED_KEY {
        /**
         * 是否第一次使用
         */
        public static final String IS_FIRST_USE = "isFirstUse";
        public static final String IS_LOGIN = "isLogin";
        public static final String LOGIN_TYPE = "loginType";
        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_NICKNAME = "userNickName";
        public static final String USER_HEADER = "userHeadImage";
        public static final String USER_PHONE = "userPhone";
        public static final String USER_EMAIL = "userEmail";
        public static final String SCREEN_WIDTH = "screenWidth";
        public static final String SCREEN_HEIGHT = "screenHeight";

        /**
         * 构造方法私有化
         */
        protected SHARED_KEY() {

        }
    }

    /**
     * EventBus事件类型
     */
    public static class EVENT_TYPE {
        public static final int FINISH = 188;
        public static final int ON_KEY_BACK_DOWN = 189;

        protected EVENT_TYPE() {

        }
    }

    /**
     * Intent请求码,用于标识startActivityForResult中所用的requestCode
     */
    public static class REQUEST_CODE {
        /**
         * 请求成功返回码
         */
        public static final int SUCCESS = 88;
        /**
         * 请求失败返回码
         */
        public static final int FAILURE = 44;
        /**
         * 请求取消返回码
         */
        public static final int CANCEL = 22;

        /**
         * 构造方法私有化
         */
        protected REQUEST_CODE() {

        }
    }

    /**
     * 数据类型名称
     */
    public static class DATA_TYPE {
        public static final String BOOLEAN = "boolean";
        public static final String INT = "int";
        public static final String SHORT = "short";
        public static final String LONG = "long";
        public static final String BYTE = "byte";
        public static final String FLOAT = "float";
        public static final String DOUBLE = "double";
        public static final String CHAR = "char";
        public static final String STRING = "String";

        /**
         * 构造方法私有化
         */
        protected DATA_TYPE() {

        }
    }

    public static class PAGE_SIZE {
        /**
         * 分页大小，即每页条数，这里固定为每页显示20条
         */
        public static final int _20 = 20;
        /**
         * 分页大小，即每页条数，这里固定为每页显示15条
         */
        public static final int _15 = 15;
        /**
         * 分页大小，即每页条数，这里固定为每页显示9条
         */
        public static final int _9 = 9;
        /**
         * 分页大小，即每页条数，这里固定为每页显示10条
         */
        public static final int _10 = 10;

        /**
         * 构造方法私有化
         */
        protected PAGE_SIZE() {

        }
    }

    /**
     * 字符编码名称
     */
    public static class CHARSET_NAME {
        /**
         * GB2312编码
         */
        public static final String GB2312 = "gb2312";
        /**
         * GBK编码
         */
        public static final String GBK = "gbk";
        /**
         * UTF-8编码
         */
        public static final String UTF8 = "utf-8";
        /**
         * ISO-8859-1编码
         */
        public static final String ISO88591 = "iso-8859-1";

        /**
         * 构造方法私有化
         */
        protected CHARSET_NAME() {

        }
    }

    /**
     * 数据传递,intent.putExtra方法中的key名称
     */
    public static class EXTRA_NAME {
        public static final String DATA = "intent_bundle";
        public static final String ENTITY = "entity";
        public static final String RESULT = "result";
        public static final String URL = "url";
        public static final String TITLE = "title";

        /**
         * 构造方法私有化
         */
        protected EXTRA_NAME() {

        }
    }

    /**
     * 构造方法保护化
     */
    protected Constant() {

    }
}