package org.pplm.framework.utils.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.pplm.framework.utils.servlet.wapper.BufferedStreamHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTrackFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(HttpTrackFilter.class);

	public static final String KEY_EXCLUDE_PATTERNS = "excludePatterns";
	
	public static final String KEY_HTTP_SESSION_TRACKER = "httpSessionTracker";
	public static final String KEY_HTTP_TRACKER_PROCESSER = "httpTrackProcesser";
	
	public static final String KEY_BODY_CLIP_REQUEST_SIZE = "bodyClipRequestSize";
	
	public static final String DEFAULT_SEPARATOR = ",";
	
	public static final int DEFAULT_BODY_CLIP_REQUEST_SIZE = 2000;
	
	private HttpSessionTrackI httpSessionTracker;
	private HttpTrackProcessI httpTrackProcesser;
	
	private Collection<HttpMatchPattern> excludePatterns;
	
	private int bodyClipRequestSize;
	
	public HttpTrackFilter() {
		super();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludePatterns = parsePatterns(filterConfig.getInitParameter(KEY_EXCLUDE_PATTERNS));
		
		bodyClipRequestSize = DEFAULT_BODY_CLIP_REQUEST_SIZE;
		String temp = filterConfig.getInitParameter(KEY_BODY_CLIP_REQUEST_SIZE);
		if (StringUtils.isNoneEmpty(temp)) {
			try {
				bodyClipRequestSize = Integer.parseInt(temp);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		
		temp = filterConfig.getInitParameter(KEY_HTTP_SESSION_TRACKER);
		if (StringUtils.isNoneEmpty(temp)) {
			try {
				httpSessionTracker = (HttpSessionTrackI) Class.forName(temp).newInstance();
				httpSessionTracker.init(filterConfig);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
		
		temp = filterConfig.getInitParameter(KEY_HTTP_TRACKER_PROCESSER);
		if (StringUtils.isNoneEmpty(temp)) {
			try {
				httpTrackProcesser = (HttpTrackProcessI) Class.forName(temp).newInstance();
				httpTrackProcesser.init(filterConfig);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpTrackBean httpTrackBean = new HttpTrackBean();
		
		httpTrackBean.setUrl(httpServletRequest.getRequestURL().toString());
		httpTrackBean.setMethod(httpServletRequest.getMethod());
		httpTrackBean.setContentTypeRequest(httpServletRequest.getContentType());
		if (!isTracked(httpTrackBean)) {
			chain.doFilter(httpServletRequest, response);
			return;
		}
		httpTrackBean.setParameters(httpServletRequest.getParameterMap());
		httpTrackBean.setClientIp(getClientIp(httpServletRequest));
		if (httpSessionTracker != null) {
			httpTrackBean.setHttpSession(httpSessionTracker.track(httpServletRequest.getSession(true)));
		}
		httpTrackBean.setHeadersRequest(getHeadersRequest(httpServletRequest));
		httpTrackBean.setEncodingRequest(httpServletRequest.getCharacterEncoding());
		if (bodyClipRequestSize > 0) {
			httpServletRequest = new BufferedStreamHttpServletRequest(httpServletRequest);
			httpTrackBean.setBodyClipRequest(getBodyClipRequest(httpServletRequest.getInputStream()));
		}
		httpTrackBean.setTrackTimeRequest(System.currentTimeMillis());
		
		chain.doFilter(httpServletRequest, response);
		
		httpTrackBean.setTrackTimeResponse(System.currentTimeMillis());
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpTrackBean.setHeadersResponse(getHeadersResponse(httpServletResponse));
		httpTrackBean.setEncodingResponse(httpServletResponse.getCharacterEncoding());
		httpTrackBean.setContentTypeResponse(httpServletResponse.getContentType());
		httpTrackBean.setBodyClipResponse(null);//implements in future
		httpTrackBean.setStatusResponse(httpServletResponse.getStatus());
		if (httpTrackProcesser != null) {
			httpTrackProcesser.process(httpTrackBean);
		}
	}
	
	@Override
	public void destroy() {
		if (httpSessionTracker != null) {
			httpSessionTracker.destroy();
		}
		if (httpTrackProcesser != null) {
			httpTrackProcesser.destroy();
		}
	}

	protected boolean isTracked(HttpTrackBean httpTrackBean) {
		if (excludePatterns == null || excludePatterns.size() == 0) {
			return true;
		}
		for (HttpMatchPattern excludePattern : excludePatterns) {
			if (excludePattern.isUrlMatch(httpTrackBean.getUrl())
					&& excludePattern.isMethodMatch(httpTrackBean.getMethod())
					&& excludePattern.isContentTypeMatch(StringUtils.trimToEmpty(httpTrackBean.getContentTypeRequest()))) {
				return false;
			}
		}
		return true;
	}
	
	private Map<String, String> getHeadersRequest(HttpServletRequest httpServletRequest) {
		Map<String, String> headers = new HashMap<String, String>();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		String key = null;
		while (headerNames.hasMoreElements()) {
			key = headerNames.nextElement();
			headers.put(key, httpServletRequest.getHeader(key));
		}
		return headers;
	}

	private Map<String, String> getHeadersResponse(HttpServletResponse httpServletResponse) {
		Map<String, String> headers = new HashMap<String, String>();
		Collection<String> headerNames = httpServletResponse.getHeaderNames();
		for (String key : headerNames) {
			headers.put(key, httpServletResponse.getHeader(key));
		}
		return headers;
	}

	private String getClientIp(HttpServletRequest httpServletRequest) {
		InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
		// Parsing from Multi Stage Reverse Proxy
		String value = httpServletRequest.getHeader("X-Forwarded-For");
		if (value != null && value.indexOf(",") > -1) {
			String[] array = value.split(",");
			for (String item : array) {
				if (inetAddressValidator.isValid(item)) {
					return item;
				}
			}
		}
		value = httpServletRequest.getHeader("X-Real-IP");
		if (value != null && inetAddressValidator.isValid(value)) {
			return value;
		}
		value = httpServletRequest.getRemoteAddr();
		if (value != null && inetAddressValidator.isValid(value)) {
			return value;
		}
		return "unknow";
	}
	
	private String getBodyClipRequest(ServletInputStream servletInputStream) throws IOException {
		servletInputStream.mark(bodyClipRequestSize);
		byte[] buffer = new byte[bodyClipRequestSize];
		int length = IOUtils.read(servletInputStream, buffer);
		servletInputStream.reset();
		return new String(buffer, 0, length);
	}

	private Collection<HttpMatchPattern> parsePatterns(String src) {
		if (StringUtils.isBlank(src)) {
			return null;
		}
		List<HttpMatchPattern> patterns = new ArrayList<HttpMatchPattern>();
		String[] items = src.split(DEFAULT_SEPARATOR);
		for (String item : items) {
			patterns.add(HttpMatchPattern.parse(item));
		}
		return patterns;
	}
	
}
