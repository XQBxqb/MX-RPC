package com.mx.rpc.server.config;

import com.mx.rpc.entity.response.RpcResponse;
import com.mx.rpc.server.provider.NettyServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.mx.rpc.entity.request.RequestEntity;
import com.mx.rpc.entity.response.ResponseEntity;

import java.lang.reflect.Method;

public class ServerRequestHandler extends SimpleChannelInboundHandler<RequestEntity> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestEntity request) {
        System.out.println("Server received request: " + request);
        ResponseEntity response = null;
        try {
            response=getResponse(request);
        } catch (Exception e) {
            response=RpcResponse.failed(e);
        }
        finally {
            // 发送响应
            ctx.writeAndFlush(response);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 通过反射创建对象，获取方法并运行
     * @param request
     * @return
     * @throws Exception
     */
    private ResponseEntity getResponse(RequestEntity request) throws Exception{
        Class<?> cla = NettyServiceFactory.getServiceInterface(request.getInterfaceName());
        Object instance = cla.newInstance();
        Method method = cla.getDeclaredMethod(request.getMethodsName(), request.getParamsType());
        Object resData = method.invoke(instance, request.getParams());
        return RpcResponse.success(resData);
    }
}
