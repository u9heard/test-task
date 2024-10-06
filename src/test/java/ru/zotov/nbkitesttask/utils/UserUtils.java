package ru.zotov.nbkitesttask.utils;

import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.entity.User;

public class UserUtils {
    public static User produceUser() {
        return new User(
                1L,
                "username",
                "surname",
                "email@mail.com",
                30
        );
    }

    public static UserRequest produceUserRequest() {
        return new UserRequest(
                "username",
                "surname",
                "email@mail.com",
                30
        );
    }
}
