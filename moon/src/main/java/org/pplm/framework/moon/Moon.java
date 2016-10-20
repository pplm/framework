package org.pplm.framework.moon;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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

	public Moon(MoonConfig config) {
		super();
		this.config = config;
	}

	public Moon(String name, MoonConfig config) {
		super();
		this.name = name;
		this.config = config;
	}

	protected abstract void moonInit();

	protected abstract void moonExecute();

	protected abstract void moonClear();

	@PostConstruct
	public final boolean init() {
		if (status != MoonStatus.NEW) {
			logger.warn("moon [" + name + "] is not NEW status, init failed");
			return false;
		}
		status = MoonStatus.INITIALIZING;
		if (name == null) {
			name = UUID.randomUUID().toString();
		}
		moonInit();
		if (config == null) {
			config = new MoonConfig();
		}
		config.validation();
		moonThread = new Thread(this::moonRun, name);
		status = MoonStatus.READY;
		logger.info("moon [" + name + "] config [" + config.toString() + "]");
		logger.info("moon [" + name + "] initialize success");
		return true;
	}

	public final boolean startup() {
		while (true) {
			switch (status) {
			case NEW:
				logger.warn("moon [" + name + "] is not initialized, begin to initialize");
				init();
				break;
			case INITIALIZING:
				int times = 0;
				while (times++ <= 3 && status != MoonStatus.READY) {
					logger.warn("moon [" + name + "] is initializing, wait 1 seconds");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
				if (status != MoonStatus.READY) {
					throw new RuntimeException("moon [" + name + "] initialize failed");
				}
				break;
			case READY:
				moonThread.start();
				return true;
			case RUNNING:
				logger.warn("moon [" + name + "] has started up");
				return false;
			case STOPPING:
				logger.warn("moon [" + name + "] is stopping");
				return false;
			case STOPPED:
				logger.warn("moon [" + name + "] has stopped");
				return false;
			case CLEARED:
				logger.warn("moon [" + name + "] has cleared");
				return false;
			}
		}
	}

	@PreDestroy
	public final boolean clear() {
		if (status != MoonStatus.RUNNING) {
			logger.warn("moon [" + name + "] is not RUNNING status, clear failed");
			return false;
		}
		logger.info("moon [" + name + "] clear begin");
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
			logger.info("moon [" + name + "] stopping wait times [" + clearWaitTimes + "]");
		}
		moonClear();
		if (status != MoonStatus.STOPPED) {
			logger.error("moon [" + name + "] stopping timeout, wait times [" + clearWaitTimes + "], clear failed");
			return false;
		} else {
			this.status = MoonStatus.CLEARED;
			logger.info("moon [" + name + "] clear finish");
		}
		return true;
	}

	private void moonRun() {
		long loopTime = config.loopTime;
		long interval = config.interval;
		long timeCount = 0;
		status = MoonStatus.RUNNING;
		logger.info("moon [" + name + "] start up");
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
		logger.info("moon [" + name + "] stopped");
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
