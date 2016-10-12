package org.pplm.framework.spring.dudu.bean;

import java.sql.Timestamp;

import org.springframework.beans.factory.InitializingBean;

import static org.pplm.framework.spring.dudu.utils.Constant.*;

public class DuduHealthCheckBean implements InitializingBean {
	
	public static final String HEALTH_STATUS_HEALTHY = "healthy";
	
	private long startupDateTime;
	private String status;
	
	public DuduHealthCheckBean() {
		super();
	}
	
	public String getUptime() {
		long upTime = System.currentTimeMillis() - startupDateTime;
        int day = (int) (upTime / TIME_LONG_DAY);
        long modValue = upTime % TIME_LONG_DAY;
        int hour = (int) (upTime % modValue / TIME_LONG_HOUR);
        modValue %= TIME_LONG_HOUR;
        int minute = (int) (modValue / TIME_LONG_MINUTE);
        modValue %= TIME_LONG_MINUTE;
        int second = (int) (modValue / TIME_LONG_SECOND);
        return String.format("%s days %s hours %s minutes %s seconds", day, hour, minute, second);
	}
	
	public String getStatusTime() {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.startupDateTime = System.currentTimeMillis();
		this.status = HEALTH_STATUS_HEALTHY;
	}
	
}
