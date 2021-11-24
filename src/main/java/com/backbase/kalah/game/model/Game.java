package com.backbase.kalah.game.model;

import io.vavr.control.Either;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.IntStream;

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
        this.players = players;
        this.currentPlayer = currentPlayer;
    }

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

    public boolean canThisPlayerMove(Integer pitId) {
        final var playersIndex = pitId >= 1 && pitId <= 7 ? 0 : 1;
        return players.get(playersIndex).equals(currentPlayer) && board.get(pitId) != 0;
    }

    public Either<ThrowableProblem, Game> move(Integer pitIndex) {
        return validateMove(pitIndex)
                .map(validated -> {
                    var tmpIndex = pitIndex;

                    var numOfStonesInPit = board.get(tmpIndex);
                    board.replace(tmpIndex, 0);

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

                        board.compute(tmpIndex, (k, numOfStones) -> numOfStones + 1);
                        numOfStonesInPit--;
                    }

                    if (isThisNotMyHouse(tmpIndex)) {
                        final var nextPlayer = switchPlayer();
                        return new Game(this.id, board, players, nextPlayer);
                    }

                    return this;
                });
    }

    private Either<ThrowableProblem, Game> validateMove(Integer pitId) {
        if (pitId < 0 || pitId > 14) {
            log.warn("User selected number outside the scope 1-14");
            return Either.left(buildValidationProblem("User selected number outside the scope 1-14"));
        } else if (!canThisPlayerMove(pitId)) {
            log.warn("This player could not move");
            return Either.left(buildValidationProblem("This player could not move. It's second player move."));
        } else if (pitId == 7 || pitId == 14) {
            log.warn("User selected houses as starting points");
            return Either.left(buildValidationProblem("User selected houses as starting points"));
        }
        return Either.right(this);
    }

    private ThrowableProblem buildValidationProblem(String message) {
        return Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Validation error")
                .withDetail(message)
                .build();
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
        return board;
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
