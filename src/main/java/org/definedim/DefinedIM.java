package org.definedim;

import org.definedim.config.DefinedIMConfig;
import org.definedim.crypto.RustSM2Crypto;
import org.definedim.exception.NativeLoadingException;
import org.definedim.net.socket.SocketHandler;
import org.definedim.net.socket.SocketServer;
import org.definedim.plugin.PluginManager;

import java.io.*;
import java.net.Socket;

public class DefinedIM {
    public static DefinedIMServer definedIMServer;
    public static void run(){
        definedIMServer = new DefinedIMServer();
        definedIMServer.init();
//        definedIMServer.start();
    }
}
