package com.videoprocessor.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricsTracker {

    private final AtomicInteger processedJobs =
            new AtomicInteger(0);

    public void incrementProcessedJobs() {

        int updatedValue =
                processedJobs.incrementAndGet();

        System.out.println(
                "[METRICS] Processed Jobs: "
                        + updatedValue
        );
    }

    public int getProcessedJobs() {
        return processedJobs.get();
    }
}