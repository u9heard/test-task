package ru.zotov.nbkitesttask.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.zotov.nbkitesttask.configuration.TestConfig;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;
import ru.zotov.nbkitesttask.exceptions.EmailAlreadyExistException;
import ru.zotov.nbkitesttask.exceptions.UserNotFoundException;
import ru.zotov.nbkitesttask.repository.UserRepository;
import ru.zotov.nbkitesttask.services.impl.UserServiceImpl;
import ru.zotov.nbkitesttask.utils.UserUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {UserServiceImpl.class})
@Import(TestConfig.class)
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getExistingUser() {
        User mockUser = UserUtils.produceUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        UserResponse user = userService.getUser(1L);

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockUser.getFirstName(), user.getFirstName());
        assertEquals(mockUser.getSurname(), user.getSurname());
        assertEquals(mockUser.getEmail(), user.getEmail());
        assertEquals(mockUser.getAge(), user.getAge());
    }

    @Test
    void getNotExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void saveNewUser() {
        UserRequest userRequest = UserUtils.produceUserRequest();
        User mockUser = UserUtils.produceUser();
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse savedUser = userService.saveUser(userRequest);

        assertNotNull(savedUser);
        assertEquals(mockUser.getId(), savedUser.getId());
        assertEquals(mockUser.getFirstName(), savedUser.getFirstName());
        assertEquals(mockUser.getSurname(), savedUser.getSurname());
        assertEquals(mockUser.getEmail(), savedUser.getEmail());
        assertEquals(mockUser.getAge(), savedUser.getAge());
    }

    @Test
    void saveUserWithExistingEmail() {
        UserRequest userRequest = UserUtils.produceUserRequest();
        User mockUser = UserUtils.produceUser();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));

        assertThrows(EmailAlreadyExistException.class, () -> userService.saveUser(userRequest));
    }

    @Test
    void updateExistingUser() {
        UserRequest userRequest = UserUtils.produceUserRequest();
        User mockUser = UserUtils.produceUser();

        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));

        UserResponse userResponse = assertDoesNotThrow(() -> userService.updateUser(1L, userRequest));
        assertNotNull(userResponse);
    }

    @Test
    void updateUserWithExistingEmail() {
        UserRequest userRequest = UserUtils.produceUserRequest();
        User mockUser = UserUtils.produceUser();
        userRequest.setEmail("another@email.com");

        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail("another@email.com")).thenReturn(Optional.of(mockUser));

        assertThrows(EmailAlreadyExistException.class, () -> userService.updateUser(1L, userRequest));
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}