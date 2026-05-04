package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    public Item addItem(Item item);

    public Item updateItem(Item item);

    public Item getItemById(Long itemId);

    public Collection<Item> getItemsByOwner(Long userId);

    public Collection<Item> searchForItems(String text);

}
