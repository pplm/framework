package org.pplm.framework.utils.servlet.filter;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author OracleGao
 *
 */
public class HttpMatchPattern {
	
	public static final String DEFAULT_WILDCARD_NONE_MATCH = "-";
	public static final String DEFAULT_WILDCARD_ALL_MATCH = "*";
	public static final String DEFAULT_SEPARATOR_PARSE = "::";
	
	public static final String DEFAULT_PATTERN = DEFAULT_WILDCARD_NONE_MATCH;
	
	public static final HttpMatchPattern NONE_MATCH = new HttpMatchPattern(DEFAULT_WILDCARD_NONE_MATCH);
	public static final HttpMatchPattern ALL_MATCH = new HttpMatchPattern(DEFAULT_WILDCARD_ALL_MATCH);
	
	private String urlPattern;
	private String methodPattern;
	private String contentTypePattern;
	
	private HttpMatchPattern() {
		super();
	}

	private HttpMatchPattern(String wildcard) {
		super();
		this.urlPattern = wildcard;
		this.methodPattern = wildcard;
		this.contentTypePattern = wildcard;
	}

	public boolean isUrlMatch(String url) {
		return regularMatch(url, urlPattern);
	}
	
	public boolean isMethodMatch(String method) {
		return caseMatch(method, methodPattern);
	}
	
	public boolean isContentTypeMatch(String contentType) {
		return regularMatch(contentType, contentTypePattern);
	}
	
	private boolean regularMatch(String value, String pattern) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		if (urlPattern.equals(DEFAULT_WILDCARD_NONE_MATCH)) {
			return false;
		}
		if (urlPattern.equals(DEFAULT_WILDCARD_ALL_MATCH)) {
			return true;
		}
		return value.matches(pattern);
	} 
	
	private boolean caseMatch(String value, String pattern) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		if (urlPattern.equals(DEFAULT_WILDCARD_NONE_MATCH)) {
			return false;
		}
		if (urlPattern.equals(DEFAULT_WILDCARD_ALL_MATCH)) {
			return true;
		}
		return value.equalsIgnoreCase(pattern);
	}
	
	public String getUrlPattern() {
		return urlPattern;
	}

	public String getMethodPattern() {
		return methodPattern;
	}

	public String getContentTypePattern() {
		return contentTypePattern;
	}
	
	private void setUrlPattern(String urlPattern) {
		this.urlPattern = parseValue(urlPattern, DEFAULT_PATTERN);
	}

	private void setMethodPattern(String methodPattern) {
		this.methodPattern = parseValue(methodPattern, DEFAULT_PATTERN);
	}
	
	private void setContentTypePattern(String contentTypePattern) {
		this.contentTypePattern = parseValue(contentTypePattern, DEFAULT_PATTERN);
	}
	
	private String parseValue(String src, String defaultValue) {
		if (StringUtils.isBlank(src)) {
			return defaultValue;
		} else {
			return src.trim();
		}
	}
	
	public static HttpMatchPattern parse(String pattern) {
		HttpMatchPattern httpExcludePattern =  new HttpMatchPattern();
		if (StringUtils.isBlank(pattern) || DEFAULT_WILDCARD_NONE_MATCH.equals(pattern)) {
			return NONE_MATCH;
		}
		if (pattern.equals(DEFAULT_WILDCARD_ALL_MATCH)) {
			return ALL_MATCH;
		}
		String[] patterns = pattern.split(DEFAULT_SEPARATOR_PARSE);
		if (patterns.length > 3) {
			httpExcludePattern.setUrlPattern(patterns[0]);
			httpExcludePattern.setMethodPattern(patterns[1]);
			httpExcludePattern.setContentTypePattern(patterns[2]);
		} else if (patterns.length > 2) {
			httpExcludePattern.setUrlPattern(patterns[0]);
			httpExcludePattern.setMethodPattern(patterns[1]);
			httpExcludePattern.contentTypePattern = DEFAULT_PATTERN;
		} else {
			httpExcludePattern.setUrlPattern(patterns[0]);
			httpExcludePattern.methodPattern = DEFAULT_PATTERN;
			httpExcludePattern.contentTypePattern = DEFAULT_PATTERN;
		}
		return httpExcludePattern;
	}
	
}
