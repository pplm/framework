package org.pplm.framework.utils.servlet.wapper;

import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class ServletInputStreamWapper extends ServletInputStream {

	private ServletInputStream servletInputStream;
	
	public ServletInputStreamWapper(ServletInputStream servletInputStream) {
		this.servletInputStream = servletInputStream;
	}
	
	@Override
	public int read() throws IOException {
		return servletInputStream.read();
	}

	@Override
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

	@Override
    public int read(byte b[], int off, int len) throws IOException {
		return servletInputStream.read(b, off, len);
    }

	@Override
    public long skip(long n) throws IOException {
		return servletInputStream.skip(n);
    }

	@Override
    public int available() throws IOException {
        return servletInputStream.available();
    }

	@Override
    public void close() throws IOException {
		servletInputStream.close();
	}

	@Override
    public void mark(int readlimit) {
		servletInputStream.mark(readlimit);
	}

	@Override
    public synchronized void reset() throws IOException {
		servletInputStream.reset();
    }

	public boolean markSupported() {
        return servletInputStream.markSupported();
    }
	
	@Override
	public boolean isFinished() {
		return servletInputStream.isFinished();
	}

	@Override
	public boolean isReady() {
		return servletInputStream.isReady();
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		servletInputStream.setReadListener(readListener);
	}

}
