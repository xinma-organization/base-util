package com.xinma.base.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于AES算法BASE64算法工具类对数据进行加密，先对数据进行AES加密，再用BASE64对加密后的数据进行编码
 * 
 * @author Alauda
 *
 * @date 2016年6月19日
 *
 */
public class AesBase64Encryption {

	/**
	 * 编码方式
	 */
	private final static String charsetName = "UTF-8";

	/**
	 * AES加密转换模式
	 */
	private final static String defaultTransformation = "AES/ECB/PKCS5Padding";

	/**
	 * 用于非URL传输的加密算法
	 * 
	 * @param data
	 *            要加密的内容
	 * @param aesKey
	 *            密码
	 * @return
	 * @throws Exception
	 */
	/**
	 * 对数据进行aes加密和BASE64编码
	 * 
	 * @param plainText
	 *            明文
	 * @param aesKey
	 *            aes加密算法key
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String plainText, String aesKey) throws Exception {
		if (plainText == null || aesKey == null)
			return null;
		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey.getBytes(charsetName), "AES"));
		byte[] bytes = cipher.doFinal(plainText.getBytes(charsetName));
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * 对数据进行aes解密和BASE64解密
	 * 
	 * @param cipherText
	 *            密文
	 * @param aesKey
	 *            aes加密密码
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String cipherText, String aesKey) throws Exception {
		if (cipherText == null || aesKey == null)
			return null;
		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey.getBytes(charsetName), "AES"));
		byte[] bytes = Base64.decodeBase64(cipherText);
		bytes = cipher.doFinal(bytes);
		return new String(bytes, charsetName);
	}

	/**
	 * URL安全的加密算法
	 * 
	 * @param plainText
	 *            明文
	 * @param aesKey
	 *            aes密码
	 * @return 密文
	 * @throws Exception
	 */
	public static String urlSafeEncrypt(String plainText, String aesKey) throws Exception {
		if (plainText == null || aesKey == null)
			return null;
		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey.getBytes(charsetName), "AES"));
		byte[] bytes = cipher.doFinal(plainText.getBytes(charsetName));
		return Base64.encodeBase64URLSafeString(bytes);
	}

	/**
	 * URL安全的解密密算法
	 * 
	 * @param cipherText
	 *            密文
	 * @param aesKey
	 *            密码
	 * @return 明文
	 * @throws Exception
	 */
	public static String urlSafeDecrypt(String cipherText, String aesKey) throws Exception {
		if (cipherText == null || aesKey == null)
			return null;

		String base64Data = StringUtils.replace(StringUtils.replace(cipherText, "!", "+"), "-", "/");

		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey.getBytes(charsetName), "AES"));
		byte[] bytes = Base64.decodeBase64(base64Data);
		bytes = cipher.doFinal(bytes);
		return new String(bytes, charsetName);
	}

	/**
	 * URL安全的加密算法
	 * 
	 * @param plainText
	 *            明文
	 * @param aesKey
	 *            aes密码
	 * @return 密文
	 * @throws Exception
	 */
	public static String urlSafeEncrypt(String plainText, byte[] aesKey) throws Exception {
		if (plainText == null || aesKey == null)
			return null;
		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
		byte[] bytes = cipher.doFinal(plainText.getBytes(charsetName));
		return Base64.encodeBase64URLSafeString(bytes);
	}

	/**
	 * URL安全的解密密算法
	 * 
	 * @param cipherText
	 *            密文
	 * @param aesKey
	 *            密码
	 * @return 明文
	 * @throws Exception
	 */
	public static String urlSafeDecrypt(String cipherText, byte[] aesKey) throws Exception {
		if (cipherText == null || aesKey == null)
			return null;

		String base64Data = StringUtils.replace(StringUtils.replace(cipherText, "!", "+"), "-", "/");

		Cipher cipher = Cipher.getInstance(defaultTransformation);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
		byte[] bytes = Base64.decodeBase64(base64Data);
		bytes = cipher.doFinal(bytes);
		return new String(bytes, charsetName);
	}

	/**
	 * URL安全的加密算法,AES算法采用CBC模式
	 * 
	 * @param aesKey
	 *            aes加密密钥
	 * @param aesIV
	 *            aes加密初始化向量
	 * @param plainText
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypWithAesCbcMode(byte[] aesKey, byte[] aesIV, byte[] plainText) throws Exception {
		byte[] actualKey = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] actualIV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		System.arraycopy(aesKey, 0, actualKey, 0, aesKey.length > 16 ? 16 : aesKey.length);
		System.arraycopy(aesIV, 0, actualIV, 0, aesIV.length > 16 ? 16 : aesIV.length);

		IvParameterSpec iv = new IvParameterSpec(actualIV);
		SecretKeySpec skeySpec = new SecretKeySpec(actualKey, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(plainText);

		return Base64.encodeBase64URLSafeString(encrypted);
	}

	/**
	 * URL安全的解密算法,AES算法采用CBC模式
	 * 
	 * @param aesKey
	 *            aes加密密钥
	 * @param aesIV
	 *            aes加密初始化向量
	 * @param cipherText
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	public static byte[] decryptWithAesCbcMode(byte[] aesKey, byte[] aesIV, String cipherText) throws Exception {
		byte[] actualKey = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] actualIV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		System.arraycopy(aesKey, 0, actualKey, 0, aesKey.length > 16 ? 16 : aesKey.length);
		System.arraycopy(aesIV, 0, actualIV, 0, aesIV.length > 16 ? 16 : aesIV.length);

		IvParameterSpec iv = new IvParameterSpec(actualIV);
		SecretKeySpec skeySpec = new SecretKeySpec(actualKey, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		return cipher.doFinal(Base64.decodeBase64(cipherText));
	}

}
