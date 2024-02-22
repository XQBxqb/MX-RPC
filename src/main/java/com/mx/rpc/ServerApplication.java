package com.mx.rpc;

import com.mx.rpc.server.NettyServerCentor;
import com.mx.rpc.server.ServerCentor;
import com.mx.rpc.server.provider.NettyServiceFactory;
import com.mx.rpc.service.ShoppingService;
import com.mx.rpc.service.impl.ShoppingServiceImpl;
import com.mx.rpc.zk.ServiceCentor;
import com.mx.rpc.zk.ZookeeperServiceCentor;

import java.net.InetSocketAddress;

public class ServerApplication {
    public static void main(String[] args) {
        ServiceCentor serviceCentor = new ZookeeperServiceCentor();
        //模拟分布式集群服务
        serviceCentor.register(ShoppingService.class.getName(),new InetSocketAddress("127.0.0.1",7777));
        serviceCentor.register(ShoppingService.class.getName(),new InetSocketAddress("127.0.0.1",7777));
        serviceCentor.register(ShoppingService.class.getName(),new InetSocketAddress("127.0.0.1",7777));
        NettyServiceFactory.serviceRegister(ShoppingService.class.getName(), ShoppingServiceImpl.class);
        ServerCentor server = new NettyServerCentor();
        server.start(7777);
    }
}
