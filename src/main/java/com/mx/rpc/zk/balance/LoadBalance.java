package com.mx.rpc.zk.balance;

/**
 * 负载均衡器，这里实现了轮询，也可以实现随机，实现的均衡器要
 */
public interface LoadBalance {
    byte[] balanceAddress(String serviceName);

    void registerAddress(String serviceName,String address);
    //注销一个服务，像接口传入服务所在集群的位置

    byte[] discovery(String serviceName);

}
