package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemResponse mapToItemResponse(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getAvailable());
        return itemResponse;
    }

    public static Item mapItemCreateRequestToItem(ItemCreateRequest itemCreateRequest) {
        Item item = new Item();
        item.setName(itemCreateRequest.getName());
        item.setDescription(itemCreateRequest.getDescription());
        item.setAvailable(itemCreateRequest.getAvailable());
        return item;
    }

    public static void updateItemFields(Item item, ItemUpdateRequest itemUpdateRequest) {

        if (itemUpdateRequest.hasName()) {
            item.setName(itemUpdateRequest.getName());
        }

        if (itemUpdateRequest.hasDescription()) {
            item.setDescription(itemUpdateRequest.getDescription());
        }

        if (itemUpdateRequest.hasAvailavle()) {
            item.setAvailable(itemUpdateRequest.getAvailable());
        }

    }

}
