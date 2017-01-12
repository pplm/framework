package org.pplm.framework.cas.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.pplm.framework.utils.config.ConfigPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author OracleGao
 *
 */
public class CasClientFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(CasClientFilter.class);
	
	public static final String DEFAULT_FILTER_PROPERITES_FILE = "cas-client-filter.properties";
	
	public static final boolean DEFAULT_CAS_SSO_ENABLE = false;
	public static final String DEFAULT_CAS_SERVER_URL_SIGN_ON_SUFFIX = "/login";
	public static final String DEFAULT_CAS_SERVER_URL_SIGN_OUT_SUFFIX = "/logout";
	public static final String DEFAULT_APP_SERVER_URL_SIGN_OUT_SUFFIX = "/logout";
	
	public static final String EXCLUDED_PATTERNS_SEPARATOR = ",";
	public static final String EXCLUDED_PATTERN_SEPARATOR = "::";
	
	public static final String PROPERTIES_KEY_CAS_SSO_ENABLE = "cas.sso.enable";
	public static final String PROPERTIES_KEY_CAS_SERVER_URL_PREFIX = "cas.server.url.prefix";
	public static final String PROPERTIES_KEY_CAS_SERVER_URL_SIGN_ON_SUFFIX = "cas.server.url.sign.on.suffix";
	public static final String PROPERTIES_KEY_CAS_SERVER_URL_SIGN_OUT_SUFFIX = "cas.server.url.sign.out.suffix";
	public static final String PROPERTIES_KEY_APP_SERVER_URL_PREFIX = "app.server.url.prefix";
	public static final String PROPERTIES_KEY_APP_SERVER_URL_EXCLUDED_PATTERNS = "app.server.url.excluded.patterns";
	public static final String PROPERTIES_KEY_APP_SERVER_URL_SIGN_OUT_SUFFIX = "app.server.url.sign.out.suffix";

	/**{PRINCIPAL_ATTRIBUTE_KEY}::${HTTP_REQUEST_HEADER_KEY},... **/
	public static final String PROPERTIES_KEY_CAS_SSO_PRINCIPAL_HEADER_MAPPING_PATTERNS = "cas.sso.principal.header.mapping.patterns";
	
	public static final String PRINCIPAL_HEADER_MAPPING_PATTERNS_SEPARATOR = ",";
	public static final String PRINCIPAL_HEADER_MAPPING_PATTERN_SEPARATOR = "::";
	
	/**casServerUrlPrefix key for cas filter **/
	public static final String KEY_CAS_SERVER_URL_PREFIX = "casServerUrlPrefix";
	/**serverName key for cas filter **/
	public static final String KEY_SERVER_NAME = "serverName";
	/**casServerLoginUrl key for cas filter **/
	public static final String KEY_CAS_SERVER_LOGIN_URL = "casServerLoginUrl";
	/**redirectAfterValidation key for cas filter **/
	public static final String KEY_REDIRECT_AFTER_VALIDATION = "redirectAfterValidation";
	/**useSession key for cas filter **/
	public static final String KEY_USE_SESSION = "useSession";
	/**hostnameVerifier key for cas filter **/
	public static final String KEY_HOSTNAME_VERIFIER = "hostnameVerifier";
	
	public static final String KEY_ENCODING = "encoding";
	
	public static final String KEY_CAS_SERVER_LOGOUT_URL = "casServerLogoutUrl";
	public static final String KEY_APP_SERVER_LOGOUT_URL = "appServerSingoutUrl";
	
	
	private List<Filter> filters = new ArrayList<Filter>();
	
	private boolean casSsoEnable;
	private String casServerUrlPrefix;
	private String casServerUrlSignOnSuffix;
	private String casServerUrlSignOutSuffix;
	private String appServerUrlPrefix;
	private String appServerUrlSignOutSuffix;
	
	private SingleSignOutFilter singleSignOutFilter;
	
	private List<ExcludedPatternBean> excludedPatterns = new ArrayList<ExcludedPatternBean>();
	
	public CasClientFilter() {
		super();
		initFilter();
	}
	
	private void initFilter() {
		try {
			ConfigPlayer.addProperties(DEFAULT_FILTER_PROPERITES_FILE, Constant.DEFAULT_PROPERTIES_FILE_ENCODING);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
		casSsoEnable = ConfigPlayer.getBoolean(PROPERTIES_KEY_CAS_SSO_ENABLE, DEFAULT_CAS_SSO_ENABLE);
		casServerUrlPrefix = ConfigPlayer.getString(PROPERTIES_KEY_CAS_SERVER_URL_PREFIX);
		casServerUrlSignOnSuffix = ConfigPlayer.getString(PROPERTIES_KEY_CAS_SERVER_URL_SIGN_ON_SUFFIX, DEFAULT_CAS_SERVER_URL_SIGN_ON_SUFFIX);
		casServerUrlSignOutSuffix = ConfigPlayer.getString(PROPERTIES_KEY_CAS_SERVER_URL_SIGN_OUT_SUFFIX, DEFAULT_CAS_SERVER_URL_SIGN_OUT_SUFFIX);
		appServerUrlPrefix = ConfigPlayer.getString(PROPERTIES_KEY_APP_SERVER_URL_PREFIX);
		appServerUrlSignOutSuffix = ConfigPlayer.getString(PROPERTIES_KEY_APP_SERVER_URL_SIGN_OUT_SUFFIX, DEFAULT_APP_SERVER_URL_SIGN_OUT_SUFFIX);
		parseExcludedPattern(ConfigPlayer.getString(PROPERTIES_KEY_APP_SERVER_URL_EXCLUDED_PATTERNS));

		//cas server not support http options method request, so exclude all uris with options method request.
		excludedPatterns.add(new ExcludedPatternBean("options", ".*"));
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (!casSsoEnable) {
			return;
		}
		
		ServletFilterConfig servletFilterConfig = null;
		singleSignOutFilter = new SingleSignOutFilter();
		servletFilterConfig = new ServletFilterConfig("CAS Single Sign Out Filter", filterConfig);
		servletFilterConfig.addInitParameter(KEY_CAS_SERVER_URL_PREFIX, casServerUrlPrefix);
		singleSignOutFilter.init(servletFilterConfig);
		
		Filter filter = new SignOutFilter();
		servletFilterConfig = new ServletFilterConfig("APP Sign Out filter", filterConfig);
		servletFilterConfig.addInitParameter(KEY_CAS_SERVER_LOGOUT_URL, Utils.joinWithoutNull(casServerUrlPrefix, casServerUrlSignOutSuffix));
		servletFilterConfig.addInitParameter(KEY_APP_SERVER_LOGOUT_URL, Utils.joinWithoutNull(appServerUrlPrefix, appServerUrlSignOutSuffix));
		filter.init(servletFilterConfig);
		filters.add(filter);
		
		filter = new AuthenticationFilter();
		servletFilterConfig = new ServletFilterConfig("CAS Authentication Filter", filterConfig);
		servletFilterConfig.addInitParameter(KEY_CAS_SERVER_LOGIN_URL, Utils.joinWithoutNull(casServerUrlPrefix, casServerUrlSignOnSuffix));
		servletFilterConfig.addInitParameter(KEY_SERVER_NAME, appServerUrlPrefix);
		filter.init(servletFilterConfig);
		filters.add(filter);
		
		filter = new Cas30ProxyReceivingTicketValidationFilter();
		servletFilterConfig = new ServletFilterConfig("CAS Validation Filter", filterConfig);
		servletFilterConfig.addInitParameter(KEY_CAS_SERVER_URL_PREFIX, casServerUrlPrefix);
		servletFilterConfig.addInitParameter(KEY_SERVER_NAME, appServerUrlPrefix);
		servletFilterConfig.addInitParameter(KEY_REDIRECT_AFTER_VALIDATION, "true", false);
		servletFilterConfig.addInitParameter(KEY_USE_SESSION, "true", false);
		servletFilterConfig.addInitParameter(KEY_HOSTNAME_VERIFIER, "com.k2data.platform.cas.client.SslTrustHostNameVerifier", false);
		servletFilterConfig.addInitParameter(KEY_ENCODING, Constant.DEFAULT_PROPERTIES_FILE_ENCODING, false);
		filter.init(servletFilterConfig);
		filters.add(filter);
		
		filter = new HttpServletRequestWrapperFilter();
		servletFilterConfig = new ServletFilterConfig("CAS HttpServletRequest Wrapper Filter", filterConfig);
		servletFilterConfig.addInitParameter("ignoreCase", "true", false);
		filter.init(servletFilterConfig);
		filters.add(filter);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!casSsoEnable) {
			chain.doFilter(request, response);
			return;
		}
		
		StatedInnerFilterChain statedInnerFilterChain = new StatedInnerFilterChain(request, response);
		singleSignOutFilter.doFilter(statedInnerFilterChain.getRequest(), statedInnerFilterChain.getResponse(), statedInnerFilterChain.reset());
		
		if (isExcludedRequest(statedInnerFilterChain.getRequest())) {
			chain.doFilter(statedInnerFilterChain.getRequest(), statedInnerFilterChain.getResponse());
			return;
		}
		
		for (Filter filter: filters) {
			filter.doFilter(statedInnerFilterChain.getRequest(), statedInnerFilterChain.getResponse(), statedInnerFilterChain.reset());
			if (!statedInnerFilterChain.isDoFilter) {
				return;
			}
		}
		
		chain.doFilter(statedInnerFilterChain.getRequest(), statedInnerFilterChain.getResponse());
	}

	@Override
	public void destroy() {
		if (!casSsoEnable) {
			return;
		}
		Collections.reverse(filters);
		for (Filter filter : filters) {
			filter.destroy();
		}
	}

	private boolean isExcludedRequest(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		for (ExcludedPatternBean excludedPatternBean : excludedPatterns) {
			if (isExcludedUri(excludedPatternBean.uriPattern, httpServletRequest.getRequestURI())
					&& isExcludedMethod(excludedPatternBean.methodPattern, httpServletRequest.getMethod())) {
				logger.trace("exclude uri: [" + httpServletRequest.getRequestURI() + "], method [" + httpServletRequest.getMethod() + "]");
				return true;
			}
		}
		return false;
	}
	
	private boolean isExcludedMethod(String methodPattern, String method) {
		if ("*".equals(methodPattern)) {
			return true;
		} else {
			return methodPattern.equalsIgnoreCase(method);
		}
	}
	
	private boolean isExcludedUri(String uriPattern, String uri) {
		return uri.matches(uriPattern);
	}
	
	private void parseExcludedPattern(String exculdedPatternsStr) {
		if (exculdedPatternsStr == null) {
			return;
		}
		String[] patterns = exculdedPatternsStr.split(EXCLUDED_PATTERNS_SEPARATOR);
		if (patterns == null || patterns.length == 0) {
			return;
		}
		String[] pattern = null;
		for (String temp : patterns) {
			pattern = temp.split(EXCLUDED_PATTERN_SEPARATOR);
			if (pattern.length == 1) {
				if (!StringUtils.isBlank(pattern[0])) {
					excludedPatterns.add(new ExcludedPatternBean("*", pattern[0].trim()));
				}
			} else if (pattern.length > 1) {
				excludedPatterns.add(new ExcludedPatternBean(pattern[0].trim(), pattern[1].trim()));
			} 
		}
	}
	
	private class ExcludedPatternBean {
		private String methodPattern;
		private String uriPattern;

		public ExcludedPatternBean(String methodPattern, String uriPattern) {
			super();
			this.methodPattern = methodPattern;
			this.uriPattern = uriPattern;
		}
	}
	
	private static class StatedInnerFilterChain implements FilterChain {
		private ServletRequest request;
		private ServletResponse response;
		
		private boolean isDoFilter;
		
		public StatedInnerFilterChain(ServletRequest request, ServletResponse response) {
			super();
			this.request = request;
			this.response = response;
			this.isDoFilter = false;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			this.request = request;
			this.response = response;
			this.isDoFilter = true;
		}

		public ServletRequest getRequest() {
			return request;
		}

		public ServletResponse getResponse() {
			return response;
		}
		
		public StatedInnerFilterChain reset() {
			this.isDoFilter = false;
			return this;
		}
	}
	
}
