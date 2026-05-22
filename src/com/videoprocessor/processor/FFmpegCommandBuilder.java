package com.videoprocessor.processor;


public class FFmpegCommandBuilder {

    public static String[] thumbnailCommand(
            String input,
            String output
    ) {

        return new String[] {

                "ffmpeg",

                "-y",

                "-i", input,

                "-ss", "00:00:01",

                "-vframes", "1",

                output
        };
    }

    public static String[] transcode480p(
            String input,
            String output
    ) {

        return new String[] {

                "ffmpeg",

                "-y",

                "-i", input,

                "-vf", "scale=854:480",

                output
        };
    }

    public static String[] transcode720p(
            String input,
            String output
    ) {

        return new String[] {

                "ffmpeg",

                "-y",

                "-i", input,

                "-vf", "scale=1280:720",

                output
        };
    }
}