package com.mmkj.coccurent;

import com.google.common.util.concurrent.*;
import com.mmkj.common.utils.Logger;

import java.util.concurrent.*;

/**
 * @author Abocd
 */
public class GuavaFutureDemo {
    private static class HotWaterJob implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                Logger.info("开始烧水");
                TimeUnit.SECONDS.sleep(1);
                Logger.info("水烧好了");
            } catch (InterruptedException e) {
                Logger.error("发生异常");
                return false;
            }
            Logger.info("运行结束");
            return true;
        }
    }

    private static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                Logger.info("开始洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                Logger.info("茶壶洗好了");
            } catch (InterruptedException e) {
                Logger.error("发生异常");
                return false;
            }
            Logger.info("运行结束");
            return true;
        }
    }

    private static class ReadBookJob implements Runnable{
        boolean waterOk = false;
        boolean washOk = false;
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    Logger.info("读书中.....");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (waterOk && washOk) {
                    drinkTea(waterOk, washOk);
                }
            }
        }
    }
    private static void drinkTea(Boolean waterOk, Boolean washOk) {
        if (waterOk && washOk) {
            Logger.info("开始泡茶喝");
        } else if (!waterOk) {
            Logger.info("烧水失败");
        } else if (!washOk) {
            Logger.info("清洗失败");
        }
    }

    public static void main(String[] args) {
        ReadBookJob readBookJob = new ReadBookJob();
        Thread readBookThread = new Thread(readBookJob);
        readBookThread.setName("读书主线程");
        readBookThread.start();

        HotWaterJob hotWaterJob = new HotWaterJob();
        WashJob washJob = new WashJob();

        ExecutorService javaPool = Executors.newFixedThreadPool(10);

        ListeningExecutorService guavaPool = MoreExecutors.listeningDecorator(javaPool);
        ListenableFuture<Boolean> hotFuture = guavaPool.submit(hotWaterJob);
        Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    readBookJob.waterOk = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Logger.info("烧水失败，没有茶喝了");
            }
        });

        ListenableFuture<Boolean> washFuture = guavaPool.submit(washJob);
        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    readBookJob.washOk = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Logger.info("清洗失败，没有茶喝了");
            }
        });
    }
}
