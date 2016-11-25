package org.pplm.framework.utils.servlet.wapper;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;

/**
 * class adapter adaptes BufferedInputStream and ServletInputStream
 * 
 * @author OracleGao
 *
 */
public class BufferedServletInputStream extends ServletInputStreamWapper {

	private BufferedInputStream bufferedInputStream;

	public BufferedServletInputStream(ServletInputStream servletInputStream) {
		super(servletInputStream);
		this.bufferedInputStream = new BufferedInputStream(servletInputStream);
	}

	@Override
	public int read() throws IOException {
		return bufferedInputStream.read();
	}

	@Override
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

	@Override
    public int read(byte b[], int off, int len) throws IOException {
		return bufferedInputStream.read(b, off, len);
    }

	@Override
    public long skip(long n) throws IOException {
		return bufferedInputStream.skip(n);
    }

	@Override
    public int available() throws IOException {
        return bufferedInputStream.available();
    }

	@Override
    public void close() throws IOException {
		bufferedInputStream.close();
	}

	@Override
    public void mark(int readlimit) {
		bufferedInputStream.mark(readlimit);
	}

	@Override
    public synchronized void reset() throws IOException {
		bufferedInputStream.reset();
    }

	public boolean markSupported() {
        return bufferedInputStream.markSupported();
    }

}
