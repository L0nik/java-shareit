package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerId(Long bookerId, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            Long bookerId,
            LocalDateTime nowStart,
            LocalDateTime nowEnd,
            Sort sort
    );

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(
            Long ownerId,
            LocalDateTime nowStart,
            LocalDateTime nowEnd,
            Sort sort
    );

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemId(Long itemId, Sort sort);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            LocalDateTime now
    );

}
