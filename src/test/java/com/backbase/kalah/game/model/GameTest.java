package com.backbase.kalah.game.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    @Nested
    class StartGame {

        @Test
        void shouldCreateANewGame() {
            final var playerOne = PlayerMockery.sample();
            final var playerTwo = PlayerMockery.sample();
            final var expectedMap = new HashMap<>();
            expectedMap.put(1, 6);
            expectedMap.put(2, 6);
            expectedMap.put(3, 6);
            expectedMap.put(4, 6);
            expectedMap.put(5, 6);
            expectedMap.put(6, 6);
            expectedMap.put(7, 0);
            expectedMap.put(8, 6);
            expectedMap.put(9, 6);
            expectedMap.put(10, 6);
            expectedMap.put(11, 6);
            expectedMap.put(12, 6);
            expectedMap.put(13, 6);
            expectedMap.put(14, 0);

            final var game = Game.startGame(List.of(playerOne, playerTwo));

            assertThat(game.getCurrentPlayer()).isEqualTo(playerOne);
            assertThat(game.getBoard()).isEqualTo(expectedMap);

        }

    }

    @Nested
    class PlayerMove {

        @ParameterizedTest
        @MethodSource("provideArguments")
        void shouldValidateMoveOfNewGame(int pitIndex, boolean expectedResult) {
            final var game = GameMockery.newGame();

            final var actualResult = game.canPlayerMakeThatMove(pitIndex);

            assertThat(actualResult).isEqualTo(expectedResult);
        }

        public static Stream<? extends Arguments> provideArguments() {
            return Stream.of(
                    Arguments.of(1, true),
                    Arguments.of(2, true),
                    Arguments.of(3, true),
                    Arguments.of(4, true),
                    Arguments.of(5, true),
                    Arguments.of(6, true),
                    Arguments.of(7, false),
                    Arguments.of(8, false),
                    Arguments.of(9, false),
                    Arguments.of(10, false),
                    Arguments.of(11, false),
                    Arguments.of(12, false),
                    Arguments.of(13, false),
                    Arguments.of(14, false)
            );
        }
    }

    @Nested
    class Move {
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

        @Test
        void shouldFailWhenWrongPitIdx() {
            //given
            final var newGame = GameMockery.newGame();

            //when
            final var gameAfterMove = newGame.move(484);

            //then
            assertThat(gameAfterMove.isLeft()).isTrue();
        }
    }
}
