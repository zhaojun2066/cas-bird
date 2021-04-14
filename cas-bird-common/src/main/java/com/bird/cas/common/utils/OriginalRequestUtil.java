package com.bird.cas.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class OriginalRequestUtil {

	/**
	 * 获取原始请求域名
	 * @return 先通过header获取原始请求域名，再取修改后的域名
	 */
	public static String getOrignalHost(HttpServletRequest request) {
		String originalHost = request.getHeader("X-Origin-Host");
		if (Objects.isNull(originalHost))
			originalHost = request.getServerName();
		return originalHost;
	}
	
	/**
	 * 获取真实的请求路径的url
	 * @param request
	 * @return
	 */
	public static String getOriginalUrl(HttpServletRequest request) {
		String currentUrl = request.getRequestURL().toString();
		String originalHost = request.getHeader("X-Origin-Host");
		if (Objects.nonNull(originalHost))
			return currentUrl.replace(request.getServerName(), originalHost);
		return request.getRequestURL().toString();
	}
	
	/**
	 * 判断请求协议是否 https
	 * @param request
	 * @return
	 */
	public static boolean isSecureSchema(HttpServletRequest request) {
		// nginx 代理配置，获取协议信息
		String schema = request.getHeader("x-forwarded-proto");
		if (Objects.isNull(schema))
			schema = request.getScheme();
		return "https".equalsIgnoreCase(schema);
	}

	/**
	 * 是否IE浏览器，对IE不添加 secure 和 samesite 属性
	 * @param request
	 * @return
	 */
	public static boolean isIEBrowser(HttpServletRequest request) {
		String useragent = request.getHeader("user-agent");
		if (Objects.nonNull(useragent))
			return SameSiteNoneIncompatibleClientChecker.isIEBrowser(useragent);
		return false;
	}
	
	/**
	 * 是否需要兼容 samesite cookie 属性
	 * @param request
	 * @return
	 */
	public static boolean isSameBrowser(HttpServletRequest request) {
		String useragent = request.getHeader("user-agent");
		if (Objects.nonNull(useragent))
			return SameSiteNoneIncompatibleClientChecker.shouldSendSameSiteNone(useragent);
		return false;
	}
	
	public static void main(String[] args) {
		String ie = "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko";
		boolean siteNone = SameSiteNoneIncompatibleClientChecker.shouldSendSameSiteNone(ie);
		System.out.println(siteNone);
	}
	
}
