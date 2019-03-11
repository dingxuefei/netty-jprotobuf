package com.iscas.common.tools.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by zqm on 2018/8/30.
 * 基于生产者消费者模型的线程池服务类
 */
public class ConsumerThreadPoolService  {
	private ConcurrentLinkedQueue<Object> taskQueue;

	public ConsumerThreadPoolService() {
		taskQueue = new ConcurrentLinkedQueue<Object>();
	}

	public ConsumerThreadPoolService(int size, Class<? extends BaseConsumer> consumer) {
		this();
		registerConsumer(size,consumer);
	}


	/**
	 * @param size    线程池大小
	 * @param consumer 消费者实现类
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void registerConsumer(int size, Class<? extends BaseConsumer> consumer) {
		ExecutorService pool = Executors.newFixedThreadPool(size);

		try {
			for (int i = 0; i < size; i++) {
				BaseConsumer baseConsumer = consumer.newInstance();
				baseConsumer.setTaskQueue(taskQueue);
				pool.submit(baseConsumer);
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册消费者
	 * @param size 线程池大小，即消费者个数
	 * @param baseConsumer 消费者实现类
	 */
	public void registerConsumer(int size, BaseConsumer baseConsumer) {
		ExecutorService pool = Executors.newFixedThreadPool(size);

		try {
			for (int i = 0; i < size; i++) {
				baseConsumer.setTaskQueue(taskQueue);
				pool.submit(baseConsumer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生产者，提交任务
	 * @param task
	 */
	public void addTask(Object task) {
		taskQueue.offer(task);
	}
}
