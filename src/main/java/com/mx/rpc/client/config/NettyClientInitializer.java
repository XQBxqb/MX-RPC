package com.mx.rpc.client.config;


import com.mx.rpc.client.config.serialize.ClientCustomDecode;
import com.mx.rpc.client.config.serialize.ClientCustomEncode;
import com.mx.rpc.entity.response.ResponseEntity;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    final Promise<ResponseEntity> promise;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ClientCustomDecode());
        pipeline.addLast(new ClientCustomEncode());
        pipeline.addLast(new NettyClientHandler(promise));
    }
}
