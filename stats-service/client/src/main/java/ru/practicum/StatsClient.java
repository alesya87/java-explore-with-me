package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String PATH_HIT = "/hit";
    private static final String PATH_STATS = "/stats";

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> save(HitAddDto hitAddDto) {
        return post(PATH_HIT, hitAddDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get(PATH_STATS + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
