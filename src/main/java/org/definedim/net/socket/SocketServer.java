package org.definedim.net.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private int port;
    private ServerListenThread thread;
    private SocketHandler handler;

    public SocketServer(int _port, SocketHandler _handler) {
        port = _port;
        handler = _handler;
        thread = new ServerListenThread(port, handler);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.stop();
        thread = new ServerListenThread(port, handler);
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