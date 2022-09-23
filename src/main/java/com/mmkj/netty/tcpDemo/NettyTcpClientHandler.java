package com.mmkj.netty.tcpDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 15:51
 */
public class NettyTcpClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client    " + ctx);
        //将数据写到缓存区，再传到通道里
        //ctx.writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
        String content = "你好，服务器! ";
        //byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
        for (int i=0; i< 10000; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务器，我是客户端! ", CharsetUtil.UTF_8));
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("server answer: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("server address: "+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常信息
        cause.printStackTrace();
        //关闭通道
        ctx.channel().close();
    }
}
