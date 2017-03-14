package com.shyky.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 输入输出流操作工具类
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class IOUtil {
	/**
	 * 构造方法私有化
	 */
	private IOUtil() {

	}

	/**
	 * 输入流转换成字符串
	 *
	 * @param inputStream
	 *            要转换的输入流
	 * @return 转换后的字符串
	 */
	public static String inputStream2String(InputStream inputStream) {
		BufferedReader br = null;
		try {
			StringBuilder sb = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}