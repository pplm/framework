package org.pplm.framework.utils.servlet.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogHttpSessionTracker implements HttpSessionTrackI<HttpSessionTrackBean> {

	private static Logger logger = LoggerFactory.getLogger(LogHttpSessionTracker.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public HttpSessionTrackBean track(HttpSession httpSession) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> keys = Collections.list(httpSession.getAttributeNames());
		for (String key : keys) {
			map.put(key, httpSession.getAttribute(key));
		}
		try {
			logger.info(objectMapper.writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("never throw this exception", e);
		}
		return null;
	}

	@Override
	public void destroy() {}

}
