package com.backbase.kalah.game.model;

import com.backbase.kalah.game.GameMoveValidator;
import io.vavr.control.Either;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.ThrowableProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

@Document(value = "games")
@With
@Slf4j
public class Game {
    private static final Integer INIT_NUMBER_OF_STONES = 6;

    @Id
    private final UUID id;
    private final Map<Integer, Integer> board;
    private final List<Player> players;
    private final Player currentPlayer;

    @PersistenceConstructor
    Game(UUID id, Map<Integer, Integer> board, List<Player> players, Player currentPlayer) {
        this.id = id;
        this.board = board;
        this.players = unmodifiableList(players);
        this.currentPlayer = currentPlayer;
    }

    /**
     * Starts new game.
     *
     * @param players list of players taking place in game. Expected size of list 2
     * @return new game
     */
    public static Game startGame(List<Player> players) {
        if (CollectionUtils.isEmpty(players) || players.size() != 2) {
            throw new IllegalStateException("Players could not be empty, or differs in size from 2");
        }

        final var gameMap = IntStream.rangeClosed(1, 14)
                .boxed()
                .collect(toMap(it -> it, idx -> {
                    if (idx == 7 || idx == 14) {
                        return 0;
                    }
                    return INIT_NUMBER_OF_STONES;
                }));

        return new Game(UUID.randomUUID(), gameMap, players, players.get(0));
    }

    /**
     * Checks if for give pitId current player can make that move
     *
     * @param pitId pitID to check if for it move can be made
     * @return true or false
     */
    public boolean canPlayerMakeThatMove(int pitId) {
        final var playersIndex = pitId >= 1 && pitId <= 7 ? 0 : 1;
        return players.get(playersIndex).equals(currentPlayer) && board.get(pitId) != 0;
    }

    /**
     * Trying to make a move for a given pitIndex. May Fail if move is forbidden.
     *
     * @param pitIndex pitIndex of where the move will start
     * @return Either error when move is forbidden or the new game state
     */
    public Either<ThrowableProblem, Game> move(Integer pitIndex) {
        return GameMoveValidator.validateMove(this, pitIndex)
                .map(validated -> {
                    int tmpIndex = pitIndex;
                    final Map<Integer, Integer> tmpBoard = new HashMap<>(board);

                    var numOfStonesInPit = tmpBoard.get(tmpIndex);
                    tmpBoard.replace(tmpIndex, 0);

                    while (numOfStonesInPit > 0) {
                        tmpIndex++;

                        if (tmpIndex > 14) {
                            tmpIndex = 1;
                        }

                        if (isThisNotMyHouse(tmpIndex)) {
                            if (tmpIndex == 7) {
                                tmpIndex = 8;
                            } else if (tmpIndex == 14) {
                                tmpIndex = 1;
                            }
                        }

                        tmpBoard.compute(tmpIndex, (__, numOfStones) -> numOfStones + 1);
                        numOfStonesInPit--;
                    }

                    if (isThisNotMyHouse(tmpIndex)) {
                        final var nextPlayer = switchPlayer();
                        return new Game(this.id, tmpBoard, players, nextPlayer);
                    }

                    return new Game(this.id, tmpBoard, players, currentPlayer);
                });
    }

    private Player switchPlayer() {
        final var currentPlayerIndex = players.indexOf(currentPlayer);

        if (currentPlayerIndex == 0) {
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    private boolean isThisNotMyHouse(Integer pitIndex) {
        if (pitIndex == 7 && players.get(0).equals(currentPlayer)) {
            return false;
        } else {
            return pitIndex != 14 || !players.get(1).equals(currentPlayer);
        }
    }

    public UUID getId() {
        return id;
    }

    public Map<Integer, Integer> getBoard() {
        return unmodifiableMap(board);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id) && board.equals(game.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("board=" + board)
                .add("players=" + players)
                .add("currentPlayer=" + currentPlayer)
                .toString();
    }

    public List<UUID> getPlayersUUID() {
        return this.players
                .stream()
                .map(Player::getId)
                .toList();
    }
}
