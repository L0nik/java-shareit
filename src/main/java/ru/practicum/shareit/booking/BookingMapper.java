package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class BookingMapper {

    public Booking mapCreateBookingRequestToBooking(
            CreateBookingRequest bookingData,
            Long bookerId,
            BookingStatus bookingStatus
    ) {
        Booking booking = new Booking();
        booking.setItemId(bookingData.getItemId());
        booking.setStart(bookingData.getStart());
        booking.setEnd(bookingData.getEnd());
        booking.setBookerId(bookerId);
        booking.setStatus(bookingStatus);
        return booking;
    }

    public BookingResponse mapBookingToBookingResponse(Booking booking, ItemResponse item, User booker) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(booking.getId());
        bookingResponse.setStart(booking.getStart());
        bookingResponse.setEnd(booking.getEnd());
        bookingResponse.setItem(item);
        bookingResponse.setBooker(booker);
        bookingResponse.setStatus(booking.getStatus());
        return bookingResponse;
    }

}
