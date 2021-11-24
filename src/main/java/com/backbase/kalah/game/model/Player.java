package com.backbase.kalah.game.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Document(value = "players")
@With
public class Player {

    @Id
    private final UUID id;

    @PersistenceConstructor
    Player(UUID id) {
        this.id = id;
    }

    Player() {
        this(UUID.randomUUID());
    }

    public static Player create() {
        return new Player();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
