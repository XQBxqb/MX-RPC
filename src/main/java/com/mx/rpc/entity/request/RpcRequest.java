package com.mx.rpc.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRequest implements RequestEntity{
    private String interfaceName;

    private String methodsName;

    private Object[] params;

    private Class<?>[] paramsType;

}
