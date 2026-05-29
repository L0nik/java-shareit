package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemMapper {

    public ItemResponse mapToItemResponse(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getAvailable());
        return itemResponse;
    }

    public ItemResponseFull mapToItemResponseFull(
            Item item,
            LocalDateTime lastBooking,
            LocalDateTime nextBooking,
            List<CommentResponse> comments
    ) {
        ItemResponseFull itemResponse = new ItemResponseFull();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getAvailable());
        itemResponse.setLastBooking(lastBooking);
        itemResponse.setNextBooking(nextBooking);
        itemResponse.setComments(comments);
        return itemResponse;
    }

    public Item mapItemCreateRequestToItem(ItemCreateRequest itemCreateRequest, @Nullable ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemCreateRequest.getName());
        item.setDescription(itemCreateRequest.getDescription());
        item.setAvailable(itemCreateRequest.getAvailable());
        item.setItemRequest(itemRequest);
        return item;
    }

    public void updateItemFields(Item item, ItemUpdateRequest itemUpdateRequest) {

        if (itemUpdateRequest.hasName()) {
            item.setName(itemUpdateRequest.getName());
        }

        if (itemUpdateRequest.hasDescription()) {
            item.setDescription(itemUpdateRequest.getDescription());
        }

        if (itemUpdateRequest.hasAvailable()) {
            item.setAvailable(itemUpdateRequest.getAvailable());
        }

    }

}
