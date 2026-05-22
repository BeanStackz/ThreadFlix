package com.videoprocessor.telemetry;

import com.videoprocessor.monitor
        .MetricsSnapshot;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class MetricsClient {

    private static final String HOST =
            "localhost";

    private static final int PORT = 8081;

    public void send(
            MetricsSnapshot snapshot
    ) {

        try (

                Socket socket =
                        new Socket(HOST, PORT);

                ObjectOutputStream output =
                        new ObjectOutputStream(
                                socket.getOutputStream()
                        )

        ) {

            output.writeObject(snapshot);

            output.flush();

        } catch (Exception e) {

            System.out.println(
                    "[TELEMETRY ERROR] "
                            + e.getMessage()
            );
        }
    }
}