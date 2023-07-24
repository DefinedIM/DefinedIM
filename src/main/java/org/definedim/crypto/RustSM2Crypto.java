package org.definedim.crypto;

import org.definedim.exception.NativeLoadingException;

import java.io.File;

public class RustSM2Crypto {

    public RustSM2Crypto() throws NativeLoadingException {
        String OS = System.getProperty("os.name").toLowerCase();
        File libFile;
        if (OS.indexOf("windows") >= 0) {
            libFile = new File("./native/sm2_crypto.dll");
        } else if (OS.indexOf("linux") >= 0) {
            libFile = new File("./native/sm2_crypto.so");
        } else {
            throw new NativeLoadingException("RustSM2Crypto: unsupported platform");
        }
        if (libFile.exists()) {
            //必须使用绝对路径
            System.load(libFile.getAbsolutePath());
        } else {
            throw new NativeLoadingException("RustSM2Crypto: could not load lib file " + libFile.getPath());
        }
    }

    /**
     *
     * @param str
     * @return
     */
    public native String getBlake3Sum(String str);

    /**
     *
     * @return
     */
    public native String newSecretKey();

    /**
     *
     * @param str
     * @return
     */
    public native String genPublicKey(String str);

    /**
     *
     * @param pubKey
     * @param rawData
     * @return
     */
    public native String encryptString(String pubKey, String rawData);

    /**
     *
     * @param secKey
     * @param encryptedData
     * @return
     */
    public native String decryptString(String secKey, String encryptedData);
}
