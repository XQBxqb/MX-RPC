package com.mx.rpc.client.proxy;

import com.mx.rpc.client.RequestClient;
import com.mx.rpc.entity.request.RequestEntity;
import com.mx.rpc.entity.request.RpcRequest;
import com.mx.rpc.entity.response.ResponseEntity;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建jdk动态代理，所有客户端接口请求均有单例代理对象发送
 */

@AllArgsConstructor
public class NettyClientProxy implements InvocationHandler {
    private final RequestClient client;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
        RequestEntity requestEntity = RpcRequest.builder()
                                                .interfaceName(method.getDeclaringClass()
                                                                     .getName())
                                                .methodsName(method.getName())
                                                .params(args)
                                                .paramsType(method.getParameterTypes())
                                                .build();
        ResponseEntity response = null;
        try {
            response = client.sendRequest(requestEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
}
