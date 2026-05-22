package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponse createBooking(Long bookerId, BookingCreateRequest bookingData) {
        log.info("BookingServiceImpl: начало создания бронирования пользователем id = {}: {}", bookerId, bookingData);

        LocalDateTime now = LocalDateTime.now();

        User booker = userRepository.findById(bookerId).orElseThrow(() -> {
            String message = String.format("Пользователь с id=%d не найден", bookerId);
            return new NotFoundException(message);
        });

        Item item = itemRepository.findById(bookingData.getItemId()).orElseThrow(() ->  {
            String message = String.format("Вещь с id=%d не найдена", bookingData.getItemId());
            return new NotFoundException(message);
        });

        if (!item.getAvailable()) {
            String message = String.format("Вещь с id=%d недоступна для бронирования", bookingData.getItemId());
            throw new ValidationException(message);
        }

        Booking booking = BookingMapper.mapBookingCreateRequestToBooking(bookingData, item, booker, BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        log.info("BookingServiceImpl: бронирвоание успешно создано: {}", booking);
        return BookingMapper.mapBookingToBookingResponse(
                booking,
                ItemMapper.mapToItemResponse(item),
                UserMapper.mapUserToUserResponse(booker)
        );
    }

    @Override
    public BookingResponse approveRejectBooking(Long ownerId, Long bookingId, boolean approved) {
        log.info(
                "BookingServiceImpl: начало одобрения/отклонения бронирования (ownerId = {}, bookingId = {}, approved = {})",
                ownerId,
                bookingId,
                approved
        );
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            String message = String.format("Бронирование с id=%d не найдено", bookingId);
            return new NotFoundException(message);
        });
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            String message = String.format("Пользователь с id=%d не является владельцем вещи", ownerId);
            throw new ValidationException(message);
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);

        log.info(
                "BookingServiceImpl: окончание одобрения/отклонения бронирования (ownerId = {}, bookingId = {}, approved = {})",
                ownerId,
                bookingId,
                approved
        );

        return BookingMapper.mapBookingToBookingResponse(
                booking,
                ItemMapper.mapToItemResponse(booking.getItem()),
                UserMapper.mapUserToUserResponse(booking.getBooker())
        );
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        log.info("BookingServiceImpl: получение бронирования по id = {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            String message = String.format("Бронирование с id=%d не найдено", bookingId);
            return new NotFoundException(message);
        });
        return BookingMapper.mapBookingToBookingResponse(
                booking,
                ItemMapper.mapToItemResponse(booking.getItem()),
                UserMapper.mapUserToUserResponse(booking.getBooker())
        );
    }

    @Override
    public Collection<BookingResponse> getBookingsOfUser(Long bookerId, BookingState bookingState) {

        log.info(
                "BookingServiceImpl: получение всех бронирований пользователя (bookerId = {}, bookingState = {})",
                bookerId,
                bookingState
        );

        if (!userRepository.existsById(bookerId)) {
            String message = String.format("Пользователь с id=%d не найден", bookerId);
            throw new NotFoundException(message);
        }

        Collection<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case BookingState.ALL -> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            case BookingState.WAITING -> bookings =
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case BookingState.REJECTED -> bookings =
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            case BookingState.CURRENT -> bookings =
                    bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now);
            case BookingState.PAST -> bookings =
                    bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
            case BookingState.FUTURE -> bookings =
                    bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
            default -> throw new ValidationException("Некорректное значение параметра 'state'");
        }

        return bookings.stream()
                .map(booking -> BookingMapper.mapBookingToBookingResponse(
                        booking,
                        ItemMapper.mapToItemResponse(booking.getItem()),
                        UserMapper.mapUserToUserResponse(booking.getBooker())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponse> getBookingsOfOwner(Long ownerId, BookingState bookingState) {
        log.info(
                "BookingServiceImpl: получение бронирований вещей текущего пользователя (ownerId = {}, bookingState = {})",
                ownerId,
                bookingState
        );

        if (!userRepository.existsById(ownerId)) {
            String message = String.format("Пользователь с id=%d не найден", ownerId);
            throw new NotFoundException(message);
        }

        Collection<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case BookingState.ALL -> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
            case BookingState.WAITING -> bookings =
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case BookingState.REJECTED -> bookings =
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            case BookingState.CURRENT -> bookings =
                    bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now);
            case BookingState.PAST -> bookings =
                    bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now);
            case BookingState.FUTURE -> bookings =
                    bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now);
            default -> throw new ValidationException("Некорректное значение параметра 'state'");
        }

        return bookings.stream()
                .map(booking -> BookingMapper.mapBookingToBookingResponse(
                        booking,
                        ItemMapper.mapToItemResponse(booking.getItem()),
                        UserMapper.mapUserToUserResponse(booking.getBooker())
                ))
                .collect(Collectors.toList());
    }

}
