package com.videoprocessor.shared;

import java.io.Serializable;

public class MetricsSnapshot
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private int processedJobs;

    private int failedJobs;

    private int queueSize;

    private int activeFFmpegProcesses;

    // Required for serialization
    public MetricsSnapshot() {
    }

    // Main constructor
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

    public void setProcessedJobs(
            int processedJobs
    ) {

        this.processedJobs =
                processedJobs;
    }

    public int getFailedJobs() {
        return failedJobs;
    }

    public void setFailedJobs(
            int failedJobs
    ) {

        this.failedJobs =
                failedJobs;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(
            int queueSize
    ) {

        this.queueSize =
                queueSize;
    }

    public int getActiveFFmpegProcesses() {

        return activeFFmpegProcesses;
    }

    public void setActiveFFmpegProcesses(
            int activeFFmpegProcesses
    ) {

        this.activeFFmpegProcesses =
                activeFFmpegProcesses;
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