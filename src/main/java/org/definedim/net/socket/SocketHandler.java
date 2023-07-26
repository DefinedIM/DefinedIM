package org.definedim.net.socket;

import java.net.Socket;

public abstract class SocketHandler {
    /**
     * 自行实现socket处理,必须保证线程能正常退出
     *
     * @param socket
     */
    public abstract void handle(Socket socket);
}