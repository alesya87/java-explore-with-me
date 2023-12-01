package ru.practicum.event.util;

import org.springframework.http.ResponseEntity;
import ru.practicum.HitAddDto;
import ru.practicum.StatsClient;
import ru.practicum.event.model.Event;
import ru.practicum.excception.InternalException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stats {
    public static void addView(StatsClient statsClient, String uri, String ip) {
        statsClient.save(new HitAddDto("ewm-service", uri, ip, LocalDateTime.now()));
    }

    public static Long getViewsCount(StatsClient statsClient, Event event) {
        if (event.getPublishedOn() == null) {
            return 0L;
        }
        String[] uris = {"/events/" + event.getId()};

        ResponseEntity<Object> response = statsClient.getStats(event.getPublishedOn(), LocalDateTime.now(), uris, true);
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new InternalException("Произошла ошибка при получении просмотров");
        }
        List<Map<String, Integer>> stats = (ArrayList<Map<String, Integer>>) response.getBody();
        if (stats.size() != 0) {
            return Long.valueOf(stats.get(0).get("hits"));
        }
        return 0L;
    }
}