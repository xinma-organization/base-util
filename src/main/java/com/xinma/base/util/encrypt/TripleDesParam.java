package com.xinma.base.util.encrypt;

/**
 * 3des对称加密算法参数实体类
 * 
 * @author Alauda
 *
 * @date 2016年6月19日
 *
 */
public class TripleDesParam {
	/**
	 * 加密模式
	 */

	private String algorithm = "DESede/CBC/PKCS5Padding";

	/**
	 * 加密密钥
	 */
	private byte[] key = "012345671234567823456789".getBytes();

	/**
	 * 加密算法的向量
	 */
	private byte[] iv = "12345678".getBytes();

	private static TripleDesParam instance = new TripleDesParam();

	public TripleDesParam() {
	}

	public TripleDesParam(String algorithm, byte[] key, byte[] iv) {
		this.algorithm = algorithm;
		this.key = key;
		this.iv = iv;
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

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public static TripleDesParam getDefaultInstance() {
		return instance;
	}
}
