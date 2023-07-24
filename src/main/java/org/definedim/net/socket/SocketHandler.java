package org.definedim.net.socket;

import java.net.Socket;

public abstract class SocketHandler {
    /**
     * 自行实现socket处理
     * @param socket
     */
    public abstract void handle(Socket socket);
}