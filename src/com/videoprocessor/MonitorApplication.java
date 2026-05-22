package com.videoprocessor;

import com.videoprocessor.monitor
        .MetricsServer;

public class MonitorApplication {

    public static void main(String[] args) {

        MetricsServer server =
                new MetricsServer();

        server.start();
    }
}