package org.definedim.plugin;

/**
 * 插件应打包成一个jar, 在jar顶层放一个 plugin.json 文件, 内容如下:
 * {
 * "name":"字符串,你的插件名",
 * "version":"字符串,你的插件版本",
 * "author":"字符串,作者",
 * "classpath":"字符串,继承org.definedim.plugin.DefinedIMPlugin类的类的完整路径,即 包名.类名"
 * "link":"字符串,相关链接(本项可选)"
 * }
 */

public class DefinedIMPluginConfig {
    public String name;
    public String version;
    public String author;
    public String classpath;
    public String link;
}
