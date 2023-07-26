package org.definedim.plugin;

public class DefinedIMPlugin {
    public DefinedIMPluginConfig config;

    /**
     * 在插件被加载时被调用,将阻塞主线程
     */
    public void onRegister() {
        System.out.println("plugin load done!");
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
