package com.horstmann.violet.application.jni;

import java.io.IOException;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.PostConstruct;

@ManagedBean
public class CheerpJInterfaceService {

    @PostConstruct
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

    public static native boolean nativeShowOpenDialog(String acceptedExtensions);

    public static native byte[] nativeGetFileData();

    public static native String nativeGetFileName();

    public static native boolean nativeShowSaveDialog(String defaultFilename, byte[] content, String mimeType);

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

    public static boolean showOpenDialog(String acceptedExtensions) throws IOException {
        try {
            return nativeShowOpenDialog(acceptedExtensions);
        } catch (Throwable t) {
            throw new IOException("File open dialog is not available", t);
        }
    }

    public static byte[] getFileData() throws IOException {
        try {
            return nativeGetFileData();
        } catch (Throwable t) {
            throw new IOException("Cannot retrieve file data", t);
        }
    }

    public static String getFileName() throws IOException {
        try {
            return nativeGetFileName();
        } catch (Throwable t) {
            throw new IOException("Cannot retrieve file name", t);
        }
    }

    public static boolean showSaveDialog(String defaultFilename, byte[] content, String mimeType) throws IOException {
        try {
            return nativeShowSaveDialog(defaultFilename, content, mimeType);
        } catch (Throwable t) {
            throw new IOException("File save dialog is not available", t);
        }
    }
}