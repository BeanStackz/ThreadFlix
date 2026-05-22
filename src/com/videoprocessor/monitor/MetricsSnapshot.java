package com.videoprocessor.monitor;

import java.io.Serializable;

public class MetricsSnapshot
        implements Serializable {

    private final int processedJobs;

    private final int failedJobs;

    private final int queueSize;

    private final int activeFFmpegProcesses;

    public MetricsSnapshot(
            int processedJobs,
            int failedJobs,
            int queueSize,
            int activeFFmpegProcesses
    ) {

        this.processedJobs =
                processedJobs;

        this.failedJobs =
                failedJobs;

        this.queueSize =
                queueSize;

        this.activeFFmpegProcesses =
                activeFFmpegProcesses;
    }

    public int getProcessedJobs() {
        return processedJobs;
    }

    public int getFailedJobs() {
        return failedJobs;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getActiveFFmpegProcesses() {
        return activeFFmpegProcesses;
    }

    @Override
    public String toString() {

        return """
                ==============================
                METRICS SNAPSHOT
                ==============================
                Processed Jobs      : %d
                Failed Jobs         : %d
                Queue Size          : %d
                Active FFmpeg       : %d
                ==============================
                """
                .formatted(
                        processedJobs,
                        failedJobs,
                        queueSize,
                        activeFFmpegProcesses
                );
    }
}