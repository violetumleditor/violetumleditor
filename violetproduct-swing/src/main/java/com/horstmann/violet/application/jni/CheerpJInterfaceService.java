package com.horstmann.violet.application.jni;

import java.io.IOException;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.PostConstruct;


@ManagedBean
public class CheerpJInterfaceService {
    

    @PostConstruct
    public void initialize() {
        if (CheerpJInterfaceService.isJavaScriptBridgeAvailable()) {
            // Long-running Java thread to enable callbacks from JavaScript (CheerpJ) into our Java application
            new Thread(() -> {
                try {
                    CheerpJInterfaceService.nativeSetApplication(this);
                    System.out.println("Starting Thread");
                } catch (Throwable t) {
                    // Do nothing, this is expected to fail when not running as an applet, and the application will work fine without it
                }
            }).start();
        }
    }

    
    /**
     * Implemented in JavaScript, called by CheerpJ to set the application instance in JavaScript
     */
    public static native void nativeSetApplication(CheerpJInterfaceService appInstance);


    /**
     * Implemented in JavaScript, called by CheerpJ when the application is launched as an applet
     */
    public static native void nativeMethodCallback();


    /**
     * Implemented in JavaScript, called by Java to trigger a browser download.
     */
    public static native void nativeDownloadFile(String filename, String mimeType, byte[] content);


    /**
     * Detects whether JavaScript callbacks are available (CheerpJ applet runtime).
     */
    public static boolean isJavaScriptBridgeAvailable() {
        try {
            nativeMethodCallback();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }


    /**
     * Triggers a browser download through the JavaScript bridge.
     */
    public static void downloadFile(String filename, String mimeType, byte[] content) throws IOException {
        try {
            nativeDownloadFile(filename, mimeType, content);
        } catch (Throwable t) {
            throw new IOException("Browser download bridge is not available", t);
        }
    }

}
