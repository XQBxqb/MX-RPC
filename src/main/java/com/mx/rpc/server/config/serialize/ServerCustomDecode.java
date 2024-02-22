package com.mx.rpc.server.config.serialize;

import com.mx.rpc.entity.request.RequestEntity;
import com.mx.rpc.entity.request.RpcRequest;
import com.mx.rpc.entity.response.RpcResponse;
import com.mx.rpc.utils.FuryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ServerCustomDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 确保有足够的字节可以读取，以进行正确的反序列化
        // 这里的4是一个示例值，具体值取决于你的协议设计
        if (in.readableBytes() < 4) {
            return;
        }
        // 标记当前readIndex的位置，以便重置
        in.markReaderIndex();
        // 读取数据长度（根据你的协议设计来）
        int dataLength = in.readInt();
        // 检查是否接收到了完整的数据包
        if (in.readableBytes() < dataLength) {
            // 数据包不完整，重置readIndex等待更多数据到来
            in.resetReaderIndex();
            return;
        }
        // 读取指定长度的字节数据
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        // 使用FuryUtils进行反序列化
        Object obj = FuryUtils.deserialize(data, RpcRequest.class);
        // 将反序列化后的对象传递给下一个ChannelInboundHandler处理
        out.add(obj);
    }
}
