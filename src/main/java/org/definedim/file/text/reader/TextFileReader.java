package org.definedim.file.text.reader;

import java.io.*;

public class TextFileReader {
    File textFile;
    FileInputStream fis;
    InputStreamReader isr;
    BufferedReader br;

    public TextFileReader(File _textFile) {
        textFile = _textFile;
        try {
            fis = new FileInputStream(textFile);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextFileReader(String _textFile) {
        textFile = new File(_textFile);
        try {
            fis = new FileInputStream(textFile);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文件读取编码,并复位读指针
     *
     * @param charsetName
     */
    public void setEncoding(String charsetName) {
        try {
            isr = new InputStreamReader(fis, charsetName);
            br = new BufferedReader(isr);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从上次读取结束的地方读取一行,
     *
     * @return 一行内容, 若文件结束或失败则为null
     */
    public String readLine() {
        try {
            String line = br.readLine();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从上次读取结束的地方继续读取直到文件结束
     *
     * @return 剩余的全部内容, 文件结束则返回空串
     */
    public String readAll() {
        try {
            //br.reset();
            String all = "";
            String line;
            String lineSeparator = System.lineSeparator();
            while ((line = br.readLine()) != null) {
                //process the line
                all += line + lineSeparator;
            }
            return all;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
