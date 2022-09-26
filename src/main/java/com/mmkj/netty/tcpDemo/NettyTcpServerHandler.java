package com.mmkj.netty.tcpDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 15:43
 */
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端接收到客户端连接请求....");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //System.out.println("server ctx =" + ctx);
        //将客户端消息msg 转换为ByteBuf类型，此bytebuf是netty提供的，性能更高
        System.out.println("读取次数 = " + (++count));
        if (msg instanceof ByteBuf ) {
            ByteBuf buf = (ByteBuf) msg;
            // 模拟拆包
            //System.out.println("读取次数 = " + (++count));
            //System.out.println("长度是：" + buf.readableBytes());

            // 模式粘包
            System.out.println("client send message: " + buf.toString(CharsetUtil.UTF_8));
            System.out.println("client send address: " + ctx.channel().remoteAddress());
        } else {
            System.out.println("client send messageStr: " + msg);
            System.out.println("client send address: " + ctx.channel().remoteAddress());
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写到缓存区ButeBuf中，并且传到通道里，write+flush
        //unPooled: Netty 提供一个专门用来操作缓冲区(即 Netty 的数据容器)的工具类
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道，拿到通道再关闭
        ctx.channel().close();
    }


}
