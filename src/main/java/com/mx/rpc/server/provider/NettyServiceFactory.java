package com.mx.rpc.server.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于映射接口和实现类，通过注册中心的接口名映射到接口的实现类
 */

public class NettyServiceFactory {

    private static final ConcurrentMap<String,Class<?>> serviceMap;

    static {
        serviceMap = new ConcurrentHashMap<>();
    }

    public static void serviceRegister(String serviceName,Class<?> cla) {
        serviceMap.put(serviceName,cla);
    }

    public static Class<?> getServiceInterface(String serviceName) {
        return serviceMap.get(serviceName);
    }

}
