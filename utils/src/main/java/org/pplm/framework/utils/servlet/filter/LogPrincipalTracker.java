package org.pplm.framework.utils.servlet.filter;

import java.security.Principal;

import javax.servlet.FilterConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogPrincipalTracker implements PrincipalTrack<PrincipalTrackBeanable> {
	private static Logger logger = LoggerFactory.getLogger(LogPrincipalTracker.class);
	
	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void destroy() {}

	@Override
	public PrincipalTrackBeanable track(Principal principal) {
		if (principal != null) {
			logger.info("principal name [" + principal.getName()  + "]");
		}
		return null;
	}

}
