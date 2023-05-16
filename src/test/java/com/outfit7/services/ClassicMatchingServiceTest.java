package com.outfit7.services;

import static com.outfit7.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Objects;

import com.outfit7.entity.exception.InsufficientOpponentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.outfit7.entity.User;

@ExtendWith(MockitoExtension.class)
class ClassicMatchingServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    ClassicMatchingService classicMatchingService;

    @Test
    void shouldRetrieveOpponentsForUserId() { //this also checks for duplicate names
        // Given
        String userId = "some-user-id";
        given(userService.get(userId))
                .willReturn(user(0));
        given(userService.getAll())
                .willReturn(users());

        // When
        List<User> opponents = classicMatchingService.retrieveOpponents(userId);

        // Then
        assertThat(opponents)
                .hasSize(4)
                .extracting(User::getId)
                .containsExactly("2", "3", "5", "7");
    }

    @Test
    void shouldReturnOpponent_duplicateNameTest(){
        // Given
        String userId = "some-user-id";
        given(userService.get(userId))
                .willReturn(user(0));
        given(userService.getAll())
                .willReturn(usersById("5","6"));

        // When
        List<User> opponents = classicMatchingService.retrieveOpponents(userId);

        // Then
        assertThat(opponents)
                .hasSize(1)
                .extracting(User::getId)
                .anyMatch(opponentId -> Objects.equals(opponentId, "5") || Objects.equals(opponentId, "6"));
    }

    @Test
    void shouldThrowExceptionIfNoUsersAreFound() {
        //Given
        User unmatchableUser = User.builder().playerName("name1").powerLevel(10000L).id("10").hero(hero(1L)).rank(100L).champions(champions()).build();

        String userId = "some_string";

        given(userService.get(userId))
                .willReturn(unmatchableUser);
        given(userService.getAll())
                .willReturn(users());

        //When
        Throwable thrown = catchThrowable(() -> classicMatchingService.retrieveOpponents(userId));

        // Then
        assertThat(thrown)
                .isInstanceOf(InsufficientOpponentsException.class);
    }
}