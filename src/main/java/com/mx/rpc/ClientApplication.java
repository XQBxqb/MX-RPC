package com.mx.rpc;

import com.mx.rpc.client.NettyRequestClient;
import com.mx.rpc.client.RequestClient;
import com.mx.rpc.client.proxy.NettyClientProxy;
import com.mx.rpc.service.ShoppingService;

import java.util.List;

public class ClientApplication {
    public static void main(String[] args) {
        RequestClient client = new NettyRequestClient();
        NettyClientProxy proxyClient = new NettyClientProxy(client);
        ShoppingService service = proxyClient.getProxy(ShoppingService.class);
        //模拟客户端与服务端多次通信，同时能够效验负载器均衡工作
        while(true){
            List<String> products = service.products();
            products.forEach(t-> System.out.println(t));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}