package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.GameInfrastructure;
import com.backbase.kalah.game.model.Game;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

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

    /**
     * Make a move in a game with Id
     *
     * @param gameId the ID of a game where the move will take place
     * @param pitId  the ID of pit from which the move starts
     * @return Either error if game not found or move could not be executed or board representation
     */
    public Either<ThrowableProblem, Map<Integer, Integer>> makeAMove(UUID gameId, Integer pitId) {
        final var game = fetchGame(gameId);

        return Option.ofOptional(game)
                .toEither(Problem.valueOf(Status.NOT_FOUND))
                .flatMap(it -> it.move(pitId))
                .map(gameInfrastructure::save)
                .map(Game::getBoard);
    }

    /**
     * Fetches game for given UUID
     *
     * @param gameId the ID of a game to fetch
     * @return Optional of game if found
     */
    public Optional<Game> fetchGame(UUID gameId) {
        return gameInfrastructure.findById(gameId);
    }
}
