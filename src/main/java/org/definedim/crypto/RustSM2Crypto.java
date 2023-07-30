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
            libFile = new File("./native/libsm2_crypto.so");
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
     * @param str a string
     * @return blake3 hash sum of the str
     */
    public native String getBlake3Sum(String str);

    /** generate a new secret key
     * @return a new secret key (HEX without leading 0x)
     */
    public native String newSecretKey();

    /** get public key by secret key
     * @param str secret key (HEX without leading 0x)
     * @return a public key (HEX without leading 0x)
     */
    public native String genPublicKey(String str);

    /**
     * @param pubKey public key (HEX without leading 0x)
     * @param rawData the data which needs to be encrypted
     * @return encrypted string
     */
    public native String encryptString(String pubKey, String rawData);

    /**
     * @param secKey secret key (HEX without leading 0x)
     * @param encryptedData the data which needs to be decrypted
     * @return decrypted string
     */
    public native String decryptString(String secKey, String encryptedData);
}
