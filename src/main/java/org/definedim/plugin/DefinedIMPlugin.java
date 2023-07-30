package org.definedim.plugin;

import org.definedim.DefinedIMServer;

public class DefinedIMPlugin {
    public DefinedIMPluginConfig config; // 这项将会在插件加载的时候从plugin.json读取

    private DefinedIMServer definedIMServer;

    /**
     * 在插件被加载时被调用,将阻塞主线程
     */
    public void onLoad() {
    }

    /**
     * 在执行reload时被调用,将阻塞主线程
     */
    public void onReload() {
    }

    /**
     * 在DefinedIM退出时调用,将阻塞主线程
     */
    public void onExit() {
    }

    public final void init(DefinedIMServer _definedIMServer) {
        definedIMServer = _definedIMServer;
    }

    public final DefinedIMServer getDefinedIMServer() {
        return definedIMServer;
    }

}
