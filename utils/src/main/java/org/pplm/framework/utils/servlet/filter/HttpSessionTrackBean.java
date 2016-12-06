package org.pplm.framework.utils.servlet.filter;

/**
 * 
 * @author OracleGao
 *
 */
public class HttpSessionTrackBean {
	private String userId;
	private String userName;
	
	public HttpSessionTrackBean() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
