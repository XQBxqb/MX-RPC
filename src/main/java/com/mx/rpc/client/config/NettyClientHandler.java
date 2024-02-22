package com.mx.rpc.client.config;

import com.mx.rpc.entity.response.ResponseEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseEntity> {
    private final Promise<ResponseEntity> promise;
    public NettyClientHandler(Promise<ResponseEntity> promise) {
        this.promise = promise;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseEntity msg) {
        promise.setSuccess(msg);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
