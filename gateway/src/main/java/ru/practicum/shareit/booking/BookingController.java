package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid BookingCreateRequest bookingData
    ) {
        log.info("BookingService: получен запрос на бронирование от пользователя {}: {}", userId, bookingData);
        return bookingClient.createBooking(userId, bookingData);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveRejectBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved
    ) {
        log.info(
                "BookingService: получен запрос на одобрение/отказ по бронированию (userId = {}, bookingId = {}, approved = {})",
                userId,
                bookingId,
                approved
        );
        return bookingClient.approveRejectBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId) {
        log.info("BookingService: получен запрос на получение бронирования по id = {}", bookingId);
        return bookingClient.getBookingById(bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsOfUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingState state
    ) {
        log.info(
                "BookingService: получен запрос на получение бронирований пользователя (userId = {}, state = {})",
                userId,
                state
        );
        return bookingClient.getBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOfOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingState state
    ) {
        log.info(
                "BookingService: получен запрос на получение бронирований вещей текущего пользователя (userId = {}, state = {})",
                userId,
                state
        );
        return bookingClient.getBookingsOfOwner(userId, state);
    }
}
