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
     * @param a string 
     * @return blake3 hash sum of the string
     */
    public native String getBlake3Sum(String str);

    /**
     *
     * @return a new secret key(HEX)
     */
    public native String newSecretKey();

    /**
     *
     * @param a secret key(HEX)
     * @return a public key(HEX)
     */
    public native String genPublicKey(String str);

    /**
     *
     * @param a public key(HEX)
     * @param the data that need to be encrypt
     * @return
     */
    public native String encryptString(String pubKey, String rawData);

    /**
     *
     * @param secKey(HEX)
     * @param encryptedData the data that need to be decrypt
     * @return
     */
    public native String decryptString(String secKey, String encryptedData);
}
