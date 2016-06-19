package com.xinma.base.util;

public class HexUtil {
	public static byte[] parseByteArray(String str, String expr) {
		if (str != null) {
			str = str.trim();
			if ("".equals(str) == false) {
				String[] byteStr = str.split(expr);
				byte[] byteArray = new byte[byteStr.length];
				for (int i = 0; i < byteStr.length; i++) {
					byteArray[i] = parseByte(byteStr[i]);
				}
				return byteArray;
			}
		}
		return null;
	}

	public static byte[] parseByteArray(String str) {
		if (str != null) {
			str = str.trim();
			if ("".equals(str) == false) {
				if (str.length() % 2 != 0) {
					str = "0" + str;
				}
				byte[] byteArray = new byte[str.length() / 2];
				for (int i = 0; i < byteArray.length; i++) {
					byteArray[i] = parseByte(str.substring(2 * i, 2 * i + 2));
				}
				return byteArray;
			}
		}
		return null;
	}

	public static byte parseByte(String str) {
		str = str.toUpperCase();
		byte b = 0;
		for (int j = 0; j < str.length() && j < 2; j++) {
			char c = str.charAt(j);
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				b = (byte) (b * (byte) 0x10 + (byte) (c - '0'));
				break;
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				b = (byte) (b * (byte) 0x10 + (byte) (c - 'A' + 0xA));
				break;
			default:
				break;
			}
		}
		return b;
	}

	public static String byteArrayToString(byte[] buffer, int len) {
		return byteArrayToString(buffer, 0, len, "");
	}

	public static String byteArrayToString(byte[] buffer, int pos, int len) {
		return byteArrayToString(buffer, pos, len, "");
	}

	public static String byteArrayToString(byte[] buffer) {
		return byteArrayToString(buffer, 0, buffer.length, "");
	}

	public static String byteArrayToString(byte[] buffer, int len, String expr) {
		return byteArrayToString(buffer, 0, len, expr);
	}

	public static String byteArrayToString(byte[] buffer, String expr) {
		return byteArrayToString(buffer, 0, buffer.length, expr);
	}

	public static String byteArrayToString(byte[] buffer, int pos, int len, String expr) {
		if (buffer == null)
			return "";
		if (buffer.length < len) {
			len = buffer.length;
		}
		String format = "%02X" + expr;
		StringBuilder sb = new StringBuilder();
		for (int i = pos; i < len + pos; i++) {
			sb.append(String.format(format, buffer[i], expr));
		}
		return sb.toString();
	}

	public static void printByteArray(byte[] buffer, int len) {
		if (buffer == null)
			return;
		if (buffer.length < len) {
			len = buffer.length;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(String.format("%02X, ", buffer[i]));
		}

	}

	public static void printByteArray(byte[] buffer) {
		if (buffer == null)
			return;
		int len = buffer.length;
		printByteArray(buffer, len);
	}

}
