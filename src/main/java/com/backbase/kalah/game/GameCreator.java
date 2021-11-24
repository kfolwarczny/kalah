package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.GameInfrastructure;
import com.backbase.kalah.game.model.Game;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class GameCreator {

    private final PlayersService playersService;
    private final GameInfrastructure gameInfrastructure;

    @Autowired
    public GameCreator(PlayersService playersService,
                       GameInfrastructure gameInfrastructure) {
        this.playersService = playersService;
        this.gameInfrastructure = gameInfrastructure;
    }


    public Try<UUID> createGame() {
        log.info("Starting new game...");

        return Try.of(() -> {
                    final var players = playersService.createPlayers();
                    final var newGame = Game.startGame(players);
                    return gameInfrastructure.insert(newGame)
                            .getId();
                })
                .onSuccess(uuid -> log.info("Game started with ID: {}", uuid));
    }

}
