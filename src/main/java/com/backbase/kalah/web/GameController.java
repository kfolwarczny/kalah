package com.backbase.kalah.web;

import com.backbase.kalah.game.GameCreator;
import com.backbase.kalah.game.GameManager;
import com.backbase.kalah.web.model.GameCreatedResponse;
import com.backbase.kalah.web.model.GameMoveResponse;
import com.backbase.kalah.web.model.GameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.UUID;

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

    @GetMapping(value = "/{gameId}")
    public ResponseEntity<GameResponse> fetchGame(@PathVariable("gameId") UUID gameId) {
        return gameManager.fetchGame(gameId)
                .map(game -> new GameResponse(game.getId(), game.getMap()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{gameId}/pits/{pitId}")
    public ResponseEntity<GameMoveResponse> makeMove(@PathVariable("gameId") UUID gameId,
                                                     @PathVariable("pitId") long pitId) {
        final var moveResult = gameManager.makeAMove(gameId, pitId);
        return ResponseEntity.ok(new GameMoveResponse(gameId, moveResult));
    }

}
