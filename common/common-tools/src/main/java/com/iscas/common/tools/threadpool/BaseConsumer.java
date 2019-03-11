package com.iscas.common.tools.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ISCAS on 2016/12/8.
 */
public abstract class BaseConsumer implements Runnable {
	private ConcurrentLinkedQueue<Object> taskQueue;

	public ConcurrentLinkedQueue<Object> getTaskQueue() {
		return taskQueue;
	}

	public void setTaskQueue(ConcurrentLinkedQueue<Object> taskQueue) {
		this.taskQueue = taskQueue;
	}

	@Override
    public void run() {
		while (true) {
			if (taskQueue.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				Object task = taskQueue.poll();
				if (task != null) {
					consume(task);
				}
//				if(log.isDebugEnabled()) {
//					log.debug(Thread.currentThread().getId()+"->consume one task");
//				}
			}
		}
	}

	public abstract void consume(Object task);
}
