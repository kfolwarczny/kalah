package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.GameInfrastructure;
import com.backbase.kalah.game.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class GameManager {

    private final GameInfrastructure gameInfrastructure;

    @Autowired
    public GameManager(GameInfrastructure gameInfrastructure) {
        this.gameInfrastructure = gameInfrastructure;
    }

    public Map<Integer, Integer> makeAMove(UUID gameId, long pitId) {
        return Map.of();
    }

    public Optional<Game> fetchGame(UUID gameId) {
        return gameInfrastructure.findById(gameId);
    }
}
