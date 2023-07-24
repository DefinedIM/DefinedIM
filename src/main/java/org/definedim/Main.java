package org.definedim;

import org.definedim.crypto.RustSM2Crypto;
import org.definedim.exception.NativeLoadingException;
import org.definedim.file.TextFileReader;

public class Main {
    public static void main(String[] args) {
        try {
            TextFileReader tfr = new TextFileReader("build.gradle");
            System.out.println(tfr.readAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}