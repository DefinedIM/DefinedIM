package org.definedim;

import org.definedim.config.DefinedIMConfig;
import org.definedim.net.socket.SocketHandler;
import org.definedim.net.socket.SocketServer;

import java.io.*;
import java.net.Socket;

public class DefinedIM {
    public DefinedIMConfig definedIMConfig;

    public SocketServer socketServer;

    public DefinedIM() {
        definedIMConfig = DefinedIMConfig.byJSONFile(new File("config.json"));
    }

    /**
     * 启动DefinedIM主程序
     */
    public void start() {
        startSocketServer();
    }

    /**
     * 停止DefinedIM主程序
     */
    public void stop(){
        socketServer.stop();
    }

    private void startSocketServer() {
        socketServer = new SocketServer(definedIMConfig.socketPort, new DefinedIMSocketHandler());
        socketServer.start();
    }

}

class DefinedIMSocketHandler extends SocketHandler {
    /**
     * 此函数会在独立线程中处理客户端Socket连接
     * @param clientSocket
     */
    @Override
    public void handle(Socket clientSocket) {
        System.out.println(clientSocket.getInetAddress()+" connected");
        try {
            InputStream is = clientSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            /* 测试代码 */
            String line;
            while((line = br.readLine())!=null){
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
