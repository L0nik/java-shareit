package ru.practicum.shareit.request;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Override
    @EntityGraph(attributePaths = {"items"})
    List<ItemRequest> findAll();

    @Override
    @EntityGraph(attributePaths = {"items"})
    Optional<ItemRequest> findById(Long id);

    @EntityGraph(attributePaths = {"items"})
    Collection<ItemRequest> findByRequesterId(Long userId, Sort sort);
}
