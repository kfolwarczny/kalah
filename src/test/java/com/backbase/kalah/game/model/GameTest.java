package com.backbase.kalah.game.model;

import com.backbase.kalah.game.GameMockery;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.zalando.problem.Status;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    @Test
    void shouldExecuteMove() {
        //given
        final var newGame = GameMockery.newGame();
        final var currentPlayer = newGame.getCurrentPlayer();
        final var expectedMap = new HashMap<>();
        expectedMap.put(1, 6);
        expectedMap.put(2, 6);
        expectedMap.put(3, 6);
        expectedMap.put(4, 0);
        expectedMap.put(5, 7);
        expectedMap.put(6, 7);
        expectedMap.put(7, 1);
        expectedMap.put(8, 7);
        expectedMap.put(9, 7);
        expectedMap.put(10, 7);
        expectedMap.put(11, 6);
        expectedMap.put(12, 6);
        expectedMap.put(13, 6);
        expectedMap.put(14, 0);

        //when
        final var gameAfterMove = newGame.move(4);

        //then
        assertThat(gameAfterMove.get().getBoard()).isEqualTo(expectedMap);
        assertThat(gameAfterMove.get().getCurrentPlayer()).isNotEqualTo(currentPlayer);
    }

    @Nested
    class MoveValidation {
        @ParameterizedTest
        @ValueSource(ints = {9, 7, 124, -1, 0, 14})
        void shouldValidateMove(int pitIndex) {
            final var newGame = GameMockery.newGame();

            final var result = newGame.move(pitIndex);

            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft().getStatus()).isEqualTo(Status.BAD_REQUEST);
        }
    }
}
