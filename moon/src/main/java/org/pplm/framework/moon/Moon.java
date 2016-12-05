package org.pplm.framework.moon;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Moon {
	private static Logger logger = LoggerFactory.getLogger(Moon.class);

	public enum MoonStatus {
		NEW("NEW"), INITIALIZING("INITIALIZING"), READY("READY"), RUNNING("RUNNING"), STOPPING("STOPPING"), STOPPED(
				"STOPPED"), CLEARED("CLEARED");

		MoonStatus(String name) {
			value = name;
		}

		String value;
	}

	private MoonConfig config;

	private Thread moonThread;

	protected String name;
	protected MoonStatus status = MoonStatus.NEW;

	public Moon() {
		super();
	}

	public Moon(String name) {
		super();
		this.name = name;
	}

	protected abstract void moonInit();

	protected abstract void moonExecute();

	protected abstract void moonClear();

	public final boolean init() {
		if (name == null) {
			name = UUID.randomUUID().toString();
		}
		if (status != MoonStatus.NEW) {
			logger.warn("[" + name + "] is not [" + MoonStatus.NEW + "] status, init failed");
			return false;
		}
		status = MoonStatus.INITIALIZING;
		moonInit();
		if (config == null) {
			config = new MoonConfig();
		}
		config.validation();
		moonThread = new Thread(this::moonRun, name);
		status = MoonStatus.READY;
		logger.info("[" + name + "] config [" + config.toString() + "]");
		logger.info("[" + name + "] initialize success");
		return true;
	}

	public final boolean startup() {
		while (true) {
			switch (status) {
			case NEW:
				logger.info("[" + name + "] is uninitialized, begin to initialize");
				init();
				break;
			case INITIALIZING:
				int times = 0;
				while (times++ <= 3 && status != MoonStatus.READY) {
					logger.warn("[" + name + "] is initializing, wait 1 seconds");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
				if (status != MoonStatus.READY) {
					throw new RuntimeException("[" + name + "] initialize failed");
				}
				break;
			case READY:
				moonThread.start();
				return true;
			case RUNNING:
				logger.warn("[" + name + "] has started up");
				return false;
			case STOPPING:
				logger.warn("[" + name + "] is stopping");
				return false;
			case STOPPED:
				logger.warn("[" + name + "] has stopped");
				return false;
			case CLEARED:
				logger.warn("[" + name + "] has cleared");
				return false;
			}
		}
	}

	public final boolean shutdown() {
		if (status != MoonStatus.RUNNING) {
			logger.warn("[" + name + "] is not RUNNING status, shutdown failed");
			return false;
		}
		logger.info("[" + name + "] shutdown, begin to clear");
		status = MoonStatus.STOPPING;
		int clearWaitTimes = 0;
		while (clearWaitTimes++ < config.clearWaitTimes) {
			try {
				Thread.sleep(config.clearWait);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			if (status == MoonStatus.STOPPED) {
				break;
			}
			logger.info("[" + name + "] stopping wait times [" + clearWaitTimes + "]");
		}
		moonClear();
		if (status != MoonStatus.STOPPED) {
			logger.error("[" + name + "] stopping timeout, wait times [" + clearWaitTimes + "], clear failed");
			return false;
		} else {
			this.status = MoonStatus.CLEARED;
			logger.info("[" + name + "] finish to clear, shutdown successfully");
		}
		return true;
	}

	private void moonRun() {
		long loopTime = config.loopTime;
		long interval = config.interval;
		long timeCount = 0;
		status = MoonStatus.RUNNING;
		logger.info("[" + name + "] startup");
		logger.info("[" + name + "] thread begin");
		while (true) {
			try {
				Thread.sleep(loopTime);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
				break;
			}
			if (status == MoonStatus.STOPPING) {
				break;
			}
			timeCount += loopTime;
			if (timeCount >= interval) {
				moonExecute();
				timeCount = 0;
			}
			if (status == MoonStatus.STOPPING) {
				break;
			}
		}
		status = MoonStatus.STOPPED;
		logger.info("[" + name + "] thread stopped");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status.value;
	}

	protected MoonConfig getConfig() {
		return config;
	}

	protected void setConfig(MoonConfig config) {
		this.config = config;
	}

}
