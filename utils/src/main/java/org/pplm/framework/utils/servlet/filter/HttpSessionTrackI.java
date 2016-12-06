package org.pplm.framework.utils.servlet.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author OracleGao
 *
 */
public interface HttpSessionTrackI {
	public void init(FilterConfig filterConfig);
	
	public HttpSessionTrackBean track(HttpSession httpSession);
	
	public void destroy();
}
