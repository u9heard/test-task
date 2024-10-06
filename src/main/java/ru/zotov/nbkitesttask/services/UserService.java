package ru.zotov.nbkitesttask.services;

import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;

public interface UserService {
    /**
     * Получить пользователя по id
     * @param id - ИД пользователя
     * @return Инфо о пользователе
     */
    UserResponse getUser(Long id);

    /**
     * Сохранить пользователя
     * @param user - инфо о пользователе
     * @return Инфо о сохраненном пользователе
     */
    UserResponse saveUser(UserRequest user);

    /**
     * Обновить пользователя
     * @param user - новые данные о пользователе(вкл. ИД)
     * @return Инфо о сохраненном пользователе
     */
    UserResponse updateUser(Long id, UserRequest user);

    /**
     * Удалить пользователя
     * @param id - ИД пользователя
     */
    void deleteUser(Long id);
}
