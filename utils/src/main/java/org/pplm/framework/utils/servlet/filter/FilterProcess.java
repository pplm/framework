package org.pplm.framework.utils.servlet.filter;

import javax.servlet.FilterConfig;

/**
 * 
 * @author OracleGao
 *
 */
public interface FilterProcess {
	public void init(FilterConfig filterConfig);
	public void destroy();
}
