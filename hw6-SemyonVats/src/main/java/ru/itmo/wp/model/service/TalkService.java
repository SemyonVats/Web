package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.dto.UserDto;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;

import java.util.List;

public class TalkService {
//    private static final VALIDATION_RULES = Map.of( // todo
//
//    );

    private final TalkRepository talkRepository = new TalkRepositoryImpl(); // todo di

    public void sendMessage(User sourceUser, long targetUserId, String text) throws ValidationException {
        if (sourceUser.getId() == targetUserId) {
            throw new ValidationException("You can't send a message to yourself.");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("Message text is required.");
        }
        if (text.length() > 1000) { // todo magic numbers
            throw new ValidationException("Message is too long (max 1000 characters).");
        }

        Talk talk = new Talk();
        talk.setSourceUserId(sourceUser.getId());
        talk.setTargetUserId(targetUserId);
        talk.setText(text.trim());
        talkRepository.save(talk);
    }

    public List<Talk> findAllTalksForUser(long userId) {
        return talkRepository.findAllByUserId(userId);
    }

    public List<UserDto> findAllUsersExcept(long userId) {
        return talkRepository.findAllUsersExcept(userId);
    }
}