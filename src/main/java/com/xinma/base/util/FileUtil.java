package com.xinma.base.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件工具类方法封装
 * 
 * @author Alauda
 *
 * @date 2016年5月23日
 *
 */
public class FileUtil {

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return 存在则返回扩展名，否则为null
	 */
	public static String getFileExtensionName(String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			int dotIndex = fileName.lastIndexOf('.');
			if ((dotIndex > 0) && (dotIndex < (fileName.length() - 1))) {
				return fileName.substring(dotIndex + 1);
			}
		}
		return null;
	}

	/**
	 * 获取文件基本名称，不包含文件扩展名
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 文件基本名
	 */
	public static String getBasicFileName(String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			int dotIndex = fileName.lastIndexOf('.');
			if ((dotIndex > 0) && (dotIndex < (fileName.length()))) {
				return fileName.substring(0, dotIndex);
			}
		}
		return fileName;
	}
}
