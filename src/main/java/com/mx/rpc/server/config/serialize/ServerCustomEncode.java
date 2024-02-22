package com.mx.rpc.server.config.serialize;


import com.mx.rpc.utils.FuryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerCustomEncode extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 使用FuryUtils进行序列化
        byte[] data = FuryUtils.serialize(msg);

        // 首先写入数据长度，以便解码器能够正确地读取一个完整的消息
        // 数据长度也是一个整数，占用4个字节
        out.writeInt(data.length);

        // 然后写入实际的数据
        out.writeBytes(data);
    }
}
