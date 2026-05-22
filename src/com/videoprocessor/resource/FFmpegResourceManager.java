package com.videoprocessor.resource;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class FFmpegResourceManager {

    private final Semaphore semaphore;

    private final AtomicInteger activeProcesses =
            new AtomicInteger(0);

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

        int active =
                activeProcesses.incrementAndGet();

        System.out.println(
                "[RESOURCE] Permit acquired | Active FFmpeg: "
                        + active
        );
    }

    public void release() {

        semaphore.release();

        int active =
                activeProcesses.decrementAndGet();

        System.out.println(
                "[RESOURCE] Permit released | Active FFmpeg: "
                        + active
        );
    }

    public int getActiveProcesses() {

        return activeProcesses.get();
    }
}