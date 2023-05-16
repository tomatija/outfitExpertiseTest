package com.outfit7.services;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.outfit7.entity.User;

import com.outfit7.entity.exception.InsufficientOpponentsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ClassicMatchingService extends MatchingService{
    @Override
    public List<User> matchOpponents(User currentUser) {
        List<User> filteredOpponents = userService.getAll().stream()
                .filter(opponent -> !opponent.getId().equals(currentUser.getId()))
                .filter(filterByPowerLevel(currentUser))
                .filter(distinctByKey(User::getPlayerName)).toList();

        if(filteredOpponents.isEmpty()){
            throw new InsufficientOpponentsException("No valid opponents found");
        }

        return filteredOpponents;
    }

    public static Predicate<User> filterByPowerLevel(User currentUser) {
        int POWER_LEVEL_DIFFERENCE = 15;
        return opponent ->
                opponent.getPowerLevel() <= currentUser.getPowerLevel() + POWER_LEVEL_DIFFERENCE
                        && opponent.getPowerLevel() >= currentUser.getPowerLevel() - POWER_LEVEL_DIFFERENCE;
    }
}
