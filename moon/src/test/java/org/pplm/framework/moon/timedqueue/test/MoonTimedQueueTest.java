package org.pplm.framework.moon.timedqueue.test;

import org.junit.Test;
import org.pplm.framework.moon.timedqueue.MoonTimedQueue;
import org.pplm.framework.moon.timedqueue.MoonTimedQueueConfig;

public class MoonTimedQueueTest {
	@Test
	public void test() throws InterruptedException {

		MoonTimedQueueConfig moonTimedQueueConfig = new MoonTimedQueueConfig();
		moonTimedQueueConfig.setInterval(5000);
		moonTimedQueueConfig.setProcessLimit(23);
		moonTimedQueueConfig.setProcessWaitTimes(3);
		MoonTimedQueue<Integer> moonTimedQueue = new MoonTimedQueue<>("test");
		moonTimedQueue.setConfig(moonTimedQueueConfig);
		moonTimedQueue.startup();
		int i = 0;
		Thread.sleep(12000);
		while(i++ < 97) {
			Thread.sleep(600);
			moonTimedQueue.add(i);
		}
		moonTimedQueue.clear();
	}
}
