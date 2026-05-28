package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest mapItemRequestCreateDtoToItemRequest(
            ItemRequestCreateDto itemRequestCreateDto,
            User requester,
            LocalDateTime created
    ) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreateDto.getDescription());
        itemRequest.setCreated(created);
        itemRequest.setRequester(requester);
        return itemRequest;
    }

    public ItemRequestResponseDto mapItemRequestToItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(itemRequest.getId());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());
        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        return itemRequestResponseDto;
    }

}
