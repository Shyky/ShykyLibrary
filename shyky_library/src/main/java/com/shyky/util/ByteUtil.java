package com.shyky.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by shyky on 16-4-7.
 */

/**
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/4/7
 * @since 1.0
 */
public final class ByteUtil {
	/**
	 * 构造方法私有化
	 */
	private ByteUtil() {

	}

	public static String byte2String(byte[] bytes) {
		// return new String(bytes, 0, bytes.length);
		try {
			return new String(bytes, 0, bytes.length, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}