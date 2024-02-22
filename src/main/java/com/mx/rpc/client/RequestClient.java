package com.mx.rpc.client;

import com.mx.rpc.entity.request.RequestEntity;
import com.mx.rpc.entity.response.ResponseEntity;

public interface RequestClient {
    ResponseEntity sendRequest(RequestEntity request) throws InterruptedException ;
}
