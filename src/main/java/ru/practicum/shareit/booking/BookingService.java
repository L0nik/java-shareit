package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;

import java.util.Collection;

public interface BookingService {

    BookingResponse createBooking(Long bookerId, CreateBookingRequest bookingData);

    BookingResponse approveRejectBooking(Long ownerId, Long bookingId, boolean approved);

    BookingResponse getBookingById(Long bookingId);

    Collection<BookingResponse> getBookingsOfUser(Long userId, BookingState bookingState);

    Collection<BookingResponse> getBookingsOfOwner(Long ownerId, BookingState bookingState);

}
