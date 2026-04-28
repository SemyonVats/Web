package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.EventType;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl(); // todo di dependency injection

    public void saveEvent(long userId, EventType type) {
        Event event = new Event();
        event.setUserId(userId);
        event.setType(type);
        eventRepository.save(event);
    }
}