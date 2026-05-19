package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponse createItem(ItemCreateRequest itemData, Long ownerId) {
        log.info("ItemServiceImpl: начало добавления новой вещи {} пользователем {}", itemData, ownerId);
        User owner = userRepository.findById(ownerId).orElseThrow(() -> {
            String message = String.format("Пользователь с id=%d не найден", ownerId);
            return new NotFoundException(message);
        });
        Item item = ItemMapper.mapItemCreateRequestToItem(itemData);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);
        log.info("ItemServiceImpl: добавлена новая вещь {} пользователем {}", itemData, ownerId);
        return ItemMapper.mapToItemResponse(savedItem);
    }

    @Override
    public ItemResponse updateItem(Long ownerId, Long itemId, ItemUpdateRequest itemData) {
        log.info("ItemServiceImpl: начало обновления данных вещи {} (ownerId={}, itemId={})", itemData, ownerId, itemId);

        if (!userRepository.existsById(ownerId)) {
            String message = String.format("Пользователь с id=%d не найден", ownerId);
            throw new NotFoundException(message);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            String message = String.format("вещь с id=%d не найдена", itemId);
            return new NotFoundException(message);
        });

        if (!item.getOwner().getId().equals(ownerId)) {
            String message = String.format("Пользователь %d не является владельцем вещи %d", ownerId, itemId);
            throw new NotFoundException(message);
        }
        ItemMapper.updateItemFields(item, itemData);
        itemRepository.save(item);
        log.info("ItemServiceImpl: обновлены данные вещи {} (ownerId={}, itemId={})", itemData, ownerId, itemId);
        return ItemMapper.mapToItemResponse(item);
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        log.info("ItemServiceImpl: получение данных вещи по id (itemId={})", itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            String message = String.format("вещь с id=%d не найдена", itemId);
            return new NotFoundException(message);
        });
        return ItemMapper.mapToItemResponse(item);
    }

    @Override
    public Collection<ItemResponse> getItemsByOwner(Long userId) {
        log.info("ItemServiceImpl: получение вещей пользователя (userId={})", userId);
        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemResponse).toList();
    }

    @Override
    public Collection<ItemResponse> searchForItems(String text) {
        log.info("ItemServiceImpl: поиск вещей по строке (text={})", text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchForItems(text).stream()
                .map(ItemMapper::mapToItemResponse)
                .toList();
    }

}
