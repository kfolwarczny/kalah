package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.GameInfrastructure;
import com.backbase.kalah.game.model.GameMockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameManagerTest {

    @Mock
    private GameInfrastructure infrastructure;
    @InjectMocks
    private GameManager gameManager;

    @Test
    void shouldReturnNotFoundWhenGameNotExist() {
        //given
        final var gameId = UUID.randomUUID();

        //when
        when(infrastructure.findById(any())).thenReturn(Optional.empty());
        final var result = gameManager.makeAMove(gameId, 1);

        //then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.isRight()).isFalse();
        assertThat(result.getLeft().getStatus()).isEqualTo(Status.NOT_FOUND);
        verify(infrastructure).findById(gameId);
        verify(infrastructure, never()).save(any());
    }

    @Test
    void shouldReturnErrorWhenMoveCouldNotBeMade() {
        //given
        final var gameUUID = UUID.randomUUID();
        final var game = GameMockery.gameInPlay(gameUUID);

        //when
        when(infrastructure.findById(any())).thenReturn(Optional.of(game));
        final var result = gameManager.makeAMove(gameUUID, -21);

        //then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.isRight()).isFalse();
        assertThat(result.getLeft().getStatus()).isEqualTo(Status.BAD_REQUEST);
        verify(infrastructure).findById(gameUUID);
        verify(infrastructure, never()).save(any());
    }

    @Test
    void shouldReturnBoardWhenAllWentSmooth() {
        //given
        final var game = GameMockery.gameInPlay(UUID.randomUUID());
        final var expectedMap = new HashMap<>();
        expectedMap.put(1, 0);
        expectedMap.put(2, 1);
        expectedMap.put(3, 8);
        expectedMap.put(4, 8);
        expectedMap.put(5, 8);
        expectedMap.put(6, 8);
        expectedMap.put(7, 2);
        expectedMap.put(8, 8);
        expectedMap.put(9, 0);
        expectedMap.put(10, 7);
        expectedMap.put(11, 7);
        expectedMap.put(12, 7);
        expectedMap.put(13, 7);
        expectedMap.put(14, 1);

        //when
        when(infrastructure.findById(any())).thenReturn(Optional.of(game));
        when(infrastructure.save(any())).thenReturn(game);
        final var result = gameManager.makeAMove(UUID.randomUUID(), 8);

        //then
        assertThat(result.isRight()).isTrue();
        assertThat(result.isLeft()).isFalse();
        assertThat(result.get()).isEqualTo(expectedMap);
    }

}
