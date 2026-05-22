package com.videoprocessor.monitor;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MetricsServer {

    private static final int PORT = 8081;

    public void start() {

        System.out.println(
                "[MONITOR] Starting metrics server..."
        );

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            while (true) {

                Socket socket =
                        serverSocket.accept();

                Thread.ofVirtual().start(() -> {

                    handleClient(socket);
                });
            }

        } catch (Exception e) {

            System.out.println(
                    "[MONITOR ERROR] "
                            + e.getMessage()
            );
        }
    }

    private void handleClient(
            Socket socket
    ) {

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             socket.getInputStream()
                     )) {

            MetricsSnapshot snapshot =
                    (MetricsSnapshot)
                            input.readObject();

            System.out.println(snapshot);

        } catch (Exception e) {

            System.out.println(
                    "[CLIENT ERROR] "
                            + e.getMessage()
            );
        }
    }
}