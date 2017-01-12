package org.pplm.framework.cas.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author OracleGao
 *
 */
public class SignOutFilter implements Filter {

	private String appServerSingoutUrl;
	private String casServerLogoutUrl;
	private URI logoutUri;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		appServerSingoutUrl = filterConfig.getInitParameter(CasClientFilter.KEY_APP_SERVER_LOGOUT_URL);
		casServerLogoutUrl = filterConfig.getInitParameter(CasClientFilter.KEY_CAS_SERVER_LOGOUT_URL);
		try {
			logoutUri = new URI(appServerSingoutUrl);
		} catch (URISyntaxException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if (logoutUri.getPath().equals(httpServletRequest.getRequestURI())) {
			httpServletRequest.getSession().invalidate();
			httpServletResponse.sendRedirect(casServerLogoutUrl);
		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	@Override
	public void destroy() {}

}
