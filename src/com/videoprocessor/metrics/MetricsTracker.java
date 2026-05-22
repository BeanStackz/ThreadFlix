package com.videoprocessor.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricsTracker {

    private final AtomicInteger processedJobs =
            new AtomicInteger(0);

    private final AtomicInteger failedJobs =
            new AtomicInteger(0);

    private final AtomicInteger activeJobs =
            new AtomicInteger(0);

    public void incrementProcessedJobs() {

        int updatedValue =
                processedJobs.incrementAndGet();

        System.out.println(
                "[METRICS] Processed Jobs: "
                        + updatedValue
        );
    }

    public void incrementFailedJobs() {

        int updatedValue =
                failedJobs.incrementAndGet();

        System.out.println(
                "[METRICS] Failed Jobs: "
                        + updatedValue
        );
    }

    public int getProcessedJobs() {

        return processedJobs.get();
    }

    public int getFailedJobs() {

        return failedJobs.get();
    }

    public void incrementActiveJobs() {

        activeJobs.incrementAndGet();
    }

    public void decrementActiveJobs() {

        activeJobs.decrementAndGet();
    }

    public int getActiveJobs() {

        return activeJobs.get();
    }
}