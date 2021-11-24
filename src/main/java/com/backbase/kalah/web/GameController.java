package com.backbase.kalah.web;

import com.backbase.kalah.game.GameCreator;
import com.backbase.kalah.game.GameManager;
import com.backbase.kalah.web.model.GameCreatedResponse;
import com.backbase.kalah.web.model.GameMoveResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@RestController
@Slf4j
@RequestMapping("/games")
public class GameController {

    private final GameCreator gameCreator;
    private final GameManager gameManager;

    @Autowired
    public GameController(GameCreator gameCreator,
                          GameManager gameManager) {
        this.gameCreator = gameCreator;
        this.gameManager = gameManager;
    }

    @PostMapping
    public ResponseEntity<GameCreatedResponse> createGame() {
        return gameCreator.createGame()
                .map(GameCreatedResponse::new)
                .fold(
                        err -> {
                            log.error("Error while creating game", err);
                            throw Problem.builder()
                                    .withStatus(Status.INTERNAL_SERVER_ERROR)
                                    .withTitle("Could not create new game")
                                    .build();
                        },
                        ResponseEntity::ok
                );
    }

    @PutMapping(value = "/{gameId}/pits/{pitId")
    public ResponseEntity<GameMoveResponse> makeMove(@PathVariable("gameId") long gameId,
                                                     @PathVariable("pitId") long pitId) {
        final var moveResult = gameManager.makeAMove(gameId, pitId);
        return ResponseEntity.ok(new GameMoveResponse(gameId, moveResult));
    }

}
