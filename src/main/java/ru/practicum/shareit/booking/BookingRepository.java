package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId,
            LocalDateTime nowStart,
            LocalDateTime nowEnd
    );

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long BookerId, LocalDateTime now);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId,
            LocalDateTime nowStart,
            LocalDateTime nowEnd
    );

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    Collection<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            LocalDateTime now
    );

}
