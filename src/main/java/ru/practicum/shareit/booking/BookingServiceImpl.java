package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponse createBooking(Long bookerId, CreateBookingRequest bookingData) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> {
            String message = String.format("Пользователь с id=%d не найден", bookerId);
            return new NotFoundException(message);
        });

        Item item = itemRepository.findById(bookingData.getItemId()).orElseThrow(() ->  {
            String message = String.format("Вещь с id=%d не найдена", bookingData.getItemId());
            return new NotFoundException(message);
        });

        Booking booking = BookingMapper.mapCreateBookingRequestToBooking(bookingData, bookerId, BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        return BookingMapper.mapBookingToBookingResponse(booking, ItemMapper.mapToItemResponse(item), booker);
    }

    @Override
    public BookingResponse approveRejectBooking(Long ownerId, Long bookingId, boolean approved) {
        //todo
        return null;
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        //todo
        return null;
    }

    @Override
    public Collection<BookingResponse> getBookingsOfUser(Long userId, BookingState bookingState) {
        //todo
        return List.of();
    }

    @Override
    public Collection<BookingResponse> getBookingsOfOwner(Long ownerId, BookingState bookingState) {
        //todo
        return List.of();
    }

}
