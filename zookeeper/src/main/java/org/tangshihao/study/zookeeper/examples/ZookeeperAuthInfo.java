package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.*;

import java.io.IOException;

public class ZookeeperAuthInfo {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 5000, null);
        //使用digest模式，用户名为foo，密码为true
        zookeeper.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper.create("/test112", "555".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.PERSISTENT);
        ZooKeeper zookeeper1 = new ZooKeeper("localhost:2181", 5000, null);
        //'/test1'节点具有权限，下面方法执行会出错
        System.out.println(new String(zookeeper1.getData("/test112", false, null)));
    }
}
