package ru.itmo.wp.model.repository.dto;

public class UserDto { // todo whst is transfer object ? when to use ?
    private final long id;
    private final String login;

    public UserDto(long id, String login) {
        this.id = id;
        this.login = login;
    }

    public long getId() { return id; }
    public String getLogin() { return login; }
}