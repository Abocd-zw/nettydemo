package com.mmkj.nio.channelDemo.socketChannelDemo;

import com.mmkj.nio.NioDemoConfig;
import com.mmkj.common.utils.Dateutil;
import com.mmkj.common.utils.Print;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 *
 **/
public class UDPClient {

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }

    private void send() throws IOException {
        //1. 获取DatagramChannel数据报通道
        DatagramChannel dChannel = DatagramChannel.open();
        // 设置为非阻塞
        dChannel.configureBlocking(false);

        // 如果需要接收数据，还需要调用bind方法绑定一个数据报的监听端口。
        //dChannel.socket().bind(new InetSocketAddress(8888));

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

        Scanner scanner = new Scanner(System.in);
        Print.tcfo("UDP 客户端启动成功！");
        Print.tcfo("请输入发送内容:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            // 将数据写入到缓冲区
            buffer.put((Dateutil.getNow() + " >>" + next).getBytes());
            // 将缓冲区翻转到读模式
            buffer.flip();
            // 3. 通过DatagramChannel数据报通道发送数据
            dChannel.send(buffer, new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            // 清空缓冲区，切换到写模式
            buffer.clear();
        }
        //4.关闭DatagramChannel数据报通道
        dChannel.close();
    }
}
