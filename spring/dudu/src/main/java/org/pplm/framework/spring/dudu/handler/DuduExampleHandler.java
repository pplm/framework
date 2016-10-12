package org.pplm.framework.spring.dudu.handler;

import java.util.HashMap;
import java.util.Map;

import org.pplm.framework.spring.dudu.bean.DuduExampleBean;
import org.pplm.framework.spring.dudu.bean.DuduExampleBean.DuduExampleJson2Bean;
import org.pplm.framework.spring.dudu.utils.BusinessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class DuduExampleHandler {

	@GetMapping("/exception")
	public void onGetException() {
		throw new RuntimeException("example for exception");
	}

	@GetMapping("/queryparams")
	public BusinessResponse<DuduExampleBean> onGetQueryParams(@RequestParam(name = "rp1", required = false) String rp1, @RequestParam(name = "rp2", required = false) String rp2) {
		DuduExampleBean duduExampleBean = new DuduExampleBean();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("rp1", rp1);
		queryParams.put("rp2", rp2);
		duduExampleBean.setQueryParams(queryParams);
		return BusinessResponse.success(duduExampleBean);
	}
	
	@GetMapping("/pathparams")
	public BusinessResponse<?> onGetPathParams() {
		return BusinessResponse.success();
	}
	
	@GetMapping("/pathparams/{pp1}")
	public BusinessResponse<DuduExampleBean> onGetPathParams(@PathVariable(name = "pp1", required = false) String pp1) {
		DuduExampleBean duduExampleBean = new DuduExampleBean();
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("pp1", pp1);
		duduExampleBean.setPathParams(pathParams);
		return BusinessResponse.success(duduExampleBean);
	}
	
	@GetMapping("/pathparams/{pp1}/{pp2}")
	public BusinessResponse<DuduExampleBean> onGetPathParams(@PathVariable(name = "pp1", required = false) String pp1,
			@PathVariable(name = "pp2", required = false) String pp2) {
		DuduExampleBean duduExampleBean = new DuduExampleBean();
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("pp1", pp1);
		pathParams.put("pp2", pp2);
		duduExampleBean.setPathParams(pathParams);
		return BusinessResponse.success(duduExampleBean);
	}

	@PostMapping("/json2bean")
	public BusinessResponse<DuduExampleJson2Bean> onPostJson2Bean(@RequestBody DuduExampleJson2Bean duduExampleJson2Bean) {
		 return BusinessResponse.success(duduExampleJson2Bean);
	}
	
}
