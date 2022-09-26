package com.mmkj.nettyDemo.tcpDemo.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;



import java.util.List;

/**
 * @description:
 * @author: zhangw
 * @create: 2022/9/22 16:54
 */
public class CustomDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 获取得到可读取的字节长度
        int len = in.readableBytes();
        in.markReaderIndex();
        if (len > 0) {
            byte[] src = new byte[len];
            in.readBytes(src);          //把数据读到字节数组中(读取完之后指针会到最后一个数据)
            in.resetReaderIndex();      //重置当前指针到标记位(包头)
            //验证首部为A5 5A,只接收首部正确的数据包,如果包头错误可以直接丢弃或关闭连接
            if ((src[0] & 0x000000ff) == 0xA5 && (src[1] & 0x000000ff) == 0x5A) {
                //计算报文长度
                byte[] data =  {src[3],src[2]};
                String hexLen = byteArrayToHexString(data);
                //这里计算出来的是数据长度的报文长度,需要加27个固定长度
                int pLen = Integer.parseInt(hexLen, 16) + 27;
                if (len < pLen) {
                    //当数据包的长度不够时直接return，netty在缓冲区有数据时会一直调用decode方法，所以我们只需要等待下一个数据包传输过来一起解析
                    return;
                }
                byte[] packet = new byte[pLen];
                in.readBytes(packet,0,pLen);
                out.add(packet);
            }else {
                channelHandlerContext.close();
            }
        }
    }

    public static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }
    private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
}
