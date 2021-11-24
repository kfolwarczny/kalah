package com.backbase.kalah.game;

import com.backbase.kalah.game.infrastructure.GameInfrastructure;
import com.backbase.kalah.game.model.Game;
import com.backbase.kalah.game.model.GameMockery;
import com.backbase.kalah.game.model.Player;
import com.mongodb.MongoClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameCreatorTest {

    @Mock
    private PlayersService playersService;
    @Mock
    private GameInfrastructure infrastructure;

    @InjectMocks
    private GameCreator creator;

    @Test
    void shouldReturnGameWhenEverythingWentSmooth() {
        final var createdGame = GameMockery.newGame();

        //when
        when(playersService.createPlayers()).thenReturn(List.of(Player.create(), Player.create()));
        when(infrastructure.insert(any(Game.class))).thenReturn(createdGame);
        final var game = creator.createGame();

        //then
        assertThat(game.isSuccess()).isTrue();
        assertThat(game.get()).isEqualTo(createdGame.getId());
    }

    @Test
    void shouldReturnFailureWhenSomethingCrashed() {
        //when
        when(playersService.createPlayers()).thenReturn(List.of(Player.create(), Player.create()));
        when(infrastructure.insert(any(Game.class))).thenThrow(new MongoClientException("something went wrong"));
        final var game = creator.createGame();

        //then
        assertThat(game.isSuccess()).isFalse();
        assertThat(game.isFailure()).isTrue();
    }

}
