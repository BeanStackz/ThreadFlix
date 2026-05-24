package com.videoprocessor.monitorservice.tcp;

import com.videoprocessor.monitorservice.metrics.MetricsHub;
import com.videoprocessor.shared.MetricsSnapshot;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class MetricsTcpServer {

    private static final String HOST = "localhost";

    private static final int PORT = 8081;

    private final MetricsHub metricsHub;

    public MetricsTcpServer(
            MetricsHub metricsHub
    ) {

        this.metricsHub = metricsHub;
    }

    @PostConstruct
    public void start() {

        Thread.ofVirtual().start(() -> {

            try (
                    ServerSocket serverSocket =
                            new ServerSocket(PORT)
            ) {

                System.out.println(
                        "[TCP] Listening on 8081"
                );

                while (true) {

                    Socket socket =
                            serverSocket.accept();

                    Thread.ofVirtual()
                            .start(() -> {

                                handle(socket);
                            });
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }

    private void handle(
            Socket socket
    ) {

        try (
                ObjectInputStream input =
                        new ObjectInputStream(
                                socket.getInputStream()
                        )
        ) {
            System.out.println(
                    "[TCP] Client connected"
            );

            MetricsSnapshot snapshot =
                    (MetricsSnapshot)
                            input.readObject();

            metricsHub.updateSnapshot(
                    snapshot
            );

            System.out.println(
                    "[TCP] Snapshot received: "
                            + snapshot
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}