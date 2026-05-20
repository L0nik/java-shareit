package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class BookingResponse {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private ItemResponse item;
    private User booker;
    private BookingStatus status;
}
