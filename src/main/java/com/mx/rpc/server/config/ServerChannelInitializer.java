package com.mx.rpc.server.config;

import com.mx.rpc.server.config.serialize.ServerCustomDecode;
import com.mx.rpc.server.config.serialize.ServerCustomEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // 添加自定义的编解码器
        pipeline.addLast(new ServerCustomDecode());
        pipeline.addLast(new ServerCustomEncode());

        // 添加业务处理器
        pipeline.addLast(new ServerRequestHandler());
    }
}