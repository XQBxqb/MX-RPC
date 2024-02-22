package com.mx.rpc.zk;

import com.mx.rpc.utils.FuryUtils;
import com.mx.rpc.zk.balance.LoadBalance;
import com.mx.rpc.zk.balance.RoundBalance;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;

public class ZookeeperServiceCentor implements ServiceCentor{
    private static final CuratorFramework client;

    private static String ROOT_PATH;

    private static final LoadBalance LOAD_BALANCE;

    static {
        ROOT_PATH = "root";
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                                             .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        LOAD_BALANCE = RoundBalance.getRoundBalance(client);
        client.start();
    }



    @Override
    public void register(String serviceName, InetSocketAddress address) {
        LOAD_BALANCE.registerAddress(serviceName,parseAddress(address));
    }

    @Override
    public InetSocketAddress discovery(String serviceName) {
        return decodeAddress(decode(LOAD_BALANCE.discovery(serviceName)));
    }

    private String decode(byte[] bytes){
        return FuryUtils.deserialize(bytes,String.class);
    }
    private String parseAddress(InetSocketAddress address){
        return address.getHostString()+":"+address.getPort();
    }

    private InetSocketAddress decodeAddress(String address){
        String[] str = address.split(":");
        return new InetSocketAddress(str[0], Integer.parseInt(str[1]));
    }
}
