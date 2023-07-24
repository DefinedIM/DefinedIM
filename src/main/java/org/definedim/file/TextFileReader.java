package org.definedim.file;

import java.io.*;

public class TextFileReader {
    File textFile;

    public TextFileReader(File _textFile) {
        textFile = _textFile;
    }

    public TextFileReader(String _textFile) {
        textFile = new File(_textFile);
    }

    public String readAll() throws IOException {
        if (textFile.exists()) {
            FileInputStream fis = new FileInputStream(textFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String all = "";
            String line;
            String lineSeparator = System.lineSeparator();
            while ((line = br.readLine()) != null) {
                //process the line
                all += line + lineSeparator;
            }
            br.close();
            isr.close();
            fis.close();
            return all;
        }
        throw new IOException("File " + textFile.getName() + " doesn't exist.");
    }
}
