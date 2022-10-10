package com.mmkj.nettyDemo;

import com.mmkj.common.utils.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/21 15:35
 */
public class NettyDiscardServer {
    private  int serverPort = 8888;
    /**
     *  netty的服务启动类，他是一个组装和集成器，将不同的Netty组件组装在一起。
     */
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        // 1.构建反应器。netty对应的反应器类型是NioEventLoopGroup
        // boosLoopGroup：负责服务器通道新连接的IO事件的监听
        NioEventLoopGroup boosLoopGroup = new NioEventLoopGroup();
        // workerLoopGroup:负责传输通道IO事件的处理和Handler业务处理
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            serverBootstrap.group(boosLoopGroup, workerLoopGroup);
            //2 设置nio类型的channel，也可以将他设置成阻塞式的OIO
            serverBootstrap.channel(NioServerSocketChannel.class);
            //3 设置监听端口
            serverBootstrap.localAddress(serverPort);
            //4 设置通道的参数：option是给父通道设置一些通道选项，childOption是个子通道设置
            // 是否开始TCP底层心跳机制，true为开启，false为关闭
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            // 可以通过该方法配置父通道的流水线，但是父通道也就是NioServerSocketChannel连接接收通道，他的内部业务处理是固定的：接受新连接后
            // 创建子通道，然后初始化子通道，所以不需要特别的配置
            serverBootstrap.handler(null);
            //5 装配子通道的Pipeline流水线
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            // 6 开始绑定server
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            Logger.info(" 服务器启动成功，监听端口: " + channelFuture.channel().localAddress());

            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            boosLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8888;
        new NettyDiscardServer(port).runServer();
    }
}
