package com.mmkj.coccurent;

import com.mmkj.common.utils.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureDemo {
    private static class HotWarterJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
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

    private static class WashCapJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
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
        HotWarterJob hotWarterJob = new HotWarterJob();
        FutureTask<Boolean> futureTask = new FutureTask<>(hotWarterJob);
        Thread thread = new Thread(futureTask);

        WashCapJob washCapJob = new WashCapJob();
        FutureTask<Boolean> futureTask1 = new FutureTask<>(washCapJob);
        Thread thread1 = new Thread(futureTask1);

        thread.start();
        thread1.start();

        Thread.currentThread().setName("主线程");

        try {
            Boolean aBoolean = futureTask.get();
            Boolean aBoolean1 = futureTask1.get();
            drinkTea(aBoolean,aBoolean1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
