package org.definedim;

import org.definedim.crypto.RustSM2Crypto;
import org.definedim.exception.NativeLoadingException;

public class Main {
    public static void main(String[] args) {
        try {
            RustSM2Crypto rustSM2Crypto = new RustSM2Crypto();
            System.out.println(rustSM2Crypto.getBlake3Sum("12233234234"));
        } catch (NativeLoadingException e) {
            e.printStackTrace();
        }
    }
}