package com.videoprocessor.processor;

public enum ProcessingStage {

    DECODE,

    EXTRACT_METADATA,

    GENERATE_THUMBNAIL,

    TRANSCODE_480P,
    TRANSCODE_720P,
    TRANSCODE_1080P,

    COMPRESS,

    UPLOAD
}