package org.pplm.framework.spring.dudu.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.pplm.framework.spring.dudu.utils.BusinessResponse;
import org.pplm.framework.utils.servlet.wapper.BufferedStreamHttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buffered")
public class BufferedStreamRequestHandler {
	
	@PostMapping()
	public BusinessResponse<String> onPost(HttpServletRequest httpServletRequest) throws IOException {
		HttpServletRequest bufferedStreamHttpServletRequest = new BufferedStreamHttpServletRequest(httpServletRequest);
		ServletInputStream servletInputStream = bufferedStreamHttpServletRequest.getInputStream();
		servletInputStream.mark(10);
		byte[] buffer = new byte[10];
		int length = IOUtils.read(servletInputStream, buffer);
		servletInputStream.reset();
		String buffered = new String(buffer, 0, length);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		servletInputStream = bufferedStreamHttpServletRequest.getInputStream();
		IOUtils.copyLarge(servletInputStream, byteArrayOutputStream);
		String body = byteArrayOutputStream.toString();
		return BusinessResponse.success("success", "buffered: " + buffered + ", body: " + body);
	}
}
