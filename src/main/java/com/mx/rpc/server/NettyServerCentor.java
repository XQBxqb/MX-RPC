package com.mx.rpc.server;

import com.mx.rpc.server.config.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;

public class NettyServerCentor implements ServerCentor{

    @Override
    @SneakyThrows
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .localAddress(port)
             .childHandler(new ServerChannelInitializer());
            ChannelFuture f = b.bind().sync();
            System.out.println("RPC Server started and listening on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

}
