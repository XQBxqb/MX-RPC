package com.mx.rpc.client;

import com.mx.rpc.client.config.NettyClientInitializer;
import com.mx.rpc.entity.request.RequestEntity;
import com.mx.rpc.entity.response.ResponseEntity;
import com.mx.rpc.zk.ServiceCentor;
import com.mx.rpc.zk.ZookeeperServiceCentor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class NettyRequestClient implements RequestClient{
    private static final Bootstrap BOOTSTRAP;//Netty的Reactor，用于管理连接、监听事件

    private static final EventLoopGroup EVENT_LOOP_GROUP;//Netty的Handler，用于处理真正的IO操作、业务逻辑
    private final ServiceCentor serviceCentor;//Zookeeper服务中心，为了简化操作，这里客户端和服务端代码冗余写进ServiceCentor

    public NettyRequestClient() {
        this.serviceCentor = new ZookeeperServiceCentor();
    }

    static {
        EVENT_LOOP_GROUP = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        BOOTSTRAP.group(EVENT_LOOP_GROUP).channel(NioSocketChannel.class);
    }

    public ResponseEntity sendRequest(RequestEntity request) throws InterruptedException {
        // 创建Promise对象
        final Promise<ResponseEntity> promise = new DefaultPromise<>(EVENT_LOOP_GROUP.next());
        // 发现服务地址
        InetSocketAddress address = serviceCentor.discovery(request.getInterfaceName());
        String host = address.getHostName();
        int port = address.getPort();

        // 连接到服务器
        ChannelFuture connectFuture = BOOTSTRAP.clone() // 使用clone以避免修改原始BOOTSTRAP对象
                                               .handler(new NettyClientInitializer(promise))
                                               .connect(host, port);
        // 等待连接完成
        connectFuture.sync();
        // 发送请求
        connectFuture.channel().writeAndFlush(request);
        // 等待Promise完成并返回结果
        promise.await(); // 可以设置超时时间
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            // 处理失败情况
            throw new RuntimeException(promise.cause());
        }
    }

}
