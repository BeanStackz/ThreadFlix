package com.videoprocessor.worker;

import com.videoprocessor.job.Job;
import com.videoprocessor.metrics.MetricsTracker;
import com.videoprocessor.processor.FFmpegExecutor;
import com.videoprocessor.queue.JobQueue;
import com.videoprocessor.retry.RetryHandler;
import com.videoprocessor.status.JobStatusTracker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WorkerManager {

    private final ExecutorService executorService;

    private final Worker worker;

    private final MetricsTracker metricsTracker;

    private Thread dispatcherThread;

    private volatile boolean running = true;

    public WorkerManager(
            MetricsTracker metricsTracker,
            JobStatusTracker statusTracker,
            RetryHandler retryHandler,
            FFmpegExecutor executor
    ) {

        this.metricsTracker =
                metricsTracker;

        this.executorService =
                Executors.newThreadPerTaskExecutor(
                        Thread.ofVirtual()
                                .name(
                                        "video-worker-",
                                        0
                                )
                                .factory()
                );

        this.worker = new Worker(
                metricsTracker,
                statusTracker,
                retryHandler,
                executor
        );
    }

    public void start(JobQueue jobQueue) {

        Runnable dispatcher = () -> {

            while (running) {

                try {

                    Job job = jobQueue.takeJob();

                    executorService.submit(() -> {

                        metricsTracker.incrementActiveJobs();

                        try {

                            worker.processJob(job);

                        } catch (InterruptedException e) {

                            Thread.currentThread()
                                    .interrupt();

                        } finally {

                            metricsTracker
                                    .decrementActiveJobs();
                        }
                    });

                } catch (InterruptedException e) {

                    System.out.println(
                            "[DISPATCHER] Interrupted"
                    );

                    Thread.currentThread()
                            .interrupt();

                    break;
                }
            }

            System.out.println(
                    "[DISPATCHER] Stopped"
            );
        };

        dispatcherThread =
                Thread.ofPlatform()
                        .name("dispatcher-thread")
                        .start(dispatcher);
    }

    public void shutdown()
            throws InterruptedException {

        System.out.println(
                "\n[MANAGER] Shutdown initiated..."
        );

        running = false;

        dispatcherThread.interrupt();

        executorService.shutdown();

        boolean terminated =
                executorService.awaitTermination(
                        10,
                        TimeUnit.SECONDS
                );

        if (!terminated) {

            executorService.shutdownNow();
        }

        System.out.println(
                "[MANAGER] Shutdown complete"
        );
    }

    public boolean isIdle(
            JobQueue jobQueue
    ) {

        return jobQueue.size() == 0
                && metricsTracker
                .getActiveJobs() == 0;
    }
}