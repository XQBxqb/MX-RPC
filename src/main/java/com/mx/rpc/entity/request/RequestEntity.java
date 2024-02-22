package com.mx.rpc.entity.request;

public interface RequestEntity {
    String getInterfaceName();
    String getMethodsName();

    Object[] getParams();

    Class<?>[] getParamsType();
}
