package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Collection<ItemResponse> items;
}
