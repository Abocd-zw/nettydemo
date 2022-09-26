package com.mmkj.netty.tcpDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

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

        //String content = "你好，服务器! ";
        //byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
//        for (int i=0; i< 10; i++) {
//            // 模拟粘包
//           // ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务器，我是客户端" + i, CharsetUtil.UTF_8));
//            // 模拟解决粘包方式一：添加换行符
//            ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务器，我是netty客户端" + i + "\n", CharsetUtil.UTF_8));
//        }

        // 模拟拆包
//        byte[] bytes = new byte[102400];
//        Arrays.fill(bytes, (byte)10);
//        for (int i = 0; i < 10; i++) {
//            ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
//        }

        //一次发送102400字节数据
        try{
            char[] chars = new char[10240];
            Arrays.fill(chars, 0, 10238, 'a');
            chars[10239] = '\n';
            for (int i = 0; i < 10; i++) {
                ctx.writeAndFlush(Unpooled.copiedBuffer(chars, CharsetUtil.UTF_8));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
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
