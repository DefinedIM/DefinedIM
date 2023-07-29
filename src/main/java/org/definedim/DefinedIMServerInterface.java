package org.definedim;

import org.definedim.config.DefinedIMConfig;
import org.definedim.crypto.RustSM2Crypto;
import org.definedim.net.socket.SocketServer;
import org.definedim.plugin.PluginManager;

public interface DefinedIMServerInterface {
    DefinedIMConfig definedIMConfig = null;

    SocketServer socketServer = null;

    RustSM2Crypto rustSM2Crypto = null;

    PluginManager pluginManager = null;
}
