package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String PASSWORD_SALT = "1174f9d7bc21e00e9a5fd0a783a44c9a9f73413c";

    private final UserRepository userRepository = new UserRepositoryImpl();

    public long findUserCount() {
        return userRepository.findCount();
    }

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        if (Strings.isNullOrEmpty(user.getLogin())) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) { // todo bad
            throw new ValidationException("Login can't be longer than %s letters".formatted(8));
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }


        if (Strings.isNullOrEmpty(user.getEmail())) {
            throw new ValidationException("Email is required");
        }
        if (user.getEmail().indexOf('@') == -1 || user.getEmail().indexOf('@') != user.getEmail().lastIndexOf('@')) { // todo easier way (stream api cpunt chars)
            throw new ValidationException("Email must contain exactly one '@' character");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in use");
        }


        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Passwords must be equal");
        }

        if (password.length() < 4) { // todo
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) { // todo
            throw new ValidationException("Password can't be longer than 64 characters");
        }
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByLoginOrEmailAndPassword(String loginOrEmail, String password) {
        User user = userRepository.findByLoginAndPasswordSha(loginOrEmail, getPasswordSha(password));
        return (user != null) ? user : userRepository.findByEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
    }

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        if (Strings.isNullOrEmpty(loginOrEmail)) {
            throw new ValidationException("Login or email is required");
        }
        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        User user = findByLoginOrEmailAndPassword(loginOrEmail, password);
        if (user == null) {
            throw new ValidationException("Invalid login or password");
        }
    }

}
