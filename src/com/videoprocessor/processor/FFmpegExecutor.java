//package com.videoprocessor.processor;
//
//import com.videoprocessor.com.videoprocessor.resource
//        .FFmpegResourceManager;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.concurrent.TimeUnit;
//
//public class FFmpegExecutor {
//
//    private final FFmpegResourceManager
//            resourceManager;
//
//    public FFmpegExecutor(
//            FFmpegResourceManager resourceManager
//    ) {
//
//        this.resourceManager =
//                resourceManager;
//    }
//
//    public boolean execute(
//            String[] command
//    ) {
//
//        Process process = null;
//
//        try {
//
//            // acquire permit
//            resourceManager.acquire();
//
//            ProcessBuilder builder =
//                    new ProcessBuilder(command);
//
//            builder.redirectErrorStream(true);
//
//            process = builder.start();
//
//            BufferedReader reader =
//                    new BufferedReader(
//                            new InputStreamReader(
//                                    process.getInputStream()
//                            )
//                    );
//
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//
//                System.out.println(line);
//            }
//
//            boolean completed =
//                    process.waitFor(
//                            2,
//                            TimeUnit.MINUTES
//                    );
//
//            if (!completed) {
//
//                process.destroyForcibly();
//
//                System.out.println(
//                        "[FFMPEG] Timeout"
//                );
//
//                return false;
//            }
//
//            return process.exitValue() == 0;
//
//        } catch (Exception e) {
//
//            System.out.println(
//                    "[FFMPEG ERROR] "
//                            + e.getMessage()
//            );
//
//            return false;
//
//        } finally {
//
//            // ALWAYS release
//            resourceManager.release();
//        }
//    }
//}


package com.videoprocessor.processor;

import com.videoprocessor.resource
        .FFmpegResourceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class FFmpegExecutor {

    private final FFmpegResourceManager
            resourceManager;

    public FFmpegExecutor(
            FFmpegResourceManager resourceManager
    ) {

        this.resourceManager =
                resourceManager;
    }

    public boolean execute(
            String[] command
    ) {

        Process process = null;

        try {

            resourceManager.acquire();

            ProcessBuilder builder =
                    new ProcessBuilder(command);

            builder.redirectErrorStream(true);

            process = builder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()
                            )
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println(
                        "[FFMPEG] " + line
                );
            }

            boolean completed =
                    process.waitFor(
                            2,
                            TimeUnit.MINUTES
                    );

            if (!completed) {

                process.destroyForcibly();

                System.out.println(
                        "[FFMPEG] Timeout"
                );

                return false;
            }

            return process.exitValue() == 0;

        } catch (Exception e) {

            System.out.println(
                    "[FFMPEG ERROR] "
                            + e.getMessage()
            );

            return false;

        } finally {

            resourceManager.release();
        }
    }
}