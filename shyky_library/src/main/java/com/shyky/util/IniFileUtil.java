package com.shyky.util;

import java.io.File;
import java.io.IOException;

/**
 * 配置(.ini或.properties)文件操作工具类
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/9
 * @since 1.0
 */
public final class IniFileUtil {
	/**
	 * .ini配置文件名
	 */
	private static String iniFileName;
	/**
	 * 保存要写入的数据，用于一次性写入到配置文件中
	 */
	private static StringBuilder content;

	/**
	 * 构造方法私有化
	 */
	private IniFileUtil() {

	}

	/**
	 * 打开配置文件
	 * 
	 * @param file
	 *            文件对象
	 * @return 成功返回true，失败返回false
	 */
	public static boolean open(File file) {
		try {
			// 文件不存在，则创建文件
			if (!ObjectUtil.isNull(file) && !FileUtil.exists(file))
				file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// if (FileUtil.isFile(file)) {
		System.out.println(file.getAbsolutePath());
		if (!ObjectUtil.isNull(iniFileName))
			throw new IllegalArgumentException("此方法只能调用一次");
		iniFileName = file.getAbsolutePath();
		return true;
		// }
		// return false;
	}

	public static boolean open(String fileName) {
		System.out.println(fileName);
		return open(new File(fileName));
	}

	public static void check() {
		if (ObjectUtil.isNull(iniFileName))
			throw new NullPointerException("必须要调用IniFileUtil.open(xx)方法");
	}

	public static boolean addCategory(String categoryName) {
		check();
		File file = new File(iniFileName);
		if (FileUtil.isFile(file) && !TextUtil.isEmpty(categoryName)) {
			return FileUtil.write(file, "[" + categoryName + "]\n", true);
		}
		return false;
	}

	public static boolean save(String name, String value) {
		check();
		File file = new File(iniFileName);
		if (FileUtil.isFile(file) && !TextUtil.isEmpty(name) && !ObjectUtil.isNull(value)) {
			return FileUtil.write(file, name.trim() + "=" + value + "\n", true);
		}
		return false;
	}

	public static boolean save(String name, char value) {
		return save(name, TextUtil.toString(value));
	}

	public static boolean save(String name, boolean value) {
		return save(name, TextUtil.toString(value));
	}

	public static boolean save(String name, int value) {
		return save(name, TextUtil.toString(value));
	}

	public static boolean save(String name, long value) {
		return save(name, TextUtil.toString(value));
	}

	public static boolean save(String name, float value) {
		return save(name, TextUtil.toString(value));
	}

	public static boolean save(String name, double value) {
		return save(name, TextUtil.toString(value));
	}

	public static void put(String name, String value) {
		check();
		if (ObjectUtil.isNull(content))
			content = new StringBuilder();
		if (TextUtil.isEmpty(name))
			throw new NullPointerException("配置项名称不能为null或空白字符");
		else if (ObjectUtil.isNull(value))
			throw new NullPointerException("数据不能为空");
		String data = name.trim() + "=" + value + "\n";
		if (content.indexOf(name.trim()) == -1)
			content.append(data);
	}

	public static void put(String name, boolean value) {
		put(name, TextUtil.toString(value));
	}

	public static void put(String name, int value) {
		put(name, TextUtil.toString(value));
	}

	public static void put(String name, float value) {
		put(name, TextUtil.toString(value));
	}

	public static void put(String name, double value) {
		put(name, TextUtil.toString(value));
	}

	/**
	 * 添加注释信息
	 * 
	 * @param comment
	 *            注释信息
	 * @return 成功返回true，失败返回false
	 */
	public static boolean addComment(String comment) {
		check();
		File file = new File(iniFileName);
		if (FileUtil.isFile(file) && !TextUtil.isEmpty(comment)) {
			return FileUtil.write(file, "--" + comment + "\n", true);
		}
		return false;
	}

	public static String read(String name) {
		check();
		String content = FileUtil.read(iniFileName);
		if (TextUtil.isEmpty(content))
			return null;
		String[] spls = content.split("\n");
		String result = null;
		if (!ObjectUtil.isNull(spls) && spls.length > 0) {
			for (String item : spls) {
				System.out.println("item -- >" + item);
				// 过滤配置节
				if (!TextUtil.isEmpty(item) && item.charAt(0) == '[' && item.charAt(item.length() - 1) == ']')
					continue;
				else if (item.trim().equals("\n"))
					continue; // 过滤行
				else if (item.trim().equals(""))
					continue; // 过滤行
				String[] ss = item.split("=");
				if (ObjectUtil.notNull(ss) && ss.length == 2) {
					String key = ss[0];
					System.out.println("key = " + key);
					String value = ss[1];
					if (key.equals(name.trim())) {
						result = value;
					}
				}
			}
		}
		return result;
	}

	public static boolean delete(String name) {
		check();
		String content = FileUtil.read(iniFileName);
		if (!TextUtil.isEmptyAndNull(content)) {
			String[] spls = content.split("\n");
			if (!ObjectUtil.isNull(spls) && spls.length > 0) {
				StringBuilder result = new StringBuilder(content);
				boolean state = false;
				for (String item : spls) {
					// 有当前配置项
					if (item.startsWith(name.trim() + "=")) {
						state = true;
						continue;
					}
					result.append(item).append("\n");
				}
				if (state) {
					FileUtil.delete(iniFileName);
					FileUtil.createFile(iniFileName);
					return FileUtil.write(iniFileName, result.deleteCharAt(result.length() - 1).toString());
				}
			}
		}
		return false;
	}

	public static boolean commit() {
		if (TextUtil.isEmptyAndNull(content))
			return false;
		return FileUtil.write(iniFileName, content.toString());
	}
}