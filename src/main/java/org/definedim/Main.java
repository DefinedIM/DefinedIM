package org.definedim;

public class Main {
    public static void main(String[] args) {
        DefinedIM definedIM = null;
        try {
            definedIM = new DefinedIM();
            definedIM.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}