package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public Optional<User> findById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        } else {
            return Optional.empty();
        }
    }

    public User create(User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (findAll().stream().anyMatch(u -> u.getEmail().equals(newUser.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        newUser.setId(getNextId());
        newUser.setRegistrationDate(Instant.now());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(User updatedUser) {
        // проверяем необходимые условия
        if (updatedUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(updatedUser.getId())) {
            User oldUser = users.get(updatedUser.getId());
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(oldUser.getEmail())) {
                if (findAll().stream().anyMatch(u -> u.getEmail().equals(updatedUser.getEmail()))) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
                oldUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getUsername() != null) {
                oldUser.setUsername(updatedUser.getUsername());
            }

            if (updatedUser.getPassword() != null) {
                oldUser.setPassword(updatedUser.getPassword());
            }

            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + updatedUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}