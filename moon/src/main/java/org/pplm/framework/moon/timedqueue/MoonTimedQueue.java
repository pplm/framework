package org.pplm.framework.moon.timedqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.pplm.framework.moon.Moon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonTimedQueue<T> extends Moon {

	private static Logger logger = LoggerFactory.getLogger(MoonTimedQueue.class);

	private List<T> buffer;
	private List<T> processingBuffer;

	private Function<List<T>, Integer> process;

	private MoonTimedQueueConfig config;

	private int processingCount;
	private Count count;

	private int size;
	private int waitTimes;
	private int processResultTemp;

	public MoonTimedQueue() {
		super();
	}

	public MoonTimedQueue(String name) {
		super(name);
	}

	@Override
	protected void moonInit() {
		count = new Count();
		this.waitTimes = 0;
		if (config == null) {
			config = new MoonTimedQueueConfig();
			super.setConfig(config);
		}
		buffer = initBuffer();
	}

	public void add(T e) {
		if (status != MoonStatus.READY && status != MoonStatus.RUNNING) {
			throw new RuntimeException("[" + name + "] should be [READY] or [RUNNING] status");
		}
		this.buffer.add(e);
	}
	
	public void addAll(Collection<T> c) {
		if (status != MoonStatus.READY && status != MoonStatus.RUNNING) {
			throw new RuntimeException("[" + name + "] should be [" + MoonStatus.READY + "] or [" + MoonStatus.RUNNING + "] status");
		}
		this.buffer.addAll(c);
	}

	public int unprocessedCount() {
		return buffer.size();
	}

	public int processingCount() {
		return this.processingCount;
	}

	public long processedCount() {
		return this.count.processed;
	}

	public long successCount() {
		return this.count.success;
	}

	public long failedCount() {
		return this.count.failed;
	}

	public Count count() {
		return new Count(this.count);
	}

	@Override
	protected void moonExecute() {
		size = buffer.size();
		if (size > 0) {
			if (size > config.processLimit) {
				if (config.processLimit > 0) {
					logger.info("[{}] buffer size [{}] is over the process limit [{}], begin to process", name, size,
							config.processLimit);
				}
				processing();
				waitTimes = 0;
			} else {
				if (++waitTimes >= config.processWaitTimes) {
					logger.info("[{}] process wait times up [{}], begin to process", name, waitTimes);
					processing();
					waitTimes = 0;
				} else {
					logger.info(
							"[{}] buffer size [{}] is less than the process limit [{}] and proess wait times [{}] less than the limit [{}], skip the process",
							name, size, config.processLimit, waitTimes, config.processWaitTimes);
				}
			}
		} else {
			logger.info("[{}] buffer is empty, skip the process", name);
		}
	}

	private void processing() {
		processingBuffer = buffer;
		buffer = initBuffer();
		processingCount = processingBuffer.size();
		processResultTemp = process.apply(processingBuffer);
		processingBuffer = null;
		count.addProcessed(processingCount);
		count.addSuccess(processResultTemp);
		count.addFailed(processingCount - processResultTemp);
		logger.info("[{}] buffer processed [{}] success [{}] failed [{}]", name, processingCount, processResultTemp,
				processingCount - processResultTemp);
	}

	@Override
	protected void moonClear() {
		processing();
		logger.info("[{}] buffer final processed [{}] success [{}] failed [{}]", name, count.processed, count.success,
				count.failed);
	}

	public Function<List<T>, Integer> getProcess() {
		return process;
	}

	public void setProcess(Function<List<T>, Integer> process) {
		this.process = process;
	}

	private List<T> initBuffer() {
		return Collections.synchronizedList(new ArrayList<>(config.initBufferSize));
	}

	public MoonTimedQueueConfig getConfig() {
		return new MoonTimedQueueConfig(config);
	}

	public void setConfig(MoonTimedQueueConfig config) {
		config.validation();
		super.setConfig(config);
		this.config = config;
	}

	public static class Count {

		private long processed;
		private long success;
		private long failed;

		private Count() {
			super();
		}

		private Count(Count count) {
			super();
			this.processed = count.processed;
			this.success = count.success;
			this.failed = count.failed;
		}

		public long getProcessed() {
			return processed;
		}

		public void setProcessed(long processed) {
			this.processed = processed;
		}

		public void addProcessed(long processed) {
			this.processed += processed;
		}

		public long getSuccess() {
			return success;
		}

		public void setSuccess(long success) {
			this.success = success;
		}

		public void addSuccess(long success) {
			this.success += success;
		}

		public long getFailed() {
			return failed;
		}

		public void setFailed(long failed) {
			this.failed = failed;
		}

		public void addFailed(long failed) {
			this.failed += failed;
		}
	}

}
