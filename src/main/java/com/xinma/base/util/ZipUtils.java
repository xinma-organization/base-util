package com.xinma.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

public class ZipUtils {
	private static Logger logger = Logger.getGlobal();
	private final static String charSet = "UTF-8";

	/**
	 * 压缩文件
	 * 
	 * @param files
	 *            被压缩文件的绝对路径加文件名的字符串组
	 * @param zippedFile
	 *            压缩后文件绝对路径及文件名
	 */
	public static void zip(String[] files, String zippedFile) {
		File zipped = new File(zippedFile);
		String path = "";
		File[] srcFiles = new File[files.length];
		for (int i = 0; i < files.length; i++) {
			File file = new File(files[i]);
			srcFiles[i] = file;
		}
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipped), Charset.forName(charSet));

			zipFiles(out, path, srcFiles);
			out.close();
		} catch (IOException ioe) {
			ioe.getStackTrace();
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param file
	 *            被压缩文件的路径
	 * @param zippedFile
	 *            压缩后的文件路径
	 * @return 压缩成功，返回true；压缩失败，返回false
	 */
	public static boolean zip(String file, String zippedFile) {
		File zipped = new File(zippedFile);
		String path = "";
		File[] srcFiles = new File[1];
		srcFiles[0] = new File(file);
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipped), Charset.forName(charSet));

			boolean result = zipFiles(out, path, srcFiles);
			out.close();
			return result;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "file not found:" + zipped, e);
		}
		return false;
	}

	/**
	 * 文件压缩
	 * 
	 * @param out
	 *            压缩文件输出流对象
	 * @param path
	 *            被压缩文件路径
	 * @param srcFiles
	 *            被压缩源文件数组
	 * @return 压缩成功，返回true；否则返回false
	 */

	public static boolean zipFiles(ZipOutputStream out, String path, File... srcFiles) {
		path = path.replaceAll("\\*", "/");
		if (StringUtils.isNotBlank(path) && !path.endsWith("/")) {
			path += "/";
		}
		byte[] buf = new byte[1024];
		try {
			for (int i = 0; i < srcFiles.length; i++) {
				// 判断是否是目录
				if (srcFiles[i].isDirectory()) {
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if (!srcPath.endsWith("/")) {
						srcPath += "/";
					}
					out.putNextEntry(new ZipEntry(path + srcPath));
					zipFiles(out, path + srcPath, files);
					out.closeEntry();
				} else {
					FileInputStream in = new FileInputStream(srcFiles[i]);
					out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}
			}
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 解压文件
	 * 
	 * @param zippedFile
	 *            压缩文件绝对路径及文件名 d:/aa.zip
	 * @param destFolder
	 *            解压文件绝对路径 d:/或者d:/ab/
	 * 
	 * @return 解压成功，返回true;解压失败，返回false
	 */
	public static boolean unzip(String zippedFile, String destFolder) {
		File zipFile = new File(zippedFile);
		File pathFile = new File(destFolder);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile, Charset.forName(charSet));
			for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				String outPath = (destFolder + zipEntryName).replaceAll("\\*", "/");

				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}

				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			zip.close();
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error occurs when unzip (" + zippedFile + ") to (" + destFolder + ")", e);
		} finally {
			if (zip != null)
				try {
					zip.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "error occurs when close zip file", e);
				}
		}
		return false;
	}

	/**
	 * 解压文件
	 * 
	 * @param zippedFile
	 *            压缩文件绝对路径及文件名 d:/aa.zip
	 * @param destFolder
	 *            解压文件绝对路径 d:/或者d:/ab/
	 * @return list 返回压缩文件中文件的名字集合
	 * @throws IOException
	 *             IO异常
	 */
	public static List<String> unzips(String zippedFile, String destFolder) throws IOException {
		File zipFile = new File(zippedFile);
		File pathFile = new File(destFolder);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			List<String> list = new ArrayList<String>();
			zip = new ZipFile(zipFile, Charset.forName(charSet));
			for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				list.add(destFolder + zipEntryName);
				InputStream in = zip.getInputStream(entry);
				String outPath = (destFolder + zipEntryName).replace('\\', '/');

				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}

				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}

			return list;
		} finally {
			if (zip != null) {
				zip.close();
			}
		}
	}
}
