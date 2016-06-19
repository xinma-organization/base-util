package com.xinma.base.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesEncryptUtil {
	private TripleDesParam param;

	private DesEncryptUtil() {
	}

	/**
	 * 获取DES加密工具类实例
	 * 
	 * @param param
	 *            3des加密参数
	 * @return DesEncryptUtil对象
	 */
	public static DesEncryptUtil getInstance(TripleDesParam param) {
		DesEncryptUtil cipher = new DesEncryptUtil();
		cipher.param = param;
		return cipher;
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
		File file = new File(srcFileName);
		if (file.exists() && file.isFile() && srcFileName.endsWith(".zip")) {
			file.delete();
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
		try {
			DESedeKeySpec spec = new DESedeKeySpec(param.getKey());
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
			SecretKey deskey = keyfactory.generateSecret(spec);
			Cipher c1 = Cipher.getInstance(param.getAlgorithm());
			IvParameterSpec ips = new IvParameterSpec(param.getIv());

			c1.init(Cipher.ENCRYPT_MODE, deskey, ips);

			CipherOutputStream cos = new CipherOutputStream(out, c1);
			byte[] buffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				cos.write(buffer, 0, bytesRead);
			}
			cos.close();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
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
		try {
			DESedeKeySpec spec = new DESedeKeySpec(param.getKey());
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
			Key deskey = keyfactory.generateSecret(spec);
			Cipher c1 = Cipher.getInstance(param.getAlgorithm());
			IvParameterSpec ips = new IvParameterSpec(param.getIv());

			c1.init(Cipher.DECRYPT_MODE, deskey, ips);
			byte[] buffer = new byte[2048];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(c1.update(buffer, 0, bytesRead));
			}
			out.write(c1.doFinal());
			out.flush();
		} finally {
			// 关闭文件流
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
}
