package com.videoprocessor.telemetry;

import com.videoprocessor.metrics
        .MetricsTracker;
import com.videoprocessor.monitor
        .MetricsSnapshot;
import com.videoprocessor.queue
        .JobQueue;
import com.videoprocessor.resource
        .FFmpegResourceManager;

public class MetricsPublisher {

    private final MetricsTracker
            metricsTracker;

    private final JobQueue jobQueue;

    private final FFmpegResourceManager
            resourceManager;

    private final MetricsClient client =
            new MetricsClient();

    public MetricsPublisher(
            MetricsTracker metricsTracker,
            JobQueue jobQueue,
            FFmpegResourceManager resourceManager
    ) {

        this.metricsTracker =
                metricsTracker;

        this.jobQueue =
                jobQueue;

        this.resourceManager =
                resourceManager;
    }

    public void start() {

        Thread.ofVirtual().start(() -> {

            while (true) {

                try {

                    MetricsSnapshot snapshot =
                            new MetricsSnapshot(
                                    metricsTracker
                                            .getProcessedJobs(),

                                    metricsTracker
                                            .getFailedJobs(),

                                    jobQueue.size(),

                                    resourceManager
                                            .getActiveProcesses()
                            );

                    client.send(snapshot);

                    Thread.sleep(3000);

                } catch (Exception e) {

                    System.out.println(
                            "[PUBLISHER ERROR] "
                                    + e.getMessage()
                    );
                }
            }
        });
    }
}