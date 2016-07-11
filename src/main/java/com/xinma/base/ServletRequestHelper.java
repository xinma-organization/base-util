package com.xinma.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServletRequestHelper {

	private static final String COMMA = ",";

	private static final String charsetName = "utf-8";

	private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";

	private static final String[] IP_HEADER_NAMES = new String[] { "HTTP_X_FORWARDED_FOR", X_FORWARDED_FOR_HEADER,
			"HTTP_CLIENT_IP", "X-Forwarded-Host", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

	/**
	 * 获取ServletContext根目录绝对路径
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return ServletContext根目录绝对路径
	 */
	public static String getContextRealPath(HttpServletRequest request) {
		String sysPath = request.getSession().getServletContext().getRealPath("/");
		if (!StringUtils.endsWith(sysPath, File.separator)) {
			sysPath = sysPath + File.separator;
		}
		return sysPath;
	}

	/**
	 * 根据HttpServletRequest获取客户端的IP地址
	 * 
	 * @param request
	 *            javax.servlet.http.HttpServletRequest
	 * @return java.lang.String
	 */
	public static String getClientIp(HttpServletRequest request) {

		String ip = getClientIpAddrFromRequestHeader(request);
		if (StringUtils.isBlank(ip)) {
			ip = request.getRemoteAddr();
		}

		// If the ip is still null, we just return 0.0.0.0 to avoid empty value
		return StringUtils.isBlank(ip) ? "0.0.0.0" : ip;
	}

	/**
	 * 获取应用程序根url
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 获取应用程序根URL
	 */
	public static String getApplicationHost(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
	}

	/**
	 * 获取程序运行主机的Ip地址
	 * 
	 * @return IP地址字符串，例如192.168.8.9
	 * @throws SocketException
	 *             IP地址无法访问时抛出的Socket异常
	 */
	public static String getLocalHostIp() throws SocketException {

		String localIp = null;
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
						&& inetAddress.isSiteLocalAddress()) {
					// IFCONFIG.append(inetAddress.getHostAddress().toString() +
					// "\n");
					localIp = inetAddress.getHostAddress().toString();
				}
			}
		}
		return localIp;
	}

	/**
	 * 获取HttpServletRequest的x-forward字符串
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return HttpServletRequest包含x-forward，则返回x-forward字符串列表；否则返回null
	 */

	public static String[] parseXForwardedFor(HttpServletRequest request) {
		String forwardedFor = request.getHeader(X_FORWARDED_FOR_HEADER);
		if (forwardedFor == null) {
			return null;
		}

		String[] ips = StringUtils.split(forwardedFor, COMMA);
		for (int i = 0; i < ips.length; i++) {
			ips[i] = StringUtils.substringBeforeLast(ips[i], ":").trim();
		}
		return ips;
	}

	/**
	 * 获取HttpServletRequest的请求体(request body),返回结果为字符串
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return HttpServletRequest的请求体字符串
	 * @throws IOException
	 *             IO异常
	 */
	public static String getStringBodyFromRequest(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), charsetName));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	/**
	 * 获取HttpServletRequest的请求体(request body),返回结果为JsonNode对象
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return HttpServletRequest请求体，并将其转化为JsonNode对象
	 * @throws IOException
	 *             IO异常
	 */
	public static JsonNode getJsonBodyFromRequest(HttpServletRequest request) throws IOException {
		return new ObjectMapper().readTree(getStringBodyFromRequest(request));
	}

	/**
	 * 获取HttpServletRequest的请求体(request body),返回结果为List\
	 * <JsonNode\>对象,要求请求体必须为每行都是JSON形式字符串
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 请求体转换为JsonNode列表
	 * @throws IOException
	 *             IO异常
	 */
	public static List<JsonNode> getJsonListBodyFromRequest(HttpServletRequest request) throws IOException {

		List<JsonNode> list = new ArrayList<JsonNode>();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), charsetName));
		String line = null;

		while ((line = br.readLine()) != null) {
			if (StringUtils.isNotBlank(line)) {
				list.add(new ObjectMapper().readTree(line));
			}
		}
		return list;
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return HttpServletRequest头信息中包含IP信息，返回IP地址字符串；否则返回Null
	 */
	private static String getClientIpAddrFromRequestHeader(HttpServletRequest request) {

		for (String ipHeaderName : IP_HEADER_NAMES) {

			String originalIp = request.getHeader(ipHeaderName);
			if (originalIp == null) {
				continue;
			}
			String[] ips = StringUtils.split(originalIp, COMMA);
			for (String ip : ips) {
				if (isInvalidIp(ip)) {
					continue;
				}
				// filter out port from IP address
				ip = StringUtils.substringBeforeLast(ip, ":");
				try {
					// get client ip
					InetAddress address = InetAddress.getByName(ip);
					// Check whether the ipAddress is in private range
					if (!address.isSiteLocalAddress()) {
						return address.getHostAddress();
					}
				} catch (UnknownHostException e) {
					// indicate it's an illegal address
					// we try to get ip from next request header
				}
			}
		}
		return null;
	}

	/**
	 * 判断IP地址是否有效
	 * 
	 * @param ip
	 *            IP地址字符串
	 * @return IP地址字符串不会为空且不等于"unknown",返回true;否则false
	 */
	private static boolean isInvalidIp(String ip) {

		return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
	}

	/**
	 * 判断请求是否是微信浏览器发出
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Request由微信发出，返回true;否则返回false
	 */
	public static boolean checkIfOpenInWechat(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (StringUtils.isBlank(userAgent)) {
			return false;
		}
		userAgent = userAgent.toLowerCase();
		if (userAgent.indexOf("micromessenger") > 0) {
			// 是微信浏览器
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 输出JSON相应格式
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param object
	 *            Response要输出的对象
	 * @throws IOException
	 *             PrintWriter的IO异常
	 */
	public static void responseOutWithJson(HttpServletResponse response, Object object) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(new ObjectMapper().writeValueAsString(object));
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
