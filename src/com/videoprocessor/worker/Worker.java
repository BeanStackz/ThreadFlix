package com.videoprocessor.worker;

import com.videoprocessor.job.Job;
import com.videoprocessor.metrics.MetricsTracker;
import com.videoprocessor.processor.FFmpegExecutor;
import com.videoprocessor.processor.PipelineResult;
import com.videoprocessor.processor.VideoProcessor;
import com.videoprocessor.retry.RetryHandler;
import com.videoprocessor.status.JobStatusTracker;


public class Worker {

    private final MetricsTracker metricsTracker;

    private final JobStatusTracker statusTracker;

    private final VideoProcessor videoProcessor;

    private final RetryHandler retryHandler;

    public Worker(
            MetricsTracker metricsTracker,
            JobStatusTracker statusTracker,
            RetryHandler retryHandler,
            FFmpegExecutor executor
    ) {

        this.metricsTracker = metricsTracker;

        this.statusTracker = statusTracker;

        this.retryHandler = retryHandler;

        this.videoProcessor =
                new VideoProcessor(executor);
    }

    public void processJob(Job job)
            throws InterruptedException {

        String threadName =
                Thread.currentThread().getName();

        statusTracker.markJobProcessing(
                job.getJobId()
        );

        System.out.println(
                "\n[" + threadName + "] Processing Job: "
                        + job
        );

        PipelineResult result =
                videoProcessor.process(job);

        if (result.isSuccess()) {

            statusTracker.markJobCompleted(
                    job.getJobId()
            );

            metricsTracker.incrementProcessedJobs();

            System.out.println(
                    "[" + threadName + "] SUCCESS -> "
                            + job.getJobId()
            );

        } else {

            System.out.println(
                    "[" + threadName + "] FAILURE -> "
                            + result.getMessage()
            );

            boolean retried =
                    retryHandler.retry(job);

            if (!retried) {

                statusTracker.markJobFailed(
                        job.getJobId()
                );
            }
        }
    }
}