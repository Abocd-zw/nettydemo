package com.mmkj.ReactorModel;


import com.mmkj.nio.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *  单Reactor反应器版本的EchoServer
 *  EchoServer回显服务器的功能：读取客户端的输入，回显到客户端
 */
class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        //Reactor初始化
        // 获取选择器
        selector = Selector.open();
        // 开启ServerSocket服务监听通道
        serverSocket = ServerSocketChannel.open();
        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, AcceptorHandler
        // 绑定AcceptHandler新连接处理器到SelectKey
        sk.attach(new AcceptorHandler());
    }

    /**
     *  轮询和分发事件
     */
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
