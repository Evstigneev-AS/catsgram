package ru.yandex.practicum.catsgram.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Post {
    private Long id;
    private long authorId;
    private String description;
    private Instant postDate;
}