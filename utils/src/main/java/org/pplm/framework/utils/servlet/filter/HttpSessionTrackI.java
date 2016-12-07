package org.pplm.framework.utils.servlet.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author OracleGao
 *
 */
public interface HttpSessionTrackI<T extends HttpSessionTrackBean> {
	public void init(FilterConfig filterConfig);
	
	public T track(HttpSession httpSession);
	
	public void destroy();
}
