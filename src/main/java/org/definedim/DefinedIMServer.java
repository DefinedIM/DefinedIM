package org.definedim;

import org.definedim.config.DefinedIMConfig;
import org.definedim.crypto.RustSM2Crypto;
import org.definedim.exception.NativeLoadingException;
import org.definedim.net.socket.SocketHandler;
import org.definedim.net.socket.SocketServer;
import org.definedim.plugin.PluginManager;

import java.io.*;
import java.net.Socket;

public class DefinedIMServer {
    public DefinedIMConfig definedIMConfig;

    public SocketServer socketServer;

    public RustSM2Crypto rustSM2Crypto;

    public PluginManager pluginManager;

    public void init() {
        // 加载配置
        definedIMConfig = DefinedIMConfig.byJSONFile(new File("config.json"));

        // 加载native库
        try {
            rustSM2Crypto = new RustSM2Crypto();
        } catch (NativeLoadingException e) {
            throw new RuntimeException(e);
        }

        //加载插件
        pluginManager = new PluginManager();
        pluginManager.load();

    }

    public void start() {
        startSocketServer();
    }

    public void stop() {
        socketServer.stop();
        pluginManager.stop();
        System.out.println("Server stopped.");
    }

    private void startSocketServer() {
        socketServer = new SocketServer(definedIMConfig.socketPort, new DefinedIMSocketHandler());
        socketServer.start();
    }

}

class DefinedIMSocketHandler extends SocketHandler {
    /**
     * 此函数会在独立线程中处理客户端Socket连接
     *
     * @param clientSocket
     */
    @Override
    public void handle(Socket clientSocket) {
        System.out.println(clientSocket.getInetAddress() + " connected");
        try {
            InputStream is = clientSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            /* 测试代码 */
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            clientSocket.getOutputStream().write("test".getBytes());
            clientSocket.close();
            /* 测试代码结束 */
            System.out.println("socket closed.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
