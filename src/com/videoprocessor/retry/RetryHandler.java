package com.videoprocessor.retry;

import com.videoprocessor.job.Job;
import com.videoprocessor.queue.JobQueue;

public class RetryHandler {

    private static final int MAX_RETRIES = 3;

    private final JobQueue jobQueue;

    public RetryHandler(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    public boolean retry(Job job)
            throws InterruptedException {

        if (job.getRetryCount() >= MAX_RETRIES) {

            System.out.println(
                    "[RETRY] Max retries exceeded for "
                            + job.getJobId()
            );

            return false;
        }

        job.incrementRetryCount();

        System.out.println(
                "[RETRY] Retrying "
                        + job.getJobId()
                        + " attempt "
                        + job.getRetryCount()
        );

        Thread.sleep(2000);

        jobQueue.submitJob(job);

        return true;
    }
}