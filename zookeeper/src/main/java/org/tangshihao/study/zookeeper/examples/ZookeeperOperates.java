package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperOperates implements Watcher {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zooKeeper = null;
    private Stat stat = new Stat();

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

    //同步删除节点
    public void deleteNode(String path, int version) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, version);
    }

    //异步删除节点
    public void deleteNodeAsyn(String path, int version) {
        zooKeeper.delete(path, version, new ZookeeperVoidCallBack(), "delete node");
    }

    //获取子节点
    public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
        //watch为true表示，监控该值更改，当该节点的子节点发生变化时将调用process方法
        List<String> children = zooKeeper.getChildren(path, watch);
        return children;
    }

    //同步获取节点数据
    public String getData(String path, boolean watch) throws KeeperException, InterruptedException {
        String data = new String(zooKeeper.getData(path, watch, stat));
        return data;
    }

    //同步修改节点数据
    public void setData(String path, String data, int version) throws KeeperException, InterruptedException {
        zooKeeper.setData(path, data.getBytes(), version);
    }

    //异步修改数据
    public void setDataAsyn(String path, byte[] data, int version) {
        zooKeeper.setData(path, data, version, new ZookeeperSetCallBack(), "Set data");
    }

    public void getDataAsyn(String path, boolean watch) throws KeeperException, InterruptedException {
        zooKeeper.getData(path, watch, new ZookeeperDataCallBack(), stat);
    }

    public void getChildrenAsyn(String path, boolean watch) {
        zooKeeper.getChildren(path, watch, new ZookeeperChildrenCallBack(), null);
    }

    public void close() throws InterruptedException {
        if (zooKeeper != null) {
            zooKeeper.close();
        }
    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            //如果成功连接上zookeeper
            if (Event.EventType.None == watchedEvent.getType() &&
                    watchedEvent.getPath() == null) {
                //第一次连接的时候
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                //子节点被更改时，将触发
                try {
                    System.out.println("ReGet Child:" + zooKeeper.getChildren(watchedEvent.getPath(),
                            true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                //节点的值被改变时，将触发
                try {
                    System.out.println("ReGet Child:" + new String(zooKeeper.getData(watchedEvent.getPath(),
                            true, stat)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

    private class ZookeeperVoidCallBack implements AsyncCallback.VoidCallback {

        public void processResult(int i, String s, Object o) {
            System.out.println(String.format("code: %s, param: %s, ctx: %s",
                    i, s, o));
        }
    }

    private class ZookeeperChildrenCallBack implements AsyncCallback.ChildrenCallback {

        public void processResult(int i, String s, Object o, List<String> list) {
            System.out.println(String.format("code: %s, param: %s, ctx: %s, name: %s",
                    i, s, o, print(list)));
        }
    }

    private class ZookeeperDataCallBack implements AsyncCallback.DataCallback {

        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            System.out.println(String.format("code: %s, param: %s, ctx: %s, name: %s",
                    i, s, o, new String(bytes)));
        }
    }

    private class ZookeeperSetCallBack implements AsyncCallback.StatCallback {

        public void processResult(int i, String s, Object o, Stat stat) {
            System.out.println(String.format("code: %s, param: %s, ctx: %s, stat: %s",
                    i, s, o, stat));
        }
    }

    public static String print(List<String> list) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String temp : list) {
            if (i != 0) {
                result.append(",");
            }
            result.append(temp);
        }
        return result.toString();
    }
}
