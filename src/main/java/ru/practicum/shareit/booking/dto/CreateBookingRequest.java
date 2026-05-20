package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    private Long itemId;
    private LocalDate start;
    private LocalDate end;
}
