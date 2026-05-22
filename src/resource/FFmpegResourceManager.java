package com.videoprocessor.resource;

import java.util.concurrent.Semaphore;

public class FFmpegResourceManager {

    // max concurrent FFmpeg processes
    private final Semaphore semaphore;

    public FFmpegResourceManager(
            int maxConcurrentProcesses
    ) {

        this.semaphore =
                new Semaphore(
                        maxConcurrentProcesses
                );
    }

    public void acquire()
            throws InterruptedException {

        semaphore.acquire();

        System.out.println(
                "[RESOURCE] Permit acquired | Available: "
                        + semaphore.availablePermits()
        );
    }

    public void release() {

        semaphore.release();

        System.out.println(
                "[RESOURCE] Permit released | Available: "
                        + semaphore.availablePermits()
        );
    }
}