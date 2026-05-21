package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId,
            LocalDate nowStart,
            LocalDate nowEnd
    );

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long BookerId, LocalDate now);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDate now);

    @EntityGraph(attributePaths = {"item", "booker"})
    Collection<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId,
            LocalDate nowStart,
            LocalDate nowEnd
    );

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDate now);

    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDate now);

}
