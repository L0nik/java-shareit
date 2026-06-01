package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateRequest {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    public boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
    public boolean isStartNotEqualEnd() {
        return !start.equals(end);
    }
}
