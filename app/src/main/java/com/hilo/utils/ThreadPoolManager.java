package com.hilo.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 * Hilo
 */
public class ThreadPoolManager {
    /** 线程池所对应的代理对象 */
    private static ThreadPoolProxt mThreadPoolProxt;
    /** 同步线程锁 */
    private static Object object = new Object();

    /** 获取线程池的代理对象 */
    public static ThreadPoolProxt getPoolProxy(){
        synchronized (object) {
            if (mThreadPoolProxt == null) {
                /** 最大支持5个线程 */
                mThreadPoolProxt = new ThreadPoolProxt(5,5,5L);
            }
        }
        return mThreadPoolProxt;
    }

    /** 线程池内部类 */
    public static class ThreadPoolProxt {
        /** 核心线程数 */
        private int corePoolSize;
        /** 最大线程数 */
        private int maximumPoolSize;
        /** 线程不干活在池里面存活的时间 */
        private long keepAliveTime;
        private ThreadPoolExecutor threadPoolExecutor;
        /**
         * 线程池的构造方法
         */
        public ThreadPoolProxt(int corePoolSize,int maximumPoolSize,long keepAliveTime ) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /** 执行任务的方法 */
        public void exexute(Runnable runnable) {
            if (runnable == null) {
                return;
            } else {
                if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                    threadPoolExecutor = new ThreadPoolExecutor(
                    //核心线程数
                    corePoolSize,
                    //最大线程数
                    maximumPoolSize,
                    //线程不干活在池里面存活的时间
                    keepAliveTime,
                    //时间的格式 秒
                    TimeUnit.MILLISECONDS,
                    //任务多的时候排队的队列
                    new LinkedBlockingQueue<Runnable>(),
                    //获取默认的线程工厂的方法
                    Executors.defaultThreadFactory(),
                    //超过最大线程数时,异常处理类
                    new ThreadPoolExecutor.AbortPolicy());
                }
				/** 执行任务 */
                threadPoolExecutor.execute(runnable);
            }
        }
    }
}