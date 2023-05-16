package com.outfit7.services;

import com.outfit7.entity.User;
import com.outfit7.entity.exception.InsufficientOpponentsException;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class RankedMatchingService extends MatchingService{
    @Override
    public List<User> matchOpponents(User currentUser) {
        int MIN_NUMBER_OF_OPPONENTS = 5;
        System.out.println();
        List<User> filteredOpponents  = userService.getAll().stream()
                .filter(opponent -> !opponent.getId().equals(currentUser.getId()))
                .filter(filterByRank(currentUser))
                .filter(distinctByKey(User::getPlayerName))
                .collect(Collectors.toList());

        if(filteredOpponents.size() < MIN_NUMBER_OF_OPPONENTS){ //check if we have at least X filtered opponents
            throw new InsufficientOpponentsException("Unable to find enough valid opponents");
        }

        Collections.shuffle(filteredOpponents); //randomize the order

        filteredOpponents = filteredOpponents.subList(0,MIN_NUMBER_OF_OPPONENTS);

        return filteredOpponents;
    }

    public Predicate<User> filterByRank(User currentUser) {
        int RANK_DIFFERENCE = 100;
        return opponent ->
                opponent.getRank() <= currentUser.getRank() + RANK_DIFFERENCE
                        && opponent.getRank() >= currentUser.getRank() - RANK_DIFFERENCE;
    }
}
