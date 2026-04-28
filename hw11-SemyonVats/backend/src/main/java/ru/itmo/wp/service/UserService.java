package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.dto.UserDto;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserCredentials userCredentials) {
        User user = new User();
        user.setLogin(userCredentials.getLogin().toLowerCase().trim());

        userRepository.save(user);

        userRepository.updatePasswordSha(
                user.getId(),
                userCredentials.getLogin().toLowerCase().trim(),
                userCredentials.getPassword()
        );

        return user;
    }

    public boolean isLoginVacant(String login) {
        return userRepository.countByLogin(login.toLowerCase().trim()) == 0;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getCreationTime(),
                        user.getLogin()
                ))
                .collect(Collectors.toList());
    }
}