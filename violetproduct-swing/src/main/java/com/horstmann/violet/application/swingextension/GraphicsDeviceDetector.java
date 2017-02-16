package com.horstmann.violet.application.swingextension;

import com.horstmann.violet.application.gui.MainFrame;

import java.awt.*;


/**
 * Graphics Device Detector
 */
public class GraphicsDeviceDetector {
    private MainFrame mainFrame;

    public GraphicsDeviceDetector(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Detection which graphics device is used
     *
     * @return currently active graphics device
     */
    public GraphicsDevice detect() {
        final GraphicsConfiguration config = mainFrame.getGraphicsConfiguration();
        final GraphicsDevice graphicsDevice = config.getDevice();
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] allGraphicsDevices = env.getScreenDevices();
        return GetCurrentGraphicsDevice(allGraphicsDevices, graphicsDevice);
    }

    /**
     * gets currently active graphics device
     *
     * @param allGraphicsDevices all graphics devices
     * @param activeGraphicsDevice active graphics device
     * @return GraphicsDevice
     */
    private GraphicsDevice GetCurrentGraphicsDevice(GraphicsDevice[] allGraphicsDevices, GraphicsDevice activeGraphicsDevice) {
        for (GraphicsDevice graphicsDevice : allGraphicsDevices) {
            if (graphicsDevice.equals(activeGraphicsDevice)) {
                return graphicsDevice;
            }
        }

        throw new RuntimeException("No graphics device found");
    }
}
