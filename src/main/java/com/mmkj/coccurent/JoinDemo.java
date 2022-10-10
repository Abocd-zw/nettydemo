package com.mmkj.coccurent;

import com.mmkj.common.utils.Logger;
import java.util.concurrent.TimeUnit;

/**
 *  使用join实现异步泡茶喝
 *  @author Abocd
 */
public class JoinDemo {
    static class HotWaterThread extends Thread{
        public HotWaterThread() {
            super("烧水-Thread");
        }
        @Override
        public void run() {
            try {
                Logger.info("开始烧水");
                TimeUnit.SECONDS.sleep(1);
                Logger.info("水开了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info("运行结束");
        }
    }

    static class WashThread extends Thread{
        public WashThread() {
            super("清洗-Thread");
        }
        @Override
        public void run() {
            try {
                Logger.info("开始洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                Logger.info("茶壶洗碗了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info("运行结束");
        }
    }

    public static void main(String[] args) {
        HotWaterThread hotWaterThread = new HotWaterThread();
        WashThread washThread = new WashThread();

        hotWaterThread.start();
        washThread.start();

        try {
            hotWaterThread.join();
            washThread.join();
            Logger.info("泡茶喝");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.error("发生异常");
        }
        Logger.info(Thread.currentThread().getName() + "执行结束");
    }
}
