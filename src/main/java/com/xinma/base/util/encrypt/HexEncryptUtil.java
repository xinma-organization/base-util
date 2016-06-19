package com.xinma.base.util.encrypt;

import java.nio.charset.Charset;

public class HexEncryptUtil {
	private final static String HEX = "0123456789ABCDEF";

	/**
	 * 将字符串转化成16进制字符串
	 * 
	 * @param string
	 *            要加密的字符串
	 * @return 机密后生成的16进制的字符串
	 */
	public static String stringToHexString(String string) {
		return bytesToHexString(string.getBytes(Charset.forName("UTF-8")));
	}

	/**
	 * 将16进制字符串转换成普通字符串
	 * 
	 * @param hexString
	 *            要转换的16进制字符串
	 * @return 生成的普通字符串
	 * 
	 */
	public static String hexStringToString(String hexString) {
		return new String(hexStringToBytes(hexString), Charset.forName("UTF-8"));
	}

	/**
	 * 将16进制的字符串转化为byte[]数组
	 * 
	 * @param hexString
	 *            16进制的字符串
	 * @return 生成的byte[]数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	/**
	 * 将byte数组转化为16进制字符串
	 * 
	 * @param bytes
	 *            要加密的byte数组
	 * @return 生成的16进制字符串
	 */
	public static String bytesToHexString(byte[] bytes) {
		if (bytes == null)
			return null;

		StringBuffer result = new StringBuffer(2 * bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			appendHex(result, bytes[i]);
		}
		return result.toString();
	}

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}
