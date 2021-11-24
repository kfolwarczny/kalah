package com.backbase.kalah.game.model;

import java.util.UUID;

public class PlayerMockery {

    public static Player sample(){
        return sample(UUID.randomUUID());
    }

    public static Player sample(UUID uuid) {
        return Player.create().withId(uuid);
    }

}
