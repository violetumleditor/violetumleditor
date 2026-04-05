package com.horstmann.violet.application.cheeprj;

import java.io.IOException;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

@ManagedBean
public class CheerpJInterfaceService {


    public CheerpJInterfaceService() {
        initialize();
    }

    public void initialize() {
        if (CheerpJInterfaceService.isJavaScriptBridgeAvailable()) {
            new Thread(() -> {
                try {
                    CheerpJInterfaceService.nativeSetApplication(this);
                    System.out.println("Starting Thread");
                } catch (Throwable t) {
                    // Ignore. The desktop build runs without the JavaScript bridge.
                }
            }).start();
        }
    }

    public static native void nativeSetApplication(CheerpJInterfaceService appInstance);

    public static native void nativeMethodCallback();

    public static native void nativeDownloadFile(String filename, String mimeType, byte[] content);

    public static boolean isJavaScriptBridgeAvailable() {
        try {
            nativeMethodCallback();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static void downloadFile(String filename, String mimeType, byte[] content) throws IOException {
        try {
            nativeDownloadFile(filename, mimeType, content);
        } catch (Throwable t) {
            throw new IOException("Browser download bridge is not available", t);
        }
    }
}