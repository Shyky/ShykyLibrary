package com.shyky.util;

import java.security.MessageDigest;

/**
 * 安全相关工具类，包括加密和解密等
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/7/13
 * @since 1.0
 */
public final class SecurityUtil {
	/**
	 * 加密算法为MD5
	 */
	public static final String ALGORITHM_MD5 = "MD5";
	/**
	 * 加密算法为SHA-1
	 */
	public static final String ALGORITHM_SHA_1 = "SHA-1";
	/**
	 * 加密算法为SHA-256
	 */
	public static final String ALGORITHM_SHA_256 = "SHA-256";

	/**
	 * 构造方法私有化
	 */
	private SecurityUtil() {

	}

	/**
	 * 默认使用MD5加密字符串
	 * 
	 * @param str
	 *            要加密的字符串
	 * @return 如果字符串不为空返回加密后的字符串，否则返回null
	 */
	public static String encrypt(String str) {
		if (TextUtil.isEmpty(str))
			return null;
		return md5Encrypt(ALGORITHM_MD5, str);
	}

	/**
	 * 加密字符串
	 * 
	 * @param algorithm
	 *            加密算法，从本类中的常量取
	 * @param str
	 *            要加密的字符串
	 * @see ALGORITHM_MD5
	 * @return 如果字符串不为空返回加密后的字符串，否则返回null
	 */
	public static String encrypt(String algorithm, String str) {
		System.out.println(algorithm);
		return md5Encrypt(algorithm, str);
	}

	/***
	 * MD5加密 生成32位md5码
	 * 
	 * @param 待加密字符串
	 * @return 返回32位md5码
	 */
	private static String md5Encrypt(String algorithm, String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(algorithm);
			byte[] byteArray = str.getBytes("UTF-8");
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}