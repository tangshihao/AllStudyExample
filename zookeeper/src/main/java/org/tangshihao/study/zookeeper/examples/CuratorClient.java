package org.tangshihao.study.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorClient {
    private CuratorFramework client = null;
    private ExecutorService executorService = null;

    public void init() {
        client = CuratorFrameworkFactory.newClient("localhost:2181", 5000,
                3000, new ExponentialBackoffRetry(1000, 3));
        executorService = Executors.newFixedThreadPool(2);
        //Fluent风格的API
//        client = CuratorFrameworkFactory.builder().connectString("localhost:2181")
//                .sessionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
//                .build();
        client.start();
    }

    public void close() {
        client.close();
    }

    //创建永久节点
    public void create(String path, String data) throws Exception {
        client.create().forPath(path, data.getBytes());
    }

    //创建临时节点
    public void createEphemeral(String path, String data) throws Exception {
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
    }

    //创建临时节点，并可递归创建父节点
    public void createParentsIfNeeded(String path, String data) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
    }

    //删除节点
    public void deleteNode(String path) throws Exception {
        client.delete().forPath(path);
    }

    //如果需要删除其子节点
    public void deleteNodeChildrenIfNeeded(String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    //读取数据
    public String getData(String path) throws Exception {
        return new String(client.getData().forPath(path));
    }

    //更新数据
    public void setData(String path, String data) throws Exception {
        client.setData().forPath(path, data.getBytes());
    }

    //异步创建节点接口
    public void createNodeAsyn(String path, String data) throws Exception {
        client.create().inBackground(new CallBackForCurator(), executorService).forPath(path, data.getBytes());
    }

    //获取节点值并监听节点变化
    public String getDataWithListen(final String path) throws Exception {
        final NodeCache nc = new NodeCache(client, path, false);
        nc.start();
        nc.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println(String.format("节点%s已经变化，变为新的值%s",
                        path, new String(nc.getCurrentData().getData())));
            }
        });
        return new String(getData(path));
    }

    //监听子节点变化
    public void listenChildren(String path) throws Exception {
        //true表示缓存数据value
        PathChildrenCache pcc = new PathChildrenCache(client, path, true);
        pcc.start();
        pcc.getListenable().addListener(new ChildrenListener());
    }

    //master选举
    public void selectMaster1() {
        LeaderSelector ls = new LeaderSelector(client, "/master", new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("11成为了master角色");
                Thread.sleep(3000);
                System.out.println("释放掉master权利");
            }
        });
//        ls.autoRequeue();
        ls.start();
    }

    //master选举
    public void selectMaster2() {
        LeaderSelector ls = new LeaderSelector(client, "/master", new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("12成为了master角色");
                Thread.sleep(3000);
                System.out.println("释放掉master权利");
            }
        });
        //自动重排队，会多次重复竞争master
//        ls.autoRequeue();
        ls.start();
    }

    private class CallBackForCurator implements BackgroundCallback {

        public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
            System.out.println(String.format("code: %s, type: %s", curatorEvent.getResultCode(), curatorEvent.getType()));
        }
    }

    //监听节点变化的类
    private class CuratorListener implements NodeCacheListener {

        public void nodeChanged() throws Exception {

        }
    }

    //监听
    private class ChildrenListener implements PathChildrenCacheListener {

        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
            switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:
                    System.out.println(String.format("添加了新的子节点：%s",
                            pathChildrenCacheEvent.getData().getPath(),
                            new String(pathChildrenCacheEvent.getData().getData())));
                    break;
                case CHILD_REMOVED:
                    System.out.println(String.format("节点%s已经被移除", pathChildrenCacheEvent.getData().getPath()));
                    break;
                case CHILD_UPDATED:
                    System.out.println(String.format("节点%s的值被改变，变为%s", pathChildrenCacheEvent.getData().getPath(),
                            new String(pathChildrenCacheEvent.getData().getData())));
                    break;
                default:
            }
        }
    }
}
