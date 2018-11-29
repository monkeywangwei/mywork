package com.aia.eservice.common.utility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtils {
	private ThreadPoolUtils() {
	}
     //核心线程数
	public static int CORE_POOL_SIZE = 200;
     //最大线程数
	public static int MAX_POOL_SIZE = Integer.MAX_VALUE;
	//线程存活时间
	public static int KEEP_ALIVE_TIME = 10000;
	//阻塞队列
	private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
	
	private static ThreadPoolExecutor threadPool;
	
	private static ThreadFactory threadFactory = new ThreadFactory() {
		//递增
		private final AtomicInteger integer = new AtomicInteger();
		public Thread newThread(Runnable r) {
			return new Thread(r, "myThreadPool thread:"	+ integer.getAndIncrement());
		}
	};

	static 
	{
		threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
	}

	public static void execute(Runnable runnable) {
		threadPool.execute(runnable);
	}
}
