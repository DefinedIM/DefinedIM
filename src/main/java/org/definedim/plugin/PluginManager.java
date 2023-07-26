package org.definedim.plugin;

import com.alibaba.fastjson2.JSON;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;

public class PluginManager {
    File pluginDir;
    ArrayList<DefinedIMPlugin> pluginList;

    public PluginManager() {
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
                } else {
                    System.out.println("can't load jar file " + pluginfile.getName() + " as a plugin!");
                }
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
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}