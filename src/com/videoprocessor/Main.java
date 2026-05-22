package com.videoprocessor;

import com.videoprocessor.job.Job;
import com.videoprocessor.metrics.MetricsTracker;
import com.videoprocessor.queue.JobQueue;
import com.videoprocessor.status.JobStatusTracker;
import com.videoprocessor.worker.WorkerManager;
import com.videoprocessor.retry.RetryHandler;
import com.videoprocessor.storage.VideoStorageScanner;
import com.videoprocessor.processor.FFmpegExecutor;
import com.videoprocessor.resource.FFmpegResourceManager;
// Metrics
import com.videoprocessor.telemetry.MetricsPublisher;

public class Main {

    public static void main(String[] args)
            throws Exception {


        JobQueue jobQueue = new JobQueue();

        MetricsTracker metricsTracker =
                new MetricsTracker();

        JobStatusTracker statusTracker =
                new JobStatusTracker();

        RetryHandler retryHandler =
                new RetryHandler(jobQueue);

        FFmpegResourceManager resourceManager =
                new FFmpegResourceManager(3);

        FFmpegExecutor ffmpegExecutor =
                new FFmpegExecutor(resourceManager);

        // Metrics Section
        MetricsPublisher metricsPublisher =
                new MetricsPublisher(
                        metricsTracker,
                        jobQueue,
                        resourceManager
                );
        // Metrics end

        WorkerManager manager =
                new WorkerManager(
                        metricsTracker,
                        statusTracker,
                        retryHandler,
                        ffmpegExecutor
                );

        // Initializing metrics
        metricsPublisher.start();
        manager.start(jobQueue);

        int totalJobs = 50000;

        VideoStorageScanner scanner =
                new VideoStorageScanner();

        var videos =
                scanner.scanVideos("videos");

        int jobCounter = 1;

        for (String videoPath : videos) {

            Job job = new Job(
                    "job-" + jobCounter++,
                    videoPath
            );

            statusTracker.markJobQueued(
                    job.getJobId()
            );

            jobQueue.submitJob(job);
        }

        while (true) {

            boolean queueEmpty =
                    jobQueue.size() == 0;

            boolean noActiveJobs =
                    metricsTracker
                            .getActiveJobs() == 0;

            if (queueEmpty && noActiveJobs) {

                break;
            }

            Thread.sleep(1000);
        }

        manager.shutdown();

        System.out.println(
                "\nTOTAL PROCESSED: "
                        + metricsTracker.getProcessedJobs()
        );
    }
}