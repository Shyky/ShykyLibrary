package com.shyky.library;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.socks.library.KLog.e;

/**
 * APP崩溃处理，防止系统弹出ANR，并且会造成Fragment重叠问题
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/9/7
 * @since 1.0
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;

    /**
     * 获取实例对象
     *
     * @return CrashHandler实例对象
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context 应用程序上下文
     */
    public void init(@NonNull Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        e("APP崩溃了，别担心我不会让系统报ANR的...");
//        PrintStream ps = null;
//        final String fileName = StorageUtil.getSDcardRootPath() + File.separator + "error" + TimeUtil.getCurrentTime("_yyyy-MM-dd_HH-mm-ss", Locale.getDefault()) + ".log";
//        try {
//            File file = new File(fileName);
//            file.createNewFile();
//            ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
//            ex.printStackTrace(ps);
//            ps.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (ps != null) {
//                ps.close();
//            }
//        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}