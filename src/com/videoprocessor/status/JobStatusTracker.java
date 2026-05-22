package com.videoprocessor.status;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobStatusTracker {

    private final Map<String, String> jobStatuses =
            new ConcurrentHashMap<>();

    public void markJobQueued(String jobId) {
        jobStatuses.put(jobId, "QUEUED");
    }

    public void markJobProcessing(String jobId) {
        jobStatuses.put(jobId, "PROCESSING");
    }

    public void markJobCompleted(String jobId) {
        jobStatuses.put(jobId, "COMPLETED");
    }

    public void markJobFailed(String jobId) {
        jobStatuses.put(jobId, "FAILED");
    }

    public String getStatus(String jobId) {
        return jobStatuses.get(jobId);
    }

    public void printAllStatuses() {

        System.out.println(
                "\n===== JOB STATUSES ====="
        );

        jobStatuses.forEach((jobId, status) ->
                System.out.println(
                        jobId + " -> " + status
                ));
    }
}