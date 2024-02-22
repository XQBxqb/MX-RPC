package com.mx.rpc.zk.listerner;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * 监听器，实现zookeeper服务节点注册，失效相关逻辑
 */
@AllArgsConstructor
public class ZKGlobalListerner {
    private final TreeCache treeCache;
    @SneakyThrows
    public void listern(){
        TreeCacheListener listener = (client1, event) -> {
            switch (event.getType()) {
                case NODE_ADDED:
                    System.out.println("Node added: " + event.getData().getPath());
                    break;
                case NODE_REMOVED:
                    System.out.println("Node removed: " + event.getData().getPath());
                    break;
                case NODE_UPDATED:
                    System.out.println("Node updated: " + event.getData().getPath());
                    break;
                default:
                    break;
            }
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
    }


}
