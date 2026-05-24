package com.videoprocessor.monitorservice.metrics;

import com.videoprocessor.shared.MetricsSnapshot;

public class MetricsMessage {

    private String type;

    private long timestamp;

    private MetricsSnapshot payload;

    public MetricsMessage(
            String type,
            long timestamp,
            MetricsSnapshot payload
    ) {

        this.type = type;

        this.timestamp = timestamp;

        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MetricsSnapshot getPayload() {
        return payload;
    }
}