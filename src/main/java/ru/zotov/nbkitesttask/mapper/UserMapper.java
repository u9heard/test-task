package ru.zotov.nbkitesttask.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User map(UserRequest userRequest);
    UserResponse map(User user);
}
