package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestResponseDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestData);

    Collection<ItemRequestResponseDto> getItemRequestsOfUser(Long userId);

    Collection<ItemRequestResponseDto> getAllItemRequests(Long userId);

    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId);

}
