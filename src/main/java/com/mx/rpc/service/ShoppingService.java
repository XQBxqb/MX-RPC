package com.mx.rpc.service;

import java.util.List;

/**
 * RPC通信接口，客户端进需要声明该接口，服务端要实现该接口
 */

public interface ShoppingService {
    Integer price(String name);

    List<String> products();
}
