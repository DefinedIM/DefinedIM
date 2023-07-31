package org.definedim.crypto;

import java.io.File;

import org.definedim.exception.NativeLoadingException;

public class RustSM2Crypto {

    public RustSM2Crypto() throws NativeLoadingException {
        String OS = System.getProperty("os.name").toLowerCase();
        File libFile;
        if (OS.contains("windows")) {
            libFile = new File("./native/sm2_crypto.dll");
        } else if (OS.contains("linux")) {
            libFile = new File("./native/libsm2_crypto.so");
        } else {
            throw new NativeLoadingException("RustSM2Crypto: unsupported platform");
        }
        if (libFile.exists()) {
            // 必须使用绝对路径
            System.load(libFile.getAbsolutePath());
        } else {
            throw new NativeLoadingException("RustSM2Crypto: could not load lib file " + libFile.getPath());
        }
    }

    /**
     * @param source a byte array
     * @return blake3 hash sum of the source
     */
    public native String getBlake3Sum(byte[] source);

    /**
     * generate a new secret key
     *
     * @return a new secret key (HEX without leading 0x)
     */
    public native String newSecretKey();

    /**
     * get public key by secret key
     *
     * @param str secret key (HEX without leading 0x)
     * @return a public key (HEX without leading 0x)
     */
    public native String genPublicKey(String str);

    /**
     * @param pubKey    public key (HEX without leading 0x)
     * @param rawString the string which needs to be encrypted
     * @return encrypted string (C1C2C3)
     */
    public native String encryptString(String pubKey, String rawString);

    /**
     * @param secKey          secret key (HEX without leading 0x)
     * @param encryptedString the string which needs to be decrypted
     * @return decrypted string (C1C2C3)
     */
    public native String decryptString(String secKey, String encryptedString);

    /**
     * @param pubKey  public key (HEX without leading 0x)
     * @param rawData the data which needs to be encrypted
     * @param length  length of the data
     * @return encrypted data array (C1C2C3)
     */
    public native byte[] encryptData(String pubKey, byte[] rawData, long length);

    /**
     * @param secKey  secret key (HEX without leading 0x)
     * @param encryptedData the data which needs to be decrypted
     * @param length  length of the encrypted data
     * @return decrypted data array (C1C2C3)
     */
    public native byte[] decryptData(String secKey, byte[] encryptedData, long length);
}
