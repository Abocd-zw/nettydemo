package com.mmkj.nettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
        // workerLoopGroup:负责传输通道IO事件的处理
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            serverBootstrap.group(boosLoopGroup, workerLoopGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(serverPort);
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("服务器启动成功，监听端口：" + channelFuture.channel().localAddress());

            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {

        } finally {
            workerLoopGroup.shutdownGracefully();
            boosLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8888;
        new NettyDiscardServer(port).runServer();
    }
}
