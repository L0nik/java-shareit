package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemRequestCreateDto itemRequestData
    ) {
        log.info(
                "ItemRequestController: добавление нового запроса вещи (userIf = {}, itemRequestData = {})",
                userId,
                itemRequestData
        );
        return itemRequestService.createItemRequest(userId, itemRequestData);
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> getItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestController: получение списка запросов пользователя (userId = {})", userId);
        return itemRequestService.getItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestController: получение списка всех запросов вещей (userId = {})", userId);
        return itemRequestService.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        log.info("ItemRequestController: получение запроса вещи по id (userId = {}, requestId = {})", userId, requestId);
        return itemRequestService.getItemRequestById(userId, requestId);
    }

}
