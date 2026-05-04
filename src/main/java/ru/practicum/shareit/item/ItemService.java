package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import java.util.Collection;

public interface ItemService {

    public ItemResponse createItem(ItemCreateRequest itemData, Long ownerId);

    public ItemResponse updateItem(Long userId, Long itemId, ItemUpdateRequest itemData);

    public ItemResponse getItemById(Long itemId);

    public Collection<ItemResponse> getItemsByOwner(Long userId);

    public Collection<ItemResponse> searchForItems(String text);

}
