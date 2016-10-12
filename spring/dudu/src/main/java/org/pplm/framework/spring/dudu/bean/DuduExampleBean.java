package org.pplm.framework.spring.dudu.bean;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DuduExampleBean {
	private Map<String, String> queryParams;
	private Map<String, String> pathParams;
	
	public DuduExampleBean() {
		super();
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}
	
	@JsonInclude(Include.NON_NULL)
	public static class DuduExampleJson2Bean {
		private String example;

		public DuduExampleJson2Bean() {
			super();
		}

		public String getExample() {
			return example;
		}

		public void setExample(String example) {
			this.example = example;
		}
		
	}
	
}
