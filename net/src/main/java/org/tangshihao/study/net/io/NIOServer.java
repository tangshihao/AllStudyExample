package org.tangshihao.study.net.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO相对于BIO来说，它使用一个Reactor线程来轮询一个请求池，当客户端发来请求时，它就能从请求池获取这个
 * 请求。对于BIO来说，每一个请求必然开启一个线程来处理，但是在NIO场景下，Reactor可以选择单线程来串行处理
 * 这些请求，也可以使用多个线程来处理这些请求，因此对于大并发的场景，该方式可以控制创建的线程数量，防止创建
 * 太多线程造成服务资源耗尽。
 */
public class NIOServer {
    public static void main(String[] args) {
        Server.start();
    }

    private static class Server {
        private static int DEFAULT_PORT = 12345;
        private static ServerHandle serverHandle;

        public static void start() {
            start(DEFAULT_PORT);
        }

        public static synchronized void start(int port) {
            if (serverHandle != null) {
                serverHandle.stop();
            }
            serverHandle = new ServerHandle(port);
            new Thread(serverHandle, "Server").start();
        }
    }

    //Reactor线程，即多路复用器，该线程负责轮询查看是否有新的连接产生
    private static class ServerHandle implements Runnable {
        private Selector selector;
        private ServerSocketChannel ssc;
        private volatile boolean started;

        public ServerHandle(int port) {
            try {
                selector = Selector.open();
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.socket().bind(new InetSocketAddress(port), 1024);
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                started = true;
                System.out.println("服务已经启动，端口号：" + port);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        public void stop() {
            started = false;
        }

        public void run() {
            while (started) {
                try {
                    //每隔一秒检查一次所有管道
                    selector.select(1000);
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    SelectionKey key;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (selector != null)
                try {
                    selector.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                if (key.isAcceptable()) {
                    //这个表示是客户端发来了连接请求，等待服务端响应
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    //执行下面操作，tcp连接将建立
                    SocketChannel sc = ssc.accept();
                    System.out.println("与客户端连接成功！");
                    sc.configureBlocking(false);
                    //将该channel注册回selector中，这样在下次selector轮询中会重新获得该channel
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    //表示该channel已经与客户端建立了连接，可以直接从里面读取数据了
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(buffer);
                    if (readBytes > 0) {
                        //将索引设置到缓存数组的最开头
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        //将缓存的数据读入到bytes中
                        buffer.get(bytes);
                        System.out.println("服务器收到消息：" + new String(bytes, "UTF-8"));
                        doWrite(sc, "服务器收到请求了");
                    }
                }
            }
        }

        private void doWrite(SocketChannel channel, String response) throws IOException {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
