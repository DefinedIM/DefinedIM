package org.definedim;

public class Crypto {
    public Crypto() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("windows") >= 0) {
            System.load(".native\\crypt_utility.dll");
        } else if (OS.indexOf("linux") >= 0) {
            System.load(".native\\crypt_utility.so");
        } else {
            System.err.print("Unsupported Platform");
        }
    }

    static native String getBlake3Sum(String str);
    static native String newSecretKey();
    static native String genPublicKey(String str);
    static native String encryptString(String pubKey, String rawData);
    static native String decryptString(String secKey, String cryptedData);
}
