package ru.zotov.nbkitesttask.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zotov.nbkitesttask.dto.UserRequest;
import ru.zotov.nbkitesttask.dto.UserResponse;
import ru.zotov.nbkitesttask.entity.User;
import ru.zotov.nbkitesttask.exceptions.EmailAlreadyExistException;
import ru.zotov.nbkitesttask.exceptions.UserNotFoundException;
import ru.zotov.nbkitesttask.mapper.UserMapper;
import ru.zotov.nbkitesttask.repository.UserRepository;
import ru.zotov.nbkitesttask.services.UserService;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
        return userMapper.map(user);
    }

    @Override
    @Transactional
    public UserResponse saveUser(UserRequest user) {
        if(checkIfEmailExist(user.getEmail())){
            throw new EmailAlreadyExistException("Email already exist");
        }
        User savedUser = userRepository.save(userMapper.map(user));
        return userMapper.map(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest user) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));

        if(!(oldUser.getEmail().equals(user.getEmail()))
                && checkIfEmailExist(user.getEmail()))
            throw new EmailAlreadyExistException("Email already exist");

        User userToSave = userMapper.map(user);
        userToSave.setId(id);
        return userMapper.map(userRepository.save(userToSave));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private boolean checkIfEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean checkIfUserExist(Long id) {
        return userRepository.findById(id).isPresent();
    }

}
