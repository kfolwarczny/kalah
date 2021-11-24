package com.backbase.kalah.game.model;

import java.util.List;
import java.util.UUID;

public class GameMockery {

    public static Game newGame() {
        return Game.startGame(List.of(Player.create(), Player.create()));
    }

    public static Game gameInPlay(UUID gameUUID) {
        return Game.startGame(List.of(PlayerMockery.sample(), PlayerMockery.sample())).withId(gameUUID)
                .move(2).get()
                .move(9).get()
                .move(1).get();
    }

    public static Game gameInPlay(UUID gameUUID, UUID playerOneUUID, UUID playerTwoUUID) {
        return Game.startGame(List.of(
                        PlayerMockery.sample(playerOneUUID),
                        PlayerMockery.sample(playerTwoUUID))
                )
                .withId(gameUUID)
                .move(2).get()
                .move(9).get()
                .move(1).get();
    }
}
