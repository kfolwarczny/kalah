package com.backbase.kalah.game.infrastructure;

import com.backbase.kalah.game.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PlayerInfrastructure extends MongoRepository<Player, UUID> {

}
