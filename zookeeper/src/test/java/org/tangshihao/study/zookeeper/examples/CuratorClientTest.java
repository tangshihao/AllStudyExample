package org.tangshihao.study.zookeeper.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;

public class CuratorClientTest {
    private CuratorClient cc = null;

    @Before
    public void before() {
        cc = new CuratorClient();
        cc.init();
    }

    @After
    public void after() {
        cc.close();
    }

    @Test
    public void create() throws Exception {
        cc.create("/test1/ttyt", "123");
    }

    @Test
    public void create2() throws Exception {
        cc.createEphemeral("/test1/tt4", "222");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void setData() throws Exception {
        cc.setData("/test1/tt2", "223");
    }

    @Test
    public void getData() throws Exception {
        System.out.println(cc.getData("/test1"));
    }

    @Test
    public void createNodeAsyn() throws Exception {
        cc.createNodeAsyn("/test2", "333");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getDataWithListen() throws Exception {
        System.out.println(cc.getDataWithListen("/test1"));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void listenChildChanges() throws Exception {
        cc.listenChildren("/test1");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void createSequential() throws Exception {
        cc.createSequential("/seq/id-", "123");
    }

    public static void main(String[] args) throws InterruptedException {
        final CuratorClient cc = new CuratorClient();
        cc.init();
        Thread t1 = new Thread() {
            public void run() {
                cc.selectMaster1();
            }
        };
        t1.setName("t1");
        Thread t2 = new Thread() {
            public void run() {
                cc.selectMaster2();
            }
        };
        t2.setName("t2");
        t1.start();
        t2.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
