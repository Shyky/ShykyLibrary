package com.shyky.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 文件操作工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class FileUtil {
    /**
     * 构造方法私有化
     */
    private FileUtil() {

    }

    public static boolean isNull(File file) {
        return ObjectUtil.isNull(file);
    }

    /**
     * 判断指定文件是否存在
     *
     * @param file 文件对象
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(File file) {
        return isNull(file) || file.exists();
    }

    /**
     * 判断指定文件是否存在
     *
     * @param fileName 文件对象
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(String fileName) {
        return !TextUtil.isEmpty(fileName) && new File(fileName).exists();
    }

    public static boolean isFile(File file) {
        return isNull(file) || file.isFile();
    }

    public static boolean isFile(String fileName) {
        if (TextUtil.isEmptyAndNull(fileName))
            return false;
        return isFile(new File(fileName));
    }

    public static boolean isDirectory(File file) {
        return isNull(file) || file.isDirectory();
    }

    public static boolean isDirectoryEmpty(File file) {
        return ObjectUtil.isNull(listFiles(file));
    }

    /**
     * 删除指定的文件
     *
     * @param file 文件对象
     * @return
     */
    public static boolean deleteFile(File file) {
        if (exists(file))
            return file.delete();
        return false;
    }

    /**
     * 删除指定目录下所有的文件
     *
     * @param file 目录文件对象
     * @return
     */
    public static boolean deleteDirectory(File file) {
        if (!isNull(file) && exists(file) && isDirectory(file)) {
            final File[] files = listFiles(file);
            if (!ObjectUtil.isNull(files)) {
                // 先删除该目录下所有的文件
                for (File f : files)
                    deleteFile(f);
                // 最后删除该目录
                deleteFile(file);
            }
        }
        return false;
    }

    /**
     * 删除指定目录下所有的文件
     *
     * @param directoryName 目录文件对象
     * @return
     */
    public static boolean deleteDirectory(String directoryName) {
        if (!TextUtil.isEmptyAndNull(directoryName))
            return deleteDirectory(new File(directoryName));
        return false;
    }

    /**
     * 删除指定的文件或目录
     *
     * @param file 文件或目录对象
     */
    public static boolean delete(File file) {
        if (!isNull(file)) {
            if (isFile(file))
                return deleteFile(file);
            else if (isDirectory(file))
                return deleteDirectory(file);
        }
        return false;
    }

    /**
     * 删除指定的文件或目录
     *
     * @param fileOrDirName 文件名（全路径）或目录名
     */
    public static boolean delete(String fileOrDirName) {
        if (!TextUtil.isEmptyAndNull(fileOrDirName))
            return delete(new File(fileOrDirName));
        return false;
    }

    public static File[] listFiles(File file) {
        if (isDirectory(file)) {
            final File[] files = file.listFiles();
            return (files != null && files.length > 0) ? files : null;
        }
        return null;
    }

    public static boolean write(File file, String content, boolean append) {
        // OutputStreamWriter osw = null;
        if (ObjectUtil.isNull(content))
            return false;
        else if (!exists(file))
            return false;
        FileOutputStream fos = null;
        try {
            // osw = new OutputStreamWriter(new FileOutputStream(file),
            // "utf-8");
            // osw.write(content);
            // osw.flush();
            fos = new FileOutputStream(file, append);
            System.out.println("write --> content==" + content);
            fos.write(content.getBytes("utf-8"), 0, content.length());
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (!ObjectUtil.isNull(fos))
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean write(File file, String content) {
        return write(file, content, false);
    }

    public static boolean write(String fileName, String content) {
        return write(new File(fileName), content, false);
    }

    public static boolean write(String fileName, String content, boolean append) {
        return write(new File(fileName), content, append);
    }

    public static String read(File file) {
        if (!exists(file))
            return null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while (fis.read(b, 0, b.length) != -1) {
                sb.append(new String(b, 0, b.length, "utf-8"));
            }
            fis.close();
            return sb.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (ObjectUtil.notNull(fis))
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String read(String fileName) {
        return read(new File(fileName));
    }

    public static boolean createFile(File file) {
        try {
            if (ObjectUtil.notNull(file))
                return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFile(String fileName) {
        if (TextUtil.isEmpty(fileName))
            return false;
        return createFile(new File(fileName));
    }
}