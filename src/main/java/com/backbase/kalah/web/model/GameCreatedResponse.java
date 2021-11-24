package com.backbase.kalah.web.model;

import java.util.UUID;

public record GameCreatedResponse(
        UUID id,
        String uri
) {
    private static final String BASE_URI = "http://localhost:8080/games/"; //Host and port should be calculated or taken from properties

    public GameCreatedResponse(UUID id) {
        this(id, BASE_URI + id);
    }
}
