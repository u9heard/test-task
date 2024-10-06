package ru.zotov.nbkitesttask.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testMapToResponse() {
        User user = new User(
                1L,
                "firstname",
                "surname",
                "email@email.com",
                30);

        UserResponse userResponse = userMapper.map(user);

        assertNotNull(user);
        Assertions.assertEquals(user.getId(), userResponse.getId());
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(user.getSurname(), userResponse.getSurname());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getAge(), userResponse.getAge());
    }

    @Test
    void testMapToEntity() {
        UserRequest userRequest = new UserRequest(
                "firstname",
                "surname",
                "email@email.com",
                30
        );

        User user = userMapper.map(userRequest);
        assertNotNull(user);
        assertNull(user.getId());
        Assertions.assertEquals(user.getFirstName(), userRequest.getFirstName());
        Assertions.assertEquals(user.getSurname(), userRequest.getSurname());
        Assertions.assertEquals(user.getEmail(), userRequest.getEmail());
        Assertions.assertEquals(user.getAge(), userRequest.getAge());
    }
}