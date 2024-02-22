package com.mx.rpc.zk.balance;

import com.mx.rpc.utils.FuryUtils;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RoundBalance implements LoadBalance {

    private final CuratorFramework client;


    private RoundBalance(CuratorFramework curatorFramework){
        this.client = curatorFramework;
    }

    public static RoundBalance getRoundBalance(CuratorFramework client){
        return new RoundBalance(client);
    }
    private String getBalanceNode(String serviceName){
        return serviceName+".0";
    }

    @Override
    @SneakyThrows
    public byte[] balanceAddress(String serviceName) {
        String str = getNowAddress(serviceName);
        return client.getData().forPath("/"+serviceName+"/"+str);
    }

    private String getNowAddress(String serviceName) throws Exception {
        byte[] bytes = client.getData()
                             .forPath("/" + serviceName + "/" + getBalanceNode(serviceName));
        String str = FuryUtils.deserialize(bytes, String.class);
        return str;
    }

    @Override
    @SneakyThrows
    public void registerAddress(String serviceName,String address) {
        if(!existService(serviceName)){
            createBalanceNode(serviceName);
        }
        List<String> list = client.getChildren()
                                  .forPath("/"+serviceName);
        if(list.size()==1){
            createNode(serviceName,serviceName+".1",address);
        }else{
            String[] split = list.stream().sorted(String::compareTo).collect(Collectors.toList()).get(list.size() - 1)
                                 .split("\\.");
            int index = Integer.parseInt(split[split.length-1])+1;
            createNode(serviceName,serviceName+"."+index,address);
        }
    }


    @Override
    @SneakyThrows
    public byte[] discovery(String serviceName) {
        List<String> list = client.getChildren()
                                  .forPath("/"+serviceName);
        if (list.isEmpty())
            throw new RuntimeException();

        byte[] data = balanceAddress(serviceName);
        updateBalanceAddress(serviceName,list);
        return data;
    }
    @SneakyThrows
    private void updateBalanceAddress(String serviceName,List<String> paths){
        if (paths.isEmpty())
            throw new RuntimeException();

        String last = getNowAddress(serviceName);
        paths=paths.stream().sorted(String::compareTo).collect(Collectors.toList());
        AtomicInteger index = new AtomicInteger(0);
        AtomicInteger nextServiceIndex = new AtomicInteger();
        paths.forEach(t->{
            if(t.equals(last)){
                nextServiceIndex.set(index.addAndGet(1));
                return;
            }
            index.getAndAdd(1);
        });

        if(nextServiceIndex.get() == paths.size())
            nextServiceIndex.set(1);

        client.setData().forPath("/"+serviceName+"/"+getBalanceNode(serviceName),FuryUtils.serialize(paths.get(nextServiceIndex.get())));
    }

    @SneakyThrows
    private void createNode(String serviceName,String serverAddressName,String data){
        client.create()
              .creatingParentsIfNeeded()
              .withMode(CreateMode.EPHEMERAL)
              .forPath("/"+serviceName+"/"+serverAddressName,FuryUtils.serialize(data));
    }

    @SneakyThrows
    private void createBalanceNode(String serviceName){
        client.create()
              .creatingParentsIfNeeded()
              .withMode(CreateMode.EPHEMERAL)
              .forPath("/"+serviceName+"/"+getBalanceNode(serviceName),FuryUtils.serialize(serviceName+".1"));
    }

    @SneakyThrows
    private Boolean existService (String serviceName){
        return client.checkExists().forPath("/"+serviceName) == null ? false : true;
    };

}
