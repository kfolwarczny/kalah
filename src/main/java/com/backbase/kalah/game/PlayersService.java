package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.PlayerInfrastructure;
import com.backbase.kalah.game.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlayersService {

    private final PlayerInfrastructure infrastructure;

    @Autowired
    public PlayersService(PlayerInfrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }

    List<Player> createPlayers() {
        final var playerOne = Player.create();
        final var playerTwo = Player.create();

        return infrastructure.insert(List.of(playerOne, playerTwo));
    }
}
