package com.backbase.kalah.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Document(value = "games")
public class Game {
    @Id
    private final UUID id;
    private final Map<Integer, Integer> map;

    @PersistenceConstructor
    Game(UUID id, Map<Integer, Integer> map) {
        this.id = id;
        this.map = map;
    }

    public static Game startGame() {
        final var gameMap = IntStream.rangeClosed(1, 14)
                .boxed()
                .collect(toUnmodifiableMap(it -> it, data -> 0));

        return new Game(UUID.randomUUID(), gameMap);
    }

    public UUID getId() {
        return id;
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id) && map.equals(game.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, map);
    }
}
