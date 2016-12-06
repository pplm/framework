package org.pplm.framework.utils.servlet.filter;

import javax.servlet.FilterConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHttpTrackProcesser implements HttpTrackProcessI {

	private static Logger logger = LoggerFactory.getLogger(LogHttpTrackProcesser.class);
	
	@Override
	public void init(FilterConfig filterConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(HttpTrackBean httpTrackBean) {
		logger.info(httpTrackBean.toJsonString());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
