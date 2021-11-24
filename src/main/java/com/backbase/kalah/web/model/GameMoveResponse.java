package com.backbase.kalah.web.model;

import java.util.Map;
import java.util.UUID;

public record GameMoveResponse(
        UUID id,
        String uri,
        Map<Integer, Integer> status
) {
    private static final String BASE_URI = "http://localhost:8080/games/"; //Host and port should be calculated or taken from properties

    public GameMoveResponse(UUID gameId, Map<Integer, Integer> result) {
        this(gameId, BASE_URI + gameId, result);
    }
}
