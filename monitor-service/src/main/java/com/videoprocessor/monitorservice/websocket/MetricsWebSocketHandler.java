package com.videoprocessor.monitorservice.websocket;

import com.google.gson.Gson;

import com.videoprocessor.monitorservice.metrics.MetricsHub;
import com.videoprocessor.monitorservice.metrics.MetricsMessage;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import org.springframework.web.socket.handler
        .TextWebSocketHandler;

import java.util.Set;

import java.util.concurrent
        .ConcurrentHashMap;

@Component
public class MetricsWebSocketHandler
        extends TextWebSocketHandler {

    private final Set<WebSocketSession>
            sessions =
            ConcurrentHashMap.newKeySet();

    private final MetricsHub metricsHub;

    private final Gson gson =
            new Gson();

    public MetricsWebSocketHandler(
            MetricsHub metricsHub
    ) {

        this.metricsHub = metricsHub;
    }

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session
    ) {

        sessions.add(session);

        System.out.println(
                "[WS] Client connected"
        );
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) {

        sessions.remove(session);

        System.out.println(
                "[WS] Client disconnected"
        );
    }

    @PostConstruct
    public void startBroadcastLoop() {

        Thread.ofVirtual().start(() -> {

            while (true) {

                try {

                    broadcastMetrics();

                    Thread.sleep(2000);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }

    private void broadcastMetrics()
            throws Exception {

        MetricsMessage message =
                new MetricsMessage(
                        "METRICS",
                        System.currentTimeMillis(),
                        metricsHub
                                .getLatestSnapshot()
                );

        String json =
                gson.toJson(message);

        for (WebSocketSession session
                : sessions) {

            if (session.isOpen()) {

                System.out.println(
                        "[WS] Broadcasting metrics"
                );

                session.sendMessage(
                        new TextMessage(json)
                );
            }
        }
    }
}