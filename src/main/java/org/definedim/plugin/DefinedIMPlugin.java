package org.definedim.plugin;

public class DefinedIMPlugin {
    public DefinedIMPluginConfig config; // 这项将会在插件加载的时候从plugin.json读取

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

}
