package org.pplm.framework.moon;

import org.pplm.framework.utils.config.ConfigPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonConfig {
	private Logger logger = LoggerFactory.getLogger(MoonConfig.class);
	
	public final static String KEY_LOOP_TIME = "MOON_LOOP_TIME";
	public final static String KEY_INTERVAL = "MOON_INTERVAL";
	public final static String KEY_CLEAR_WAIT = "MOON_CLEAR_WAIT";
	public final static String KEY_CLEAR_WAIT_TIMES = "MOON_CLEAR_WAIT_TIMES";
	
	public final static long DEFAULT_LOOP_TIME = 1000;
	public final static long DEFAULT_INTERVAL = 60000;
	public final static long DEFAULT_CLEAR_WAIT = 2000;
	public final static int DEFAULT_CLEAR_WAIT_TIMES = 5;
	
	public final static long MIN_LOOP_TIME = 100;
	public final static long MIN_CLEAR_WAIT = 500;
	public final static int MIN_CLEAR_WAIT_TIMES = 1;
	
	long loopTime;
	long interval;
	long clearWait;
	int clearWaitTimes;
	
	public MoonConfig() {
		super();
		init();
	}

	private void init() {
		this.loopTime = ConfigPlayer.getLong(KEY_LOOP_TIME, DEFAULT_LOOP_TIME);
		this.interval = ConfigPlayer.getLong(KEY_INTERVAL, DEFAULT_INTERVAL);
		this.clearWait = ConfigPlayer.getLong(KEY_CLEAR_WAIT, DEFAULT_CLEAR_WAIT);
		this.clearWaitTimes = ConfigPlayer.getInt(KEY_CLEAR_WAIT_TIMES, DEFAULT_CLEAR_WAIT_TIMES);
	}
	
	public void validation() {
		if (loopTime < MIN_LOOP_TIME) {
			logger.warn(KEY_LOOP_TIME + " value [" + loopTime + "] less than min value [" + MIN_LOOP_TIME + "], use min value instead.");
			loopTime = MIN_LOOP_TIME;
		}
		if (interval < loopTime) {
			logger.warn(KEY_INTERVAL + " value [" + interval + "] less than loopTime [" + KEY_LOOP_TIME + "], use loopTime value [" + loopTime + "] instead.");
			interval = loopTime;
		}
		if (clearWait < MIN_CLEAR_WAIT) {
			logger.warn(KEY_CLEAR_WAIT + " value [" + clearWait + "] less than min value [" + MIN_CLEAR_WAIT + "], use min value instead.");
			clearWait = MIN_CLEAR_WAIT;
		}
		if (clearWaitTimes < MIN_CLEAR_WAIT_TIMES) {
			logger.warn(KEY_CLEAR_WAIT_TIMES + " value [" + clearWaitTimes + "] less than min value [" + MIN_CLEAR_WAIT_TIMES + "], use min value instead.");
			clearWaitTimes = MIN_CLEAR_WAIT_TIMES;
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(KEY_LOOP_TIME).append(':').append(loopTime);
		stringBuilder.append(',').append(KEY_INTERVAL).append(':').append(interval);
		stringBuilder.append(',').append(KEY_CLEAR_WAIT).append(':').append(clearWait);
		stringBuilder.append(',').append(KEY_CLEAR_WAIT_TIMES).append(':').append(clearWaitTimes);
		return stringBuilder.toString();
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public long getLoopTime() {
		return loopTime;
	}

	public void setLoopTime(long loopTime) {
		this.loopTime = loopTime;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getClearWait() {
		return clearWait;
	}

	public void setClearWait(long clearWait) {
		this.clearWait = clearWait;
	}

	public int getClearWaitTimes() {
		return clearWaitTimes;
	}

	public void setClearWaitTimes(int clearWaitTimes) {
		this.clearWaitTimes = clearWaitTimes;
	}
	
}
