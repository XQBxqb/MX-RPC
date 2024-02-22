package com.mx.rpc.utils;

import io.fury.Fury;
import io.fury.config.Language;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Fury高性能序列化工具,Netty客户端能够自定义该序列化工具
 */
public class FuryUtils {
    private static final Fury FURY ;

    private static final ConcurrentMap<Class<?>,Boolean> registerMap = new ConcurrentHashMap<>();
    static {
        FURY = Fury.builder()
                   .withLanguage(Language.JAVA)
                   .withNumberCompressed(true)
                   // 开启类型前后兼容，允许序列化和反序列化字段不一致，无该类需求建议关闭，性能更好
                   // .withCompatibleMode(CompatibleMode.COMPATIBLE)
                   // 开启异步多线程编译
                   .withAsyncCompilation(true)
                   .build();
    }

    public static byte[] serialize(Object obj){
        register(obj.getClass());
        return FURY.serialize(obj);
    }

    public static  <T>T deserialize(byte[] data,Class<T> cla){
        register(cla);
        Object deserialize = FURY.deserialize(data);
        return (T)deserialize;
    }

    private static void register(Class<?> cla){
        Boolean exist = registerMap.getOrDefault(cla, Boolean.FALSE);
        if(!exist){
            registerMap.put(cla,Boolean.TRUE);
            FURY.register(cla);
        }
    }
}
