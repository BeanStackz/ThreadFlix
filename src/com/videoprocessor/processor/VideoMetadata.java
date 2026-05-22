package com.videoprocessor.processor;

public class VideoMetadata {

    private final String format;

    private final int duration;

    public VideoMetadata(
            String format,
            int duration
    ) {
        this.format = format;
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {

        return "Metadata{format='%s', duration=%d}"
                .formatted(format, duration);
    }
}