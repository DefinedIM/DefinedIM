package org.definedim.net.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private boolean running = false;
    private int port;
    private ServerListenThread thread;
    private SocketHandler handler;

    public SocketServer(int _port, SocketHandler _handler) {
        port = _port;
        handler = _handler;
        thread = new ServerListenThread(port, handler);
    }

    public void start() {
        running = true;
        thread.start();
    }

    /**
     * 不再接受新的连接请求,已有连接仍会处理.
     */
    public void stop() {
        running = false;
        thread.stop();
        thread = new ServerListenThread(port, handler);
    }

    public boolean isRunning() {
        return running;
    }
}

class ServerListenThread extends Thread {
    int port;
    SocketHandler handler;

    public ServerListenThread(int _port, SocketHandler _handler) {
        port = _port;
        handler = _handler;
    }

    @Override
    public void run() {
//        System.out.println("socket server listen thread started.");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();  //等待客户端连接
                new Thread(() -> {
                    //异步处理连接
                    handler.handle(socket);

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}