package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.repository.dto.UserDto;

import java.util.List;

public interface TalkRepository {
    void save(Talk talk);
    List<Talk> findAllByUserId(long userId);
    List<UserDto> findAllUsersExcept(long userId); // Для оптимизации
}