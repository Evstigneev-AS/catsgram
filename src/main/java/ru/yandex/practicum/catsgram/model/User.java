package ru.yandex.practicum.catsgram.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "email")
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;
}