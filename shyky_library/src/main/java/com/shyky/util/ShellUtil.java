package com.shyky.util;

import java.io.IOException;

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
public final class ShellUtil {
	/**
	 * 构造方法私有化
	 */
	private ShellUtil() {

	}

	public static void execute(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			String result = IOUtil.inputStream2String(process.getInputStream());
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		execute("java -version");
	}
}