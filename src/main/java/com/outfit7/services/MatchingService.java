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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public abstract class MatchingService {

    @Inject
    UserService userService;

    public List<User> retrieveOpponents(String userId) {
        User currentUser = userService.get(userId);
        log.debug("Found user: '{}'", currentUser);
        return matchOpponents(currentUser);
    }

    public abstract List<User> matchOpponents(User currentUser);

    public static Predicate<User> distinctByKey(Function<User, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
