package org.pplm.framework.utils.servlet.filter;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author OracleGao
 *
 */
public class HttpTrackBean {

	private String url;
	private String method;
	private Map<String, String[]> parameters;
	private String ClientIp;
	private HttpSessionTrackBean httpSession;
	
	private Map<String, String> headersRequest;
	private String encodingRequest;
	private String contentTypeRequest;
	private String bodyClipRequest;
	private long trackTimeRequest;

	private Map<String, String> headersResponse;
	private String encodingResponse;
	private String contentTypeResponse;
	private String bodyClipResponse;
	private long trackTimeResponse;
	private int statusResponse;
	
	public HttpTrackBean() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	public String getClientIp() {
		return ClientIp;
	}

	public void setClientIp(String clientIp) {
		ClientIp = clientIp;
	}

	public HttpSessionTrackBean getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSessionTrackBean httpSession) {
		this.httpSession = httpSession;
	}

	public Map<String, String> getHeadersRequest() {
		return headersRequest;
	}

	public void setHeadersRequest(Map<String, String> headersRequest) {
		this.headersRequest = headersRequest;
	}

	public String getEncodingRequest() {
		return encodingRequest;
	}

	public void setEncodingRequest(String encodingRequest) {
		this.encodingRequest = encodingRequest;
	}

	public String getContentTypeRequest() {
		return contentTypeRequest;
	}

	public void setContentTypeRequest(String contentTypeRequest) {
		this.contentTypeRequest = contentTypeRequest;
	}

	public String getBodyClipRequest() {
		return bodyClipRequest;
	}

	public void setBodyClipRequest(String bodyClipRequest) {
		this.bodyClipRequest = bodyClipRequest;
	}

	public long getTrackTimeRequest() {
		return trackTimeRequest;
	}

	public void setTrackTimeRequest(long trackTimeRequest) {
		this.trackTimeRequest = trackTimeRequest;
	}

	public Map<String, String> getHeadersResponse() {
		return headersResponse;
	}

	public void setHeadersResponse(Map<String, String> headersResponse) {
		this.headersResponse = headersResponse;
	}

	public String getEncodingResponse() {
		return encodingResponse;
	}

	public void setEncodingResponse(String encodingResponse) {
		this.encodingResponse = encodingResponse;
	}

	public String getContentTypeResponse() {
		return contentTypeResponse;
	}

	public void setContentTypeResponse(String contentTypeResponse) {
		this.contentTypeResponse = contentTypeResponse;
	}

	public String getBodyClipResponse() {
		return bodyClipResponse;
	}

	public void setBodyClipResponse(String bodyClipResponse) {
		this.bodyClipResponse = bodyClipResponse;
	}

	public long getTrackTimeResponse() {
		return trackTimeResponse;
	}

	public void setTrackTimeResponse(long trackTimeResponse) {
		this.trackTimeResponse = trackTimeResponse;
	}

	public int getStatusResponse() {
		return statusResponse;
	}

	public void setStatusResponse(int statusResponse) {
		this.statusResponse = statusResponse;
	}

	public String toJsonString() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("never throw this exception", e);
		}
	}
	
}
