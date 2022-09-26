package com.mmkj.nio.channelDemo.socketChannelDemo;


import com.mmkj.nio.NioDemoConfig;
import com.mmkj.common.utils.Print;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;


public class UDPServer {
    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }

    private void receive() throws IOException {
        //1. 获取DatagramChannel数据报通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                , NioDemoConfig.SOCKET_SERVER_PORT));
        Print.tcfo("UDP 服务器启动成功！");

        // 开启一个通道选择器
        Selector selector = Selector.open();
        // 将通道注册到选择器，监听可读事件
        datagramChannel.register(selector, SelectionKey.OP_READ);
        // 通过选择器，查询IO事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 可读事件：有数据到来
                if (selectionKey.isReadable()) {
                    // 2. 读取DatagramChannel数据报通道数据
                    SocketAddress client = datagramChannel.receive(buffer);
                    buffer.flip();
                    Print.tcfo(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
        // 关闭选择器和通道
        selector.close();
        datagramChannel.close();
    }
}
