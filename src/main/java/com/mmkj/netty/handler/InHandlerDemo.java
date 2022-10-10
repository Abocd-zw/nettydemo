package com.mmkj.netty.handler;

import com.mmkj.common.utils.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @author Abocd
 */
public class InHandlerDemo extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * 当通道注册完成后，Netty会调用fireChannelRegistered，触发通道注册事件。
     * 通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelRegistered方法，会被调用到
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：channelRegistered()");
        super.channelRegistered(ctx);
    }

    /**
     * 当通道激活完成后，Netty会调用fireChannelActive，触发通道激活事件。
     * 通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelActive方法，会被调用到
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 当通道缓冲区可读，Netty会调用fireChannelRead，触发通道可读事件。
     * 通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelRead方法，会被调用到
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Logger.info("被调用：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 当通道缓冲区读完，Netty会调用fireChannelReadComplete，触发通道读完事件。
     * 通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelReadComplete方法，会被调用到。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    /**
     * 当连接被断开或者不可用，Netty会调用fireChannelInactive，触发连接不可用事件。
     * 通道会启动对应的流水线处理，在通道注册过的入站处理器Handler的channelInactive方法，会被调用到
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Logger.info("被调用：handlerRemoved()");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}