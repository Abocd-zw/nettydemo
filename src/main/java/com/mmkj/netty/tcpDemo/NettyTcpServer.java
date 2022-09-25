package com.mmkj.netty.tcpDemo;

import com.mmkj.netty.tcpDemo.common.CustomDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 15:35
 */
public class NettyTcpServer {
    public static void main(String[] args) {
        new NettyTcpServer().run();
    }
    private void run()  {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_RCVBUF, 10240)
                    //
                    .option(ChannelOption.TCP_NODELAY, false)
                    //.option(ChannelOption.SO_SNDBUF, 10240)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyTcpServerHandler());
                            pipeline.addLast(new CustomDecode());
                            pipeline.addLast(new StringDecoder());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("服务端启动成功");
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
