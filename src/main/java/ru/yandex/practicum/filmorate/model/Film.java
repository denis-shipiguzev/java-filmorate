package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;

    @JsonGetter("duration")
    public long getDurationInSeconds() {
        return duration.getSeconds();
    }

    @JsonSetter("duration")
    public void setDurationInSeconds(long durationInSeconds) {
        this.duration = Duration.ofSeconds(durationInSeconds);
    }
}
