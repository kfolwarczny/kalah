package com.backbase.kalah.game;

import com.backbase.kalah.game.model.Game;
import com.backbase.kalah.game.model.Player;

import java.util.List;
import java.util.UUID;

public class GameMockery {

    public static Game newGame() {
        return Game.startGame(List.of(Player.create(), Player.create()));
    }

    public static Game gameInPlay(UUID uuid) {
        return Game.startGame(List.of(Player.create(), Player.create())).withId(uuid);
    }
}
