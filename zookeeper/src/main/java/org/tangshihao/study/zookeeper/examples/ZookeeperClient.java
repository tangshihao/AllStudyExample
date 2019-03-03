package org.tangshihao.study.zookeeper.examples;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ZookeeperClient {
    private ZkClient zkClient = null;

    public void init() {
        zkClient = new ZkClient("localhost:2181", 5000);
        System.out.println("zookeeper连接已经建立");
    }

    public void createNode(String path) {
        //可以递归创建目录
        zkClient.createPersistent(path, true);
    }

    //对孩子节点结构的改变进行订阅
    public void subChild(String path) {
        zkClient.subscribeChildChanges(path, new ChildListener());
    }

    //对数据的改变进行订阅
    public void subData(String path) {
        zkClient.subscribeDataChanges(path, new DataListener());
    }

    public void setData(String path, String value) {
        zkClient.writeData(path, value);
    }

    public void close() {
        zkClient.close();
    }

    private class ChildListener implements IZkChildListener {

        public void handleChildChange(String s, List<String> list) throws Exception {
            System.out.println("节点" + s + "已经改变，现在的孩子节点为：" + print(list));
        }
    }

    private class DataListener implements IZkDataListener {

        public void handleDataChange(String s, Object o) throws Exception {
            System.out.println("节点" + s + "被改变了，新的值为" + o);
        }

        public void handleDataDeleted(String s) throws Exception {
            System.out.println("节点" + s + "被删除了");
        }
    }

    private String print(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String value : list) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(value);
            i++;
        }
        return sb.toString();
    }
}
