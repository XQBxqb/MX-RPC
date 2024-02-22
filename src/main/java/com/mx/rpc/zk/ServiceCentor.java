package com.mx.rpc.zk;

import java.net.InetSocketAddress;

/**
 * Zookeeper服务发现和注册中心，为了简化操作，这里将注册和发现冗余到一个接口中，实际应该是客户端仅发现，服务端仅注册
 */
public interface ServiceCentor {
    void register(String serviceName, InetSocketAddress address);

    InetSocketAddress discovery(String serviceName);
}
