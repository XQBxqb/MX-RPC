package com.mx.rpc.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcResponse implements ResponseEntity{
    private Integer code;

    private String message;

    private Object data;

    private Class<?> dataType;

    public static ResponseEntity success(Object data){
        return RpcResponse.builder().code(200).message("处理成功!").data(data).dataType(data.getClass()).build();
    }

    public static ResponseEntity failed(Exception e){
        return RpcResponse.builder().code(400).message("处理异常!").data(e).dataType(e.getClass()).build();
    }
}
