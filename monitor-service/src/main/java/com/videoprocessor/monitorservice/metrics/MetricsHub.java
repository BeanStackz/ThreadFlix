package com.videoprocessor.monitorservice.metrics;

import org.springframework.stereotype.Component;
import com.videoprocessor.shared.MetricsSnapshot;

@Component
public class MetricsHub {

    private volatile com.videoprocessor.shared.MetricsSnapshot
            latestSnapshot =
            new MetricsSnapshot();

    public void updateSnapshot(
            MetricsSnapshot snapshot
    ) {

        latestSnapshot = snapshot;
    }

    public MetricsSnapshot getLatestSnapshot() {

        return latestSnapshot;
    }
}