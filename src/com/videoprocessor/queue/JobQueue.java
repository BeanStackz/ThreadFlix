package com.videoprocessor.queue;

import com.videoprocessor.job.Job;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobQueue {

    private final BlockingQueue<Job> queue;

    public JobQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public int size() {

        return queue.size();
    }

    public void submitJob(Job job) throws InterruptedException {
        queue.put(job);
        System.out.println(
                "Current queue size: " + queue.size()
        );

        System.out.println(
                "[QUEUE] Job submitted: " + job
        );
    }

    public Job takeJob() throws InterruptedException {
        return queue.take();
    }
}