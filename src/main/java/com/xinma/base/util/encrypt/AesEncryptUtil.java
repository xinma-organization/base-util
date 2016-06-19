package com.xinma.base.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

/**
 * AES加密工具类
 * 
 * @author Alauda
 *
 * @date 2016年6月16日
 *
 */
public class AesEncryptUtil {

	private AesEncryptParam param;

	private AesEncryptUtil() {
	}

	/**
	 * 获取加密工具类实例
	 * 
	 * @param param
	 *            AES加密参数
	 * @return CipherAESUtil实例
	 */
	public static AesEncryptUtil getInstance(AesEncryptParam param) {
		AesEncryptUtil instance = new AesEncryptUtil();
		instance.param = param;
		return instance;
	}

	/**
	 * 16进制字符串转成byte[]
	 * 
	 * @param hexStr
	 *            16进制字符串
	 * @return 成功，返回byte数组；否则返回null
	 */
	public static byte[] hexStr2Bytes(String hexStr) {
		if (StringUtils.isBlank(hexStr)) {
			return null;
		}
		hexStr = hexStr.toUpperCase();
		int len = hexStr.length() / 2;
		char[] hexChars = hexStr.toCharArray();
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return result;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 加密文件
	 * 
	 * @param srcFileName
	 *            加密的原文件全路径名
	 * @param desFileName
	 *            加密的目标文件全路径名
	 * @throws Exception
	 *             抛出异常
	 */
	public void jiaMiWenJian(String srcFileName, String desFileName) throws Exception {
		jiaMiStream(new FileInputStream(srcFileName), new FileOutputStream(desFileName));
	}

	/**
	 * 加密Stream
	 * 
	 * @param in
	 *            要加密的输入流
	 * @param out
	 *            加密后的输出流
	 * @throws Exception
	 *             抛出异常
	 */
	public void jiaMiStream(InputStream in, OutputStream out) throws Exception {
		try { // 生成密钥
			byte[] raw = param.getKey();
			SecretKeySpec key = new SecretKeySpec(raw, "AES");
			Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器

			c1.init(Cipher.ENCRYPT_MODE, key);// 初始化

			CipherOutputStream cos = new CipherOutputStream(out, c1);
			byte[] buffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				cos.write(buffer, 0, bytesRead);
			}
			cos.close();
		} finally {

			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 解密文件
	 * 
	 * @param srcFileName
	 *            要解密的文件全路径名
	 * @param desFileName
	 *            解密后的目标文件全路径名
	 * @throws Exception
	 *             抛出异常
	 */
	public void jieMiWenJian(String srcFileName, String desFileName) throws Exception {

		jieMiStream(new FileInputStream(srcFileName), new FileOutputStream(desFileName));
	}

	/**
	 * 解密File对象
	 * 
	 * @param srcFile
	 *            要解密的文件对象
	 * @param desFile
	 *            解密后的文件对象
	 * @throws Exception
	 *             抛出异常
	 */
	public void jieMiFile(File srcFile, File desFile) throws Exception {

		jieMiStream(new FileInputStream(srcFile), new FileOutputStream(desFile));

	}

	/**
	 * 解密Stream对象
	 * 
	 * @param in
	 *            要解密的Stream对象
	 * @param out
	 *            解密后的Stream对象
	 * @throws Exception
	 *             抛出异常
	 */
	public void jieMiStream(InputStream in, OutputStream out) throws Exception {
		try { // 生成密钥
			byte[] raw = param.getKey();

			SecretKeySpec key = new SecretKeySpec(raw, "AES");
			Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器

			c1.init(Cipher.DECRYPT_MODE, key);// 初始化
			// Read bytes, decrypt, and write them out.
			byte[] buffer = new byte[2048];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(c1.update(buffer, 0, bytesRead));
			}
			// Write out the final bunch of decrypted bytes
			out.write(c1.doFinal());
			out.flush();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 加密Byte[]数组
	 * 
	 * @param clearBytes
	 *            要加密的byte[]
	 * @return 加密后的byte[]
	 * @throws Exception
	 *             抛出的异常
	 */
	public byte[] cipherBytes(byte[] clearBytes) throws Exception {
		byte[] raw = param.getKey();
		SecretKeySpec key = new SecretKeySpec(raw, "AES");
		Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器

		c1.init(Cipher.ENCRYPT_MODE, key);// 初始化
		return c1.doFinal(clearBytes);
	}

	/**
	 * 解密Byte[]数组
	 * 
	 * @param encyptedBytes
	 *            要解密的Byte[]
	 * @return 解密后的byte[]
	 * @throws Exception
	 */
	public byte[] deCipherBytes(byte[] encyptedBytes) throws Exception {
		byte[] raw = param.getKey();
		SecretKeySpec key = new SecretKeySpec(raw, "AES");
		Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器
		c1.init(Cipher.DECRYPT_MODE, key);// 初始化
		return c1.doFinal(encyptedBytes);
	}

	/**
	 * 加密字符串
	 * 
	 * @param clearString
	 *            要加密的明文字符串
	 * @return 加密后的密文字符串
	 * @throws Exception
	 *             抛出异常
	 */
	public String cipherString(String clearString) throws Exception {
		byte[] raw = param.getKey();
		SecretKeySpec key = new SecretKeySpec(raw, "AES");
		Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器
		c1.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] input = clearString.getBytes(Charset.forName("UTF-8"));
		byte[] output = c1.doFinal(input);
		return HexEncryptUtil.bytesToHexString(output);
	}

	/**
	 * 解密字符串
	 * 
	 * @param encyptedString
	 *            要解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 *             抛出异常
	 */
	public String deCipherString(String encyptedString) throws Exception {

		byte[] raw = param.getKey();
		SecretKeySpec key = new SecretKeySpec(raw, "AES");
		Cipher c1 = Cipher.getInstance(param.getAlgorithm());// 创建密码器

		c1.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] input = HexEncryptUtil.hexStringToBytes(encyptedString);
		byte[] output = c1.doFinal(input);

		return new String(output, Charset.forName("UTF-8"));

	}

}
