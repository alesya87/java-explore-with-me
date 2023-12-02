package ru.practicum.event.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.practicum.HitAddDto;
import ru.practicum.StatsClient;
import ru.practicum.StatsView;
import ru.practicum.event.model.Event;
import ru.practicum.excception.InternalException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
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

    public static Map<Long, Long> getViewsCount(StatsClient statsClient, List<Event> events) {
        Map<Long, Long> eventsViews = new HashMap<>();
        List<String> eventsUris = new ArrayList<>(events.size());
        List<LocalDateTime> publishedOns = new ArrayList<>(events.size());

        if (publishedOns.size() == 0) {
            events.forEach(event -> eventsViews.put(event.getId(), 0L));
            return eventsViews;
        }

        events.forEach(event -> {
            eventsUris.add(String.format("/events/%d", event.getId()));
            publishedOns.add(event.getPublishedOn());
        });

        ResponseEntity<Object> response = statsClient.getStats(Collections.min(publishedOns), LocalDateTime.now(),
                eventsUris.toArray(String[]::new), true);
        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new InternalException("Произошла ошибка при получении просмотров");
        }

        ObjectMapper mapper = new ObjectMapper();
        List<StatsView> statsViews;
        try {
            statsViews = mapper.readValue(mapper.writeValueAsString(response.getBody()), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new InternalException("Произошла ощибка при получении просмотров");
        }

        Map<Long, Long> eventStatsViews = new HashMap<>();
        statsViews.forEach(statsView -> {
            String uri = statsView.getUri();
            Long id = Long.parseLong(uri.substring(uri.lastIndexOf("/")));
            eventStatsViews.put(id, statsView.getHits());
        });

        events.forEach(event -> {
            eventsViews.put(event.getId(), eventStatsViews.getOrDefault(event.getId(), 0L));
        });

        return eventsViews;
    }
}