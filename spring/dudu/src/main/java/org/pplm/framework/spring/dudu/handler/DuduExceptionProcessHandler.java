package org.pplm.framework.spring.dudu.handler;

import org.pplm.framework.spring.dudu.utils.BusinessResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DuduExceptionProcessHandler {
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public BusinessResponse<?> onException(Exception e) {
		return BusinessResponse.failure(e.getMessage());
	}
	
}
