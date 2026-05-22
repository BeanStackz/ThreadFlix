package com.videoprocessor.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoStorageScanner {

    public List<String> scanVideos(
            String directoryPath
    ) {

        List<String> videos =
                new ArrayList<>();

        File directory =
                new File(directoryPath);

        File[] files =
                directory.listFiles();

        if (files == null) {
            return videos;
        }

        for (File file : files) {

            if (file.isFile()
                    && isVideoFile(file)) {

                videos.add(
                        file.getPath()
                );
            }
        }

        return videos;
    }

    private boolean isVideoFile(
            File file
    ) {

        String name =
                file.getName()
                        .toLowerCase();

        return name.endsWith(".mp4")
                || name.endsWith(".mov")
                || name.endsWith(".mkv");
    }
}