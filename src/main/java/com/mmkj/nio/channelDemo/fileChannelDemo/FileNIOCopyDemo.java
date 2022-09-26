package com.mmkj.nio.channelDemo.fileChannelDemo;

import com.mmkj.nio.NioDemoConfig;
import com.mmkj.common.utils.IOUtil;
import com.mmkj.common.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  演示文件通道及字节缓冲区的使用
 *  @author Abocd
 */
public class FileNIOCopyDemo {


    public static void main(String[] args) {
        //演示复制资源文件
        nioCopyResourceFile();
    }


    /**
     * 复制两个资源目录下的文件
     */
    private static void nioCopyResourceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        Logger.debug("srcPath=" + srcPath);

        String destShortPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destDecodePath = IOUtil.builderResourcePath(destShortPath);
        Logger.debug("destDecodePath=" + destDecodePath);

        nioCopyFile(srcPath, destDecodePath);
    }


    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            long startTime = System.currentTimeMillis();
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outChannel = fos.getChannel();

                int length = -1;
                // 新建的Buffer默认是写入模式，可以直接用作inChannel.read(buf)的参数
                ByteBuffer buf = ByteBuffer.allocate(1024);
                //将从输入通道读取到的数据写入到ByteBuffer中
                while ((length = inChannel.read(buf)) != -1) {
                    // 翻转buf,变成读模式
                    buf.flip();
                    int outLength = 0;
                    //将buf写入到输出的通道
                    while ((outLength = outChannel.write(buf)) != 0) {
                        System.out.println("写入字节数：" + outLength);
                    }
                    //清除buf,变成写入模式
                    buf.clear();
                }
                //强制刷新磁盘
                outChannel.force(true);
            } finally {
                IOUtil.closeQuietly(outChannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            Logger.debug("base 复制毫秒数：" + (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
