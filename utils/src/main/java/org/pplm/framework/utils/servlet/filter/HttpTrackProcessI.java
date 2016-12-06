package org.pplm.framework.utils.servlet.filter;

import javax.servlet.FilterConfig;
/**
 * 
 * @author OracleGao
 *
 */
public interface HttpTrackProcessI {
	public void init(FilterConfig filterConfig);
	
	public void process(HttpTrackBean httpTrackBean);
	
	public void destroy();
}
