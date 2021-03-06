package com.backbase.kalah.web.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GameResponse(
        UUID id,
        Map<Integer, Integer> status,
        List<UUID> playersIds,
        UUID currentPlayer
) {
}
