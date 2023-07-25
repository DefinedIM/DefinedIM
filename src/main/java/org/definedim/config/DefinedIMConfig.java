package org.definedim.config;

import com.alibaba.fastjson2.JSON;
import org.definedim.file.reader.TextFileReader;

import java.io.File;

public class DefinedIMConfig {
    /**
     * 解析JSON字符串作为配置
     * @param json
     * @return 解析后的配置
     */
    public static DefinedIMConfig byJSON(String json) {
        DefinedIMConfig definedIMConfig = JSON.parseObject(json, DefinedIMConfig.class);
        return definedIMConfig;
    }

    /**
     * 解析JSON文件作为配置,指定文件编码
     * @param jsonFile
     * @param encoding
     * @return 解析后的配置
     */
    public static DefinedIMConfig byJSONFile(File jsonFile, String encoding) {
        TextFileReader textFileReader = new TextFileReader(jsonFile);
        textFileReader.setEncoding(encoding);
        String json = textFileReader.readAll();
        return byJSON(json);
    }

    /**
     * 解析JSON文件作为配置, 使用UTF-8编码
     * @param jsonFile
     * @return 解析后的配置
     */
    public static DefinedIMConfig byJSONFile(File jsonFile) {
        return byJSONFile(jsonFile, "UTF-8");
    }

    public int socketPort; // 内置SocketServer欲使用的端口


}
