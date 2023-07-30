package org.definedim.plugin;

import com.alibaba.fastjson2.JSON;
import org.definedim.DefinedIM;
import org.definedim.Main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.*;

public class PluginManager {
    ArrayList<DefinedIMPlugin> pluginList = new ArrayList<>();
    int pluginCount = 0;

    /**
     * 加载./plugins/下的jar文件作为插件
     */
    public void load() {
        // 目录
        File pluginDir = new File("./plugins/");
        if (!pluginDir.isDirectory()) {
            pluginDir.mkdirs();
        }

        // 尝试加载 ./plugins/ 中所有的jar
        for (File pluginfile : pluginDir.listFiles()) {
            if (pluginfile.getName().endsWith(".jar")) {
                if (loadPluginFile(pluginfile)) {
                    //
                    System.out.println("loaded plugin " + pluginfile.getName());
                } else {
                    System.out.println("can't load jar file " + pluginfile.getName() + " as a plugin!");
                }
            }
        }

        // 输出概要信息
        System.out.println("loaded " + pluginCount + " plugins from ./plugins/ , listed here:");
        if (pluginCount == 0) {
            System.out.println("  (none)");
        } else {
            for (int i = 0; i < pluginCount; i++) {
                System.out.println("  " + (i + 1) + ". " + pluginList.get(i).config.name + ":" + pluginList.get(i).config.version);
            }
        }

        // 调用每个插件的onLoad()方法
        for (DefinedIMPlugin definedIMPlugin : pluginList) {
            definedIMPlugin.onLoad();
        }
    }

    /**
     * 尝试从jar文件中读取plugin.json并解析
     *
     * @param pluginFile
     * @return
     */
    boolean loadPluginFile(File pluginFile) {
        try {
            ZipFile zip = new ZipFile(pluginFile);
            InputStream zipFileInput = new FileInputStream(pluginFile);
            ZipInputStream zis = new ZipInputStream(zipFileInput);
            ZipEntry entry = null;
            DefinedIMPluginConfig config = null;
            while ((entry = zis.getNextEntry()) != null) {
                String filename = entry.getName();
                if (filename.equals("plugin.json")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
                    StringBuilder sb = new StringBuilder("");
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String content = sb.toString();
                    config = JSON.parseObject(content, DefinedIMPluginConfig.class);
                    if (config == null) {
                        // 此时无法解析配置的JSON文件
                        System.out.println("plugin.json from " + pluginFile.getName() + " is illegal.");
                        return false;
                    }
                    break;
                }
                zis.closeEntry();
            }
            zis.close();
            if (config != null) {
                URLClassLoader urlClassLoader = loadJar(pluginFile);
                Class aClass = urlClassLoader.loadClass(config.classpath);
                DefinedIMPlugin plugin = (DefinedIMPlugin) aClass.newInstance();
                pluginCount++;
                plugin.config = config;
                pluginList.add(plugin);
                plugin.init(DefinedIM.definedIMServer);
                return true;
            } else {
                // 此时jar中没有plugin.json
                System.out.println("can't find plugin.json in " + pluginFile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加载jar文件到JVM
     *
     * @param _jarFile
     * @return 使用的类加载器
     * @throws Exception
     */
    static URLClassLoader loadJar(File _jarFile) throws Exception {
        URL url = _jarFile.toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, ClassLoader.getSystemClassLoader());
        JarFile jarFile = new JarFile(_jarFile);
        // 获取jar文件中的所有条目
        Enumeration<JarEntry> entries = jarFile.entries();
        // 遍历所有条目
        while (entries.hasMoreElements()) {
            // 获取一个条目
            JarEntry jarEntry = entries.nextElement();
            // 获取条目的名称
            String entryName = jarEntry.getName();
            // 判断是否是一个类文件，并且是否在指定的包名下
            String lead = entryName.split("/")[0];
            if (entryName.endsWith(".class") && lead.toLowerCase().equals(lead)) {
                // 获取类文件的全限定名
                String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                // 使用URLClassLoader的loadClass方法来加载这个类文件
                Class<?> clazz = urlClassLoader.loadClass(className);
            }
        }
        // 返回加载的类列表
        return urlClassLoader;
    }

    /**
     * 得到所有加载的插件
     * @return
     */
    public ArrayList<DefinedIMPlugin> getPluginList() {
        return pluginList;
    }

    /**
     * 终止所有插件运行
     */
    public void stop() {
        for (DefinedIMPlugin definedIMPlugin : pluginList) {
            definedIMPlugin.onExit();
        }
    }

}