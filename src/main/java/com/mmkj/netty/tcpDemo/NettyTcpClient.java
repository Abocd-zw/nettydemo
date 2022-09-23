package com.mmkj.netty.tcpDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 15:47
 */
public class NettyTcpClient {
    public static void main(String[] args) {
        new NettyTcpClient().run();
    }
    private void run() {
        NioEventLoopGroup clientEventLoop = new NioEventLoopGroup();
        try{
            //创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap
                    //设置线程组
                    .group(clientEventLoop)
                    //设置客户端通道的实现类
                    .channel(NioSocketChannel.class)
                    //处理器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyTcpClientHandler());
                        }
                    });
            System.out.println("client is ok");
            //启动客户端连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 8888).sync();
            //监听关闭通道，监听啥时候关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            clientEventLoop.shutdownGracefully();
        }
    }
}
