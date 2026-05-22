package com.videoprocessor.processor;

public class PipelineResult {

    private final boolean success;

    private final String message;

    public PipelineResult(
            boolean success,
            String message
    ) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}