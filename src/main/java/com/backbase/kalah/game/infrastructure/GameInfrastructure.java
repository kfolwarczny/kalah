package com.backbase.kalah.game.infrastructure;

import com.backbase.kalah.game.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface GameInfrastructure extends MongoRepository<Game, UUID> {

}
