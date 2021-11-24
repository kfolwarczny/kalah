package com.backbase.kalah.game;

import com.backbase.kalah.game.model.Game;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

@Slf4j
public final class GameMoveValidator {
    private GameMoveValidator() {
    }

    /**
     * Validates move in a game
     */
    public static Either<ThrowableProblem, Game> validateMove(Game game, int moveId) {
        if (moveId < 1 || moveId > 14) {
            log.warn("User selected number outside the scope 1-14");
            return Either.left(buildValidationProblem("User selected number outside the scope 1-14"));
        } else if (!game.canPlayerMakeThatMove(moveId)) {
            log.warn("This player could not move");
            return Either.left(buildValidationProblem("This player could not move. It's second player move."));
        } else if (moveId == 7 || moveId == 14) {
            log.warn("User selected houses as starting points");
            return Either.left(buildValidationProblem("User selected houses as starting points"));
        }
        return Either.right(game);
    }

    private static ThrowableProblem buildValidationProblem(String message) {
        return Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Validation error")
                .withDetail(message)
                .build();
    }
}
