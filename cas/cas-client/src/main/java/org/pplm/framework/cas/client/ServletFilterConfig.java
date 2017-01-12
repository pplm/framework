package org.pplm.framework.cas.client;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * 
 * @author OracleGao
 *
 */
public class ServletFilterConfig implements FilterConfig {
	
	private final String filterName;

	private final ServletContext servletContext;

	private final Map<String, String> initParameters = new HashMap<String, String>();

	public ServletFilterConfig(String filterName, FilterConfig filterConfig) {
		this.filterName = filterName;
		this.servletContext = filterConfig.getServletContext();
		
		Enumeration<String> enumeration = filterConfig.getInitParameterNames();
		String name = null;
		while(enumeration.hasMoreElements()) {
			name = enumeration.nextElement();
			initParameters.put(name, filterConfig.getInitParameter(name));
		}
	}
	
	@Override
	public String getFilterName() {
		return filterName;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void addInitParameter(String name, String value) {
		if (value != null) {
			addInitParameter(name, value, true);
		}
	}
	
	public void addInitParameter(String name, String value, boolean isCover) {
		if (value == null) {
			return;
		}
		if (isCover || !this.initParameters.containsKey(name)) {
			this.initParameters.put(name, value);
		}
	}
	
	@Override
	public String getInitParameter(String name) {
		return this.initParameters.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(this.initParameters.keySet());
	}
	
}
