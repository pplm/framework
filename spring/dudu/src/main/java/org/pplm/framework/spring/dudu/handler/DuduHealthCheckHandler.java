package org.pplm.framework.spring.dudu.handler;

import javax.annotation.Resource;

import org.pplm.framework.spring.dudu.bean.DuduHealthCheckBean;
import org.pplm.framework.spring.dudu.utils.BusinessResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class DuduHealthCheckHandler {
	
	@Resource
	private DuduHealthCheckBean duduHealthCheckBean;
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public BusinessResponse<DuduHealthCheckBean> onGetHealthCheck() {
		return BusinessResponse.success(duduHealthCheckBean);
	}
	
}
