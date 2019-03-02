package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperOperates implements Watcher {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zooKeeper = null;

    public void init() throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper("localhost:2181", 5000, this);
        countDownLatch.await();
    }

    //同步创建节点
    public void createNode(String path) throws KeeperException, InterruptedException {
        String response = zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println("Create node successfully: " + response);
    }

    //异步创建节点
    public void createNodeAsyn(String path) {
        zooKeeper.create(path, "1234".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, new ZookeeperCallBack(), "I am context");
    }

    public void close() throws InterruptedException {
        if (zooKeeper != null) {
            zooKeeper.close();
        }
    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperOperates zo = null;
        try {
            zo = new ZookeeperOperates();
            zo.init();
            //在zookeeper中创建一个节点
            zo.createNode("/test1");
        } finally {
            zo.close();
        }
    }

    private class ZookeeperCallBack implements AsyncCallback.StringCallback {
        /**
         * 当提交给zookeeper的请求被处理完之后，该方法将会被调用
         * @param i
         * @param s
         * @param o
         * @param s1
         */
        public void processResult(int i, String s, Object o, String s1) {
            System.out.println(String.format("code: %s, param: %s, ctx: %s, name: %s",
                    i, s, o, s1));
        }
    }
}
