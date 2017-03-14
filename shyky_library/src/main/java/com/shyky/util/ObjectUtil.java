package com.shyky.util;

/**
 * 对象操作工具类
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class ObjectUtil {
	/**
	 * 构造方法私有化
	 */
	private ObjectUtil() {

	}

	public static boolean isNull(Object object) {
		return null == object;
	}

	// public static boolean isNull(Object... objects) {
	// if (isNull(objects) || objects.length == 0)
	// return true;
	// for (Object object : objects)
	// if (isNull(object))
	// return true;
	// return false;
	// }
	public static boolean notNull(Object object) {
		return !isNull(object);
	}

	// public static boolean notNull(Object... objects) {
	// if (notNull(objects) || objects.length == 0)
	// return true;
	// int count = 0;
	// for (Object object : objects)
	// if (isNull(object))
	// count++;
	// return count == 0 ? true : false;
	// }

	/**
	 * 比较对象是否相等
	 * 
	 * @param objects
	 *            要比较的对象，至少两个以上
	 * @return 相等返回true，不相等返回false
	 */
	public static boolean equals(Object... objects) {
		if (isNull(objects))
			return false;
		if (objects.length < 2)
			throw new IllegalArgumentException("至少要两个对象以上才能比较");
		boolean result = false;
		for (int j = 0; j < objects.length; j++) {
			for (int k = j + 1; k < objects.length; k++)
				if (objects[j] == objects[k] || objects[j].equals(objects[k]))
					result = true;
		}
		return result;
	}
}