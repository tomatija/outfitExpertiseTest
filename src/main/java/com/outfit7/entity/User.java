package com.outfit7.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {

    String id;

    public String getId() {
        return id;
    }

    String playerName;

    public String getPlayerName() {
        return playerName;
    }

    Long powerLevel;

    public Long getPowerLevel() {
        return powerLevel;
    }

    Hero hero;

    List<Champion> champions;

    Long rank;

    public Long getRank() {
        return rank;
    }

    @Value
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Champion {

        String id;

        Long level;

        String name;

    }

    @Value
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Hero {

        String id;

        Long level;

        String name;

    }

}
