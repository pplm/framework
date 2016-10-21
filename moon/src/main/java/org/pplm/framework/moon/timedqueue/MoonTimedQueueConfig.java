package org.pplm.framework.moon.timedqueue;

import java.io.IOException;

import org.pplm.framework.moon.MoonConfig;
import org.pplm.framework.utils.config.ConfigPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonTimedQueueConfig extends MoonConfig {

	private static Logger logger = LoggerFactory.getLogger(MoonTimedQueueConfig.class);

	public static String DEFALUT_CONFIG_PROPERTIES_FILE = "mtq.properties";

	static {
		/**
		 * support lower-point-lower key format
		 */
		ConfigPlayer.getInstance().addKeyFormatSupport(ConfigPlayer.KeyFormatSupport.LPL);
		try {
			ConfigPlayer.addProperties(DEFALUT_CONFIG_PROPERTIES_FILE);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public final static String KEY_PROCESS_LIMIT = "MOON_TIMED_QUEUE_PROCESS_LIMIT";
	public final static String KEY_INIT_BUFFER_SIZE = "MOON_TIMED_QUEUE_INIT_BUFFER_SIZE";
	public final static String KEY_PROCESS_WAIT_TIMES = "MOON_TIMED_QUEUE_PROCESS_WAIT_TIMES";

	public final static int DEFAULT_PROCESS_LIMIT = 0;
	public final static int DEFAULT_INIT_BUFFER_SIZE = 1000;
	public final static int DEFAULT_PROCESS_WAIT_TIMES = 5;

	int processLimit;
	int initBufferSize;
	int processWaitTimes;

	private boolean validated;

	public MoonTimedQueueConfig() {
		super();
		init();
	}

	public MoonTimedQueueConfig(MoonTimedQueueConfig config) {
		super(config);
		processLimit = config.processLimit;
		initBufferSize = config.initBufferSize;
		processWaitTimes = config.processWaitTimes;
		validated = config.validated;
	}

	public MoonTimedQueueConfig(MoonConfig config) {
		super(config);
		init();
	}

	private void init() {
		processLimit = ConfigPlayer.getInt(KEY_PROCESS_LIMIT, DEFAULT_PROCESS_LIMIT);
		initBufferSize = ConfigPlayer.getInt(KEY_INIT_BUFFER_SIZE, DEFAULT_INIT_BUFFER_SIZE);
		processWaitTimes = ConfigPlayer.getInt(KEY_PROCESS_WAIT_TIMES, DEFAULT_PROCESS_WAIT_TIMES);
	}

	@Override
	public void validation() {
		super.validation();
		if (!validated) {
			if (processLimit < 0) {
				logger.warn(KEY_PROCESS_LIMIT + " value should be greater than 0, use default value ["
						+ DEFAULT_PROCESS_LIMIT + "] instead.");
				this.processLimit = 0;
			}
			if (initBufferSize <= 0) {
				logger.warn(KEY_INIT_BUFFER_SIZE + " value should be greater equal than 0, use default value ["
						+ DEFAULT_INIT_BUFFER_SIZE + "] instead.");
			}
			if (processWaitTimes < 0) {
				logger.warn(KEY_PROCESS_WAIT_TIMES + " value should be greater than 0, use default value ["
						+ DEFAULT_PROCESS_WAIT_TIMES + "] instead.");
			}
			validated = true;
		}
	}

	public int getProcessLimit() {
		return processLimit;
	}

	public void setProcessLimit(int processLimit) {
		this.processLimit = processLimit;
	}

	public int getInitBufferSize() {
		return initBufferSize;
	}

	public void setInitBufferSize(int initBufferSize) {
		this.initBufferSize = initBufferSize;
	}

	public int getProcessWaitTimes() {
		return processWaitTimes;
	}

	public void setProcessWaitTimes(int waitTimes) {
		this.processWaitTimes = waitTimes;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		stringBuilder.append(',').append(KEY_PROCESS_LIMIT).append(':').append(processLimit);
		stringBuilder.append(',').append(KEY_INIT_BUFFER_SIZE).append(':').append(initBufferSize);
		stringBuilder.append(',').append(KEY_PROCESS_WAIT_TIMES).append(':').append(processWaitTimes);
		return stringBuilder.toString();
	}

}
