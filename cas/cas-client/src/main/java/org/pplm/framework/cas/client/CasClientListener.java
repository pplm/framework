package org.pplm.framework.cas.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.pplm.framework.utils.config.ConfigPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author OracleGao
 *
 */
public class CasClientListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(CasClientListener.class);
	
	public static final String DEFAULT_SSL_PROPERTIES_FILE = "cas-client-ssl.properties"; 
	
	public static final String DEFAULT_CAS_SSL_TRUST_STORE = "k2data-cas.truststore";
	public static final String DEFAULT_CAS_SSL_TRUST_STORE_PASSWORD = "K2Data-20161128";
	
	public static final String PROPERTIES_KEY_CAS_SSL_TRUST_STORE = "cas.ssl.trust.store";
	public static final String PROPERTIES_KEY_CAS_SSL_TRUST_STORE_PASSWORD = "cas.ssl.trust.store.password";
	
	public static final int BUFFER_SIZE = 1024;
	
	private SingleSignOutHttpSessionListener singleSignOutHttpSessionListener;
	
	public CasClientListener() {
		super();
		init();
	}

	private void init() {
		
		try {
			ConfigPlayer.addProperties(DEFAULT_SSL_PROPERTIES_FILE, Constant.DEFAULT_PROPERTIES_FILE_ENCODING);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
		String casSslTrustStore = ConfigPlayer.getString(PROPERTIES_KEY_CAS_SSL_TRUST_STORE, DEFAULT_CAS_SSL_TRUST_STORE);
		String casSslTrustStorePassword = ConfigPlayer.getString(PROPERTIES_KEY_CAS_SSL_TRUST_STORE_PASSWORD, DEFAULT_CAS_SSL_TRUST_STORE_PASSWORD);
	
		System.setProperty("javax.net.ssl.trustStore", prepareTrustStore(casSslTrustStore));
		System.setProperty("javax.net.ssl.trustStorePassword", casSslTrustStorePassword);
		
		singleSignOutHttpSessionListener = new SingleSignOutHttpSessionListener();
	}
	
	private String prepareTrustStore(String casSslTrustStore) {
		File file = new File(casSslTrustStore);
		if (file.exists() && file.isFile()) {
			return casSslTrustStore;
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(casSslTrustStore);
			if (inputStream == null) {
				throw new RuntimeException("trust store file [" + casSslTrustStore + "] not exists");
			}
			if (!inputStream.markSupported()) {
				inputStream = new BufferedInputStream(inputStream);
			}
			byte[] buffer = new byte[BUFFER_SIZE];
			inputStream.mark(BUFFER_SIZE);
			int size = IOUtils.read(inputStream, buffer);
			String trustStoreName =System.getProperty("java.io.tmpdir") + "/" + Utils.getSha1(ArrayUtils.subarray(buffer, 0, size));
			file = new File(trustStoreName);
			if (file.exists()) {
				logger.debug("find trust store file [" + trustStoreName + "]");
			} else {
				logger.debug("not find trust store file [" + trustStoreName + "], generate it");
				inputStream.reset();
				outputStream = new FileOutputStream(trustStoreName);
				IOUtils.copyLarge(inputStream, outputStream);
				outputStream.flush();
			}
			return trustStoreName;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		singleSignOutHttpSessionListener.sessionCreated(httpSessionEvent);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		singleSignOutHttpSessionListener.sessionDestroyed(httpSessionEvent);
	}
	
}
