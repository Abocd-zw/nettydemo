package com.mmkj.nio.bufferDemo;

import java.nio.IntBuffer;
import com.mmkj.common.utils.*;

/**
 *  @author Abocd
 */
public class UseBuffer {
    private static IntBuffer intBuffer = null;

    private static void allocateTest() {
        intBuffer = IntBuffer.allocate(20);
        Logger.debug("------------after allocate------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void putTest() {
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }
        Logger.debug("------------after putTest------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void flipTest() {
        intBuffer.flip();
        Logger.debug("------------after flipTest ------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void getTest() {
        for (int i = 0; i < 2; i++) {
            int j = intBuffer.get();
            Logger.debug("j = " + j);
        }
        Logger.debug("------------after get 2 int ------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            Logger.debug("j = " + j);
        }
        Logger.debug("------------after get 3 int ------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void rewindTest() {
        intBuffer.rewind();
        Logger.debug("------------after rewind ------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }


    /**
     * rewind??????????????????
     * ???????????? mark ????????????
     */
    private static void reRead() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                intBuffer.mark();
            }
            int j = intBuffer.get();
            Logger.debug("j = " + j);

        }
        Logger.debug("------------after reRead------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void afterReset() {
        Logger.debug("------------after reset------------------");
        intBuffer.reset();
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
        for (int i =2; i < 5; i++) {
            int j = intBuffer.get();
            Logger.debug("j = " + j);
        }
    }

    private static void clearDemo() {
        Logger.debug("------------after clear------------------");
        intBuffer.clear();
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    public static void main(String[] args) {
        Logger.debug("????????????");

        allocateTest();

        Logger.debug("??????");
        putTest();

        Logger.debug("??????");

        flipTest();

        Logger.debug("??????");
        getTest();

        Logger.debug("?????????");
        rewindTest();
        reRead();

        Logger.debug("make&reset??????");
        afterReset();

        Logger.debug("??????");

        clearDemo();

    }
}


