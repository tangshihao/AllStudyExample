package org.tangshihao.study.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO是阻塞IO，在这种方式下，客户端每发送一个请求，服务端都必须创建一个线程来处理该请求。
 * 也就是说客户端发送多少请求，服务端就创建多少个线程处理。该网络IO的缺点很明显，无法支持大并发
 * 的应用场景。
 */
public class BIOInstance {
    //这是一个BIO的网络编程例子，阻塞式IO是指在IO的时候是同步的，必须要等IO过程结束才能执行下一步
    private class Server extends Thread {
        private ServerSocket serverSocket;

        public void init() throws IOException {
            serverSocket = new ServerSocket(8888);
        }

        public void run() {
            byte[] buf = null;
            try {
                Socket clientSocket = serverSocket.accept();
                InputStream is = clientSocket.getInputStream();
                while (is.available() == 0) {
                    sleep(1000);
                }
                buf = new byte[is.available()];
                is.read(buf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("服务端收到啦：" + new String(buf));
        }
    }

    private class Client extends Thread {
        private Socket socket;

        public void init() throws IOException {
            socket = new Socket("localhost", 8888);
        }

        public void run() {
            try {
                OutputStream os = socket.getOutputStream();
                os.write("连接成功啦".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new BIOInstance().new Server();
        server.init();
        server.start();
        Thread.sleep(3000);
        Client client = new BIOInstance().new Client();
        client.init();
        client.start();
        server.join();
        client.join();
        System.out.println("结束了");
    }
}
