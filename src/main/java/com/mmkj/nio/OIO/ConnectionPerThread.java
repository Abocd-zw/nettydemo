package com.mmkj.nio.OIO;

import com.mmkj.nio.NioDemoConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *  BIO 编程中，最初的和最原始的网络服务器程序，
 *  是用一个while循环，不断的监听端口是否有新的连接
 *  如果有，就调用一个处理函数来处理
 */
public class ConnectionPerThread implements Runnable {
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                // 接收一个连接后，为socket连接，新建一个传输的处理器对象
                Handler handler = new Handler(socket);
                //创建新线程来handle，或者，使用线程池来处理
                new Thread(handler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class Handler implements Runnable {
        final Socket socket;

        Handler(Socket s) {
            socket = s;
        }

        public void run() {
            while (true) {
                try {
                    byte[] input = new byte[NioDemoConfig.SERVER_BUFFER_SIZE];
                    // 读取数据
                    socket.getInputStream().read(input);
                    // 处理业务逻辑，获取处理结果
                    byte[] output =null;
                    // 写入结果
                    socket.getOutputStream().write(output);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}