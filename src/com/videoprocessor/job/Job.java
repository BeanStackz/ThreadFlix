package com.videoprocessor.job;
public class Job {
    private final String jobId;
    private final String inputPath;
    private int retryCount;
    public Job(
            String jobId,
            String inputPath
    ) {
        this.jobId = jobId;
        this.inputPath = inputPath;
        this.retryCount = 0;
    }
    public String getJobId() {
        return jobId;
    }
    public String getInputPath() {
        return inputPath;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public void incrementRetryCount() {
        retryCount++;
    }

    @Override
    public String toString() {
        return "Job{id=" + jobId + ", inputPath=" + inputPath + ", retryCount=" + retryCount + '}';
    }
}