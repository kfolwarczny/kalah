package com.backbase.kalah.web.model;

public record GameCreatedResponse(
        long id,
        String uri
) {
    private static final String BASE_URI = "http://localhost:8080/games/"; //Host and port should be calculated or taken from properties

    public GameCreatedResponse(long id) {
        this(id, BASE_URI + id);
    }
}
