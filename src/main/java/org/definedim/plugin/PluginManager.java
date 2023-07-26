package org.definedim.plugin;

import com.alibaba.fastjson2.JSON;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.zip.*;

public class PluginManager {
    File pluginDir;
    ArrayList<DefinedIMPlugin> pluginList;

    public void load() {
        // 目录
        pluginDir = new File("./plugins/");
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
            } else {
                System.out.println("unknown plugin file " + pluginfile.getName());
            }
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
                        return false;
                    }
                    break;
                }
                zis.closeEntry(); // 关闭zipEntry
            }
            zis.close(); //关闭zipInputStream

            if (config != null) {
                loadJar(pluginFile.getAbsolutePath());
                Class<?> aClass = Class.forName(config.classpath);
                DefinedIMPlugin plugin = (DefinedIMPlugin) aClass.newInstance();
                plugin.onRegister();
            } else {
                System.out.println("config load failed. (" + pluginFile.getName() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    void loadJar(String jarPath) {
        File jarFile = new File(jarPath);
        // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        // 获取方法的访问权限以便写回
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = jarFile.toURI().toURL();
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(accessible);
        }
    }
}