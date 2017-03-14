package com.shyky.util;

import java.util.Random;

/**
 * 随机数相关工具类
 * Author: Created by Shyky on 2016/4/14.
 * Email: sj1510706@163.com
 */

/**
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/4/14
 * @since 1.0
 */
public final class RandomUtil {
	/**
	 * 构造方法私有化
	 */
	private RandomUtil() {

	}

	/**
	 * 生成10位数的随机密码
	 *
	 * @return 随机密码
	 */
	public static String generatePassword() {
		return generatePassword(10);
	}

	/**
	 * 生成指定位数的随机密码
	 *
	 * @return 随机密码
	 */
	public static String generatePassword(int length) {
		if (length < 1 && length > 100) {
			return null;
		}
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer();
		Random random = new Random();
		for (int j = 0; j < length; j++) {
			int index = Math.abs(random.nextInt(str.length));
			pwd.append(str[index]);
		}
		return pwd.toString();
	}

	/**
	 * 或取0到num的随机Int型正整数据
	 *
	 * @param num
	 *            所要取到的最大的整数
	 * @return 成功返回一个区间内随机的整数，失败返回-1；
	 */
	public static int getRandomInt(int num) {
		if (num > 0) {
			Random random = new Random();
			return random.nextInt(num);
		} else {
			return -1;
		}
	}
}