package com.xinma.base.util.encrypt;

import com.xinma.base.util.HexUtil;

/**
 * AES对称加密参数实体类
 * @author Alauda
 *
 * @date 2016年6月16日
 *
 */
public class AesEncryptParam {
	/**
	 * 加密模式
	 */
	private String algorithm = "AES";
	
	/**
	 * 加密密钥
	 */
	private byte[] key = "ABCDEF0123456789".getBytes();


	/**
	 * 默认实例
	 */
	private static AesEncryptParam instance = new AesEncryptParam();

	public AesEncryptParam() {
		
	}
	
	public AesEncryptParam(String algorithm, String key) {
		this.algorithm = algorithm;
		this.key = HexUtil.parseByteArray(key);
	}

	public AesEncryptParam(String algorithm, byte[] key) {
		this.algorithm = algorithm;
		this.key = key;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public static AesEncryptParam getDefaultInstance() {
		return instance;
	}
}
