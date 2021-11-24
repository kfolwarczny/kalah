package com.backbase.kalah.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class GameManager {
    public Map<Integer, Integer> makeAMove(long gameId, long pitId) {
        return Map.of();
    }
}
