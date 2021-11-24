package com.backbase.kalah.game;

import com.backbase.kalah.game.model.GameMockery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.zalando.problem.Status;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameMoveValidatorTest {

    @ParameterizedTest
    @ValueSource(ints = {9, 7, 124, -1, 0, 14})
    void shouldValidateMoveOfNewGame(int pitIndex) {
        final var newGame = GameMockery.newGame();

        final var result = GameMoveValidator.validateMove(newGame, pitIndex);

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft().getStatus()).isEqualTo(Status.BAD_REQUEST);
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 7, 124, -1, 0, 14})
    void shouldValidateMoveOfExistingGame(int pitIndex) {
        final var newGame = GameMockery.gameInPlay(UUID.randomUUID());

        final var result = GameMoveValidator.validateMove(newGame, pitIndex);

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft().getStatus()).isEqualTo(Status.BAD_REQUEST);
    }
}
