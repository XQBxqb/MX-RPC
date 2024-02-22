package com.mx.rpc.entity.response;

public interface ResponseEntity {
    Integer getCode();

    String getMessage();

    Object getData();

    Class<?> getDataType();
}
