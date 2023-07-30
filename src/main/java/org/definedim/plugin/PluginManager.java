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
    File pluginDir;
    ArrayList<DefinedIMPlugin> pluginList = new ArrayList<>();
    int pluginCount = 0;

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
                //System.out.println("unknown plugin file " + pluginfile.getName());
            }
        }

        System.out.println("loaded " + pluginCount + " plugins from ./plugins/ , listed here:");
        if (pluginCount == 0) {
            System.out.println("  (none)");
        } else {
            for (int i = 0; i < pluginCount; i++) {
                System.out.println("  " + (i + 1) + ". " + pluginList.get(i).config.name + ":" + pluginList.get(i).config.version);
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
                        System.out.println("can't understand plugin.json as config from " + pluginFile.getName());
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
                plugin.onLoad();
                return true;
            } else {
                System.out.println("plugin config load failed. (" + pluginFile.getName() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static URLClassLoader loadJar(File _jarFile) throws Exception {
        // 创建一个URL对象，指向jar文件的路径
        URL url = _jarFile.toURI().toURL();
        // 得到出程序类加载器
        ClassLoader parentClassLoader = DefinedIM.class.getClassLoader();
        // 创建一个URLClassLoader对象，传入url作为参数
//        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, parentClassLoader);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, ClassLoader.getSystemClassLoader());
        // 创建一个List<Class<?>>对象，用于存放加载的类
//        List<Class<?>> classes = new ArrayList<>();
        // 判断url的协议是否是jar
        if (/*url.getProtocol().equals("jar")*/true) {
            // 获取jar文件对象
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
                    // 将加载的类添加到List中
//                    classes.add(clazz);
//                    System.out.println(clazz.getName());
                }
            }
        }
        // 返回加载的类列表
        return urlClassLoader;
    }


    static boolean isValidJavaIdentifier(String className) {
        //确定是否允许将指定字符作为 Java 标识符中的首字符。
        if (className.length() == 0 || !Character.isJavaIdentifierStart(className.charAt(0))) {
            return false;
        }
        String name = className.substring(1);
        for (int i = 0; i < name.length(); i++) {
            //确定指定字符是否可以是 Java 标识符中首字符以外的部分。
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}