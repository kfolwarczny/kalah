package com.backbase.kalah.game;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

@Service
public class GameCreator {

    public Try<Long> createGame() {
        return Try.success(1L);
    }


}
