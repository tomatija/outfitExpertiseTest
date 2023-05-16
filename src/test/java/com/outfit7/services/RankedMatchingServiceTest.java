package com.outfit7.services;

import static com.outfit7.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.function.Predicate;

import com.outfit7.entity.exception.InsufficientOpponentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.outfit7.entity.User;

@ExtendWith(MockitoExtension.class)
class RankedMatchingServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    RankedMatchingService rankedMatchingService;


    @Test
    void shouldRetrieveOpponentsForUserId() {
        // Given
        String userId = "some-user-id";
        given(userService.get(userId))
                .willReturn(user(0));
        given(userService.getAll())
                .willReturn(users());

        // When
        List<User> opponents = rankedMatchingService.retrieveOpponents(userId);

        // Then
        assertThat(opponents)
                .hasSize(5)
                .extracting(User::getId)
                .contains("2","3","4","5","7");
    }

    @Test
    void shouldThrowExceptionIfDuplicateNameIsFound() {
        // Given
        String userId = "some-user-id";
        given(userService.get(userId))
                .willReturn(user(0));
        given(userService.getAll())
                .willReturn(usersById("5","6","2","3","7"));

        //When
        Throwable thrown = catchThrowable(() -> rankedMatchingService.retrieveOpponents(userId));
        //users 5 and 6 have the same name so if name filtering works this should throw InsufficientOpponentsException

        // Then
        assertThat(thrown)
                .isInstanceOf(InsufficientOpponentsException.class);
    }

    @Test
    void shouldThrowExceptionIfNotEnoughUsersAreFound() {
        //Given
        User unmatchableUser = User.builder().playerName("name1").powerLevel(10L).id("10").hero(hero(1L)).rank(1000000L).champions(champions()).build();

        String userId = "some_string";

        given(userService.get(userId))
                .willReturn(unmatchableUser);
        given(userService.getAll())
                .willReturn(users());

        //When
        Throwable thrown = catchThrowable(() -> rankedMatchingService.retrieveOpponents(userId));

        // Then
        assertThat(thrown)
                .isInstanceOf(InsufficientOpponentsException.class);
    }
}