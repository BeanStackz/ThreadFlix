package com.videoprocessor.processor;

import com.videoprocessor.job.Job;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VideoProcessor {

    private final FFmpegExecutor executor;

    private void ensureDirectoriesExist() {

        new java.io.File("thumbnails")
                .mkdirs();

        new java.io.File("output/480p")
                .mkdirs();

        new java.io.File("output/720p")
                .mkdirs();
    }

    public VideoProcessor(
            FFmpegExecutor executor
    ) {

        this.executor = executor;
    }

    public PipelineResult process(Job job) {

        try {
            ensureDirectoriesExist();

            return decodeVideo(job)

                    .thenCompose(this::generateArtifacts)

                    .thenApply(success -> {

                        if (success) {

                            return new PipelineResult(
                                    true,
                                    "All artifacts generated"
                            );
                        }

                        return new PipelineResult(
                                false,
                                "Artifact generation failed"
                        );
                    })

                    .orTimeout(
                            5,
                            TimeUnit.MINUTES
                    )

                    .exceptionally(ex -> {

                        System.out.println(
                                "[PIPELINE ERROR] "
                                        + ex.getMessage()
                        );

                        return new PipelineResult(
                                false,
                                "Pipeline failed"
                        );
                    })

                    .join();

        } catch (Exception e) {

            return new PipelineResult(
                    false,
                    "Fatal pipeline error"
            );
        }
    }

    // ---------------- DECODE ----------------

    private CompletableFuture<Job> decodeVideo(
            Job job
    ) {

        return CompletableFuture.supplyAsync(() -> {

            try {

                String threadName =
                        Thread.currentThread().getName();

                System.out.println(
                        "[" + threadName + "] "
                                + "DECODE -> "
                                + job.getInputPath()
                );

                Thread.sleep(1000);

                System.out.println(
                        "[" + threadName + "] "
                                + "DECODE COMPLETED"
                );

                return job;

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "Decode interrupted"
                );
            }
        });
    }

    // ---------------- PARALLEL ARTIFACTS ----------------

    private CompletableFuture<Boolean>
    generateArtifacts(Job job) {

        CompletableFuture<Boolean> thumbnailFuture =
                generateThumbnail(job);

        CompletableFuture<Boolean> transcode480Future =
                generate480p(job);

        CompletableFuture<Boolean> transcode720Future =
                generate720p(job);

        return CompletableFuture.allOf(
                thumbnailFuture,
                transcode480Future,
                transcode720Future
        ).thenApply(v -> {

            List<Boolean> results = List.of(
                    thumbnailFuture.join(),
                    transcode480Future.join(),
                    transcode720Future.join()
            );

            return results.stream()
                    .allMatch(Boolean::booleanValue);
        });
    }

    // ---------------- THUMBNAIL ----------------

    private CompletableFuture<Boolean>
    generateThumbnail(Job job) {

        return CompletableFuture.supplyAsync(() -> {

            String outputPath =
                    "thumbnails/"
                            + job.getJobId()
                            + ".jpg";

            String[] command =
                    FFmpegCommandBuilder
                            .thumbnailCommand(
                                    job.getInputPath(),
                                    outputPath
                            );

            System.out.println(
                    "[THUMBNAIL] "
                            + outputPath
            );

            return executor.execute(command);
        });
    }

    // ---------------- 480P ----------------

    private CompletableFuture<Boolean>
    generate480p(Job job) {

        return CompletableFuture.supplyAsync(() -> {

            String outputPath =
                    "output/480p/"
                            + job.getJobId()
                            + "_480p.mp4";

            String[] command =
                    FFmpegCommandBuilder
                            .transcode480p(
                                    job.getInputPath(),
                                    outputPath
                            );

            System.out.println(
                    "[480P] "
                            + outputPath
            );

            return executor.execute(command);
        });
    }

    // ---------------- 720P ----------------

    private CompletableFuture<Boolean>
    generate720p(Job job) {

        return CompletableFuture.supplyAsync(() -> {

            String outputPath =
                    "output/720p/"
                            + job.getJobId()
                            + "_720p.mp4";

            String[] command =
                    FFmpegCommandBuilder
                            .transcode720p(
                                    job.getInputPath(),
                                    outputPath
                            );

            System.out.println(
                    "[720P] "
                            + outputPath
            );

            return executor.execute(command);
        });
    }
}