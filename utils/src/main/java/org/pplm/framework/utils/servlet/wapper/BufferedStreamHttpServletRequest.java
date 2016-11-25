package org.pplm.framework.utils.servlet.wapper;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * 
 * @author OracleGao
 *
 */
public class BufferedStreamHttpServletRequest extends HttpServletRequestWrapper {
	
	private BufferedServletInputStream bufferedServletInputStreamWapper;
	
	public BufferedStreamHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.bufferedServletInputStreamWapper = new BufferedServletInputStream(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return bufferedServletInputStreamWapper;
	}
	
}
