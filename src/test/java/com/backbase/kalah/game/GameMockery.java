package com.backbase.kalah.game;

import com.backbase.kalah.game.model.Game;

public class GameMockery {

    public static Game newGame(){
        return Game.startGame();
    }

    public static Game gameInPlay() {
        return Game.startGame();
    }
}
