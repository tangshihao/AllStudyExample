package org.tangshihao.study.net.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client.start();
        Thread.sleep(3000);
        Client.sendMsg("hahah");
    }

    private static class Client {
        private static String DEFAULT_HOST = "localhost";
        private static int DEFAULT_PORT = 12345;
        private static ClientHandle clientHandle;

        public static void start() {
            start(DEFAULT_HOST, DEFAULT_PORT);
        }

        public static synchronized void start(String ip, int port) {
            if (clientHandle != null) {
                clientHandle.stop();
            }
            clientHandle = new ClientHandle(ip, port);
            new Thread(clientHandle, "client").start();
        }

        public static boolean sendMsg(String msg) throws IOException {
            if (msg.equals("q"))
                return false;
            clientHandle.sendMsg(msg);
            return true;
        }
    }

    private static class ClientHandle implements Runnable {
        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private volatile boolean started;

        public ClientHandle(String ip, int port) {
            this.host = ip;
            this.port = port;
            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                started = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        public void stop() {
            started = false;
        }

        public void run() {
            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            while (started) {
                try {
                    selector.select(1000);
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    SelectionKey key = null;
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
                    System.exit(1);
                }
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMsg(String msg) throws IOException {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel, msg);
        }

        private void doWrite(SocketChannel channel, String request) throws IOException {
            byte[] bytes = request.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }

        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                SocketChannel sc = (SocketChannel)key.channel();
                if (key.isConnectable()) {
                    if (sc.finishConnect()) {

                    } else {
                        System.exit(1);
                    }
                }
                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(buffer);
                    if (readBytes > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        System.out.println("客户端收到消息：" + new String(bytes, "UTF-8"));
                    } else if (readBytes < 0) {
                        key.channel();
                        sc.close();
                    }
                }
            }
        }

        private void doConnect() throws IOException {
            if (socketChannel.connect(new InetSocketAddress(host, port))) {

            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        }
    }
}
