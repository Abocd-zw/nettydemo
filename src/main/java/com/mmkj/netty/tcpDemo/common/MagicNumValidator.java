package com.mmkj.netty.tcpDemo.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;



/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 16:46
 */
public class MagicNumValidator extends LengthFieldBasedFrameDecoder {
    public MagicNumValidator(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
//
//    @Override
//    public Object decode(ChannelHandlerContext ctx, ByteBuf in) {
//        //魔数校验不通过
//        if(in.getInt(in.readerIndex()) != MAGIC_NUMBER){
//            ctx.channel().close();
//            return null;
//        }
//        return super.decode(ctx, in);
//    }
}
