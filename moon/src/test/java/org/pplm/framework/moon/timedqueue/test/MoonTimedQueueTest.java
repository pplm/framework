package org.pplm.framework.moon.timedqueue.test;

import org.junit.Test;
import org.pplm.framework.moon.timedqueue.MoonTimedQueue;
import org.pplm.framework.moon.timedqueue.MoonTimedQueueConfig;

public class MoonTimedQueueTest {
	@Test
	public void test() throws InterruptedException {

		MoonTimedQueueConfig moonTimedQueueConfig = new MoonTimedQueueConfig();
		moonTimedQueueConfig.setInterval(2000);
		moonTimedQueueConfig.setProcessLimit(18);
		moonTimedQueueConfig.setProcessWaitTimes(3);
		MoonTimedQueue<Integer> moonTimedQueue = new MoonTimedQueue<>("test");
		moonTimedQueue.setConfig(moonTimedQueueConfig);
		moonTimedQueue.setProcess(elements -> {
			elements.forEach(System.out::println);
			return elements.size();
		});
		moonTimedQueue.startup();
		int i = 0;
		Thread.sleep(1500);
		while(i++ < 40) {
			Thread.sleep(300);
			moonTimedQueue.add(i);
		}
		moonTimedQueue.shutdown();
	}
}
