package com.shyky.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 加解密工具类
 *
 * @author Shyky
 * @version 1.1
 * @date 2016/5/31
 * @since 1.0
 */
public final class EncodeUtil {
	/**
	 * 构造方法
	 */
	private EncodeUtil() {

	}

	// 从字符串的指定位置截取指定长度的子字符串
	private static String CutString(String str, int startIndex, int length) {
		if (startIndex >= 0) {
			if (length < 0) {
				length = length * -1;
				if (startIndex - length < 0) {
					length = startIndex;
					startIndex = 0;
				} else {
					startIndex = startIndex - length;
				}
			}
			if (startIndex > str.length()) {
				return "";
			}
		} else {
			if (length < 0) {
				return "";
			} else {
				if (length + startIndex > 0) {
					length = length + startIndex;
					startIndex = 0;
				} else {
					return "";
				}
			}
		}
		if (str.length() - startIndex < length) {
			length = str.length() - startIndex;
		}
		return str.substring(startIndex, startIndex + length);
	}

	// 从字符串的指定位置开始截取到字符串结尾的了符串
	private static String CutString(String str, int startIndex) {
		return CutString(str, startIndex, str.length());
	}

	// 用于 RC4 处理密码 pass 密码字串 kLen 密钥长度，一般为 256
	static private byte[] GetKey(byte[] pass, int kLen) {
		byte[] mBox = new byte[kLen];
		for (int i = 0; i < kLen; i++) {
			mBox[i] = (byte) i;
		}
		int j = 0;
		for (int i = 0; i < kLen; i++) {
			j = (j + (mBox[i] + 256) % 256 + pass[i % pass.length]) % kLen;
			byte temp = mBox[i];
			mBox[i] = mBox[j];
			mBox[j] = temp;
		}
		return mBox;
	}

	// 生成随机字符
	private static String RandomString(int lens) {
		char[] CharArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		int clens = CharArray.length;
		String sCode = "";
		Random random = new Random();
		for (int i = 0; i < lens; i++) {
			sCode += CharArray[Math.abs(random.nextInt(clens))];
		}
		return sCode;
	}

	public static String Encode(String desKey, String source) {
		try {
			if (source == null || desKey == null) {
				return "";
			}
			int ckey_length = 4;
			String keya, keyb, keyc, cryptkey;
			desKey = MD52(desKey);
			keya = MD52(CutString(desKey, 0, 16));
			keyb = MD52(CutString(desKey, 16, 16));
			keyc = ckey_length > 0 ? RandomString(ckey_length) : "";
			cryptkey = keya + MD52(keya + keyc);

			source = "0000000000" + CutString(MD52(source + keyb), 0, 16) + source;
			byte[] temp = RC4(source.getBytes("UTF-8"), cryptkey);
			return keyc + Base64.encode(temp);

		} catch (Exception e) {
			return "";
		}
	}

	public static String Decode(String desKey, String source) {
		try {
			if (source == null || desKey == null) {
				return "";
			}
			int ckey_length = 4;
			String keya, keyb, keyc, cryptkey, result;
			desKey = MD52(desKey);
			keya = MD52(CutString(desKey, 0, 16));
			keyb = MD52(CutString(desKey, 16, 16));
			keyc = ckey_length > 0 ? CutString(source, 0, ckey_length) : "";
			cryptkey = keya + MD52(keya + keyc);

			byte[] temp;
			temp = Base64.decode(CutString(source, ckey_length));
			result = new String(RC4(temp, cryptkey));
			if (CutString(result, 10, 16).equals(CutString(MD52(CutString(result, 26) + keyb), 0, 16))) {
				return CutString(result, 26);
			} else {
				temp = Base64.decode(CutString(source + "=", ckey_length));
				result = new String(RC4(temp, cryptkey));
				if (CutString(result, 10, 16).equals(CutString(MD52(CutString(result, 26) + keyb), 0, 16))) {
					return CutString(result, 26);
				} else {
					temp = Base64.decode(CutString(source + "==", ckey_length));
					result = new String(RC4(temp, cryptkey));
					if (CutString(result, 10, 16).equals(CutString(MD52(CutString(result, 26) + keyb), 0, 16))) {
						return CutString(result, 26);
					} else {
						return "2";
					}
				}
			}

		} catch (Throwable e) {
			return "";
		}

	}

	// RC4 原始算法 input原始字串数组 pass 密钥
	private static byte[] RC4(byte[] input, String pass) {
		if (input == null || pass == null)
			return null;
		byte[] output = new byte[input.length];
		byte[] mBox = GetKey(pass.getBytes(), 256);
		// 加密
		int i = 0;
		int j = 0;
		for (int offset = 0; offset < input.length; offset++) {
			i = (i + 1) % mBox.length;
			j = (j + (mBox[i] + 256) % 256) % mBox.length;
			byte temp = mBox[i];
			mBox[i] = mBox[j];
			mBox[j] = temp;
			byte a = input[offset];
			// byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];
			// mBox[j] 一定比 mBox.Length 小，不需要在取模
			byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];
			output[offset] = (byte) ((int) a ^ toInt(b));
		}
		return output;
	}

	private static String MD52(String MD5) {
		StringBuffer sb = new StringBuffer();
		String part = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(MD5.getBytes());
			for (byte aMd5 : md5) {
				part = Integer.toHexString(aMd5 & 0xFF);
				if (part.length() == 1) {
					part = "0" + part;
				}
				sb.append(part);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static int toInt(byte b) {
		return (b + 256) % 256;
	}
}