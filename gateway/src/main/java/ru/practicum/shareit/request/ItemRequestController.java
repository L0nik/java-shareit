package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid ItemRequestCreateDto itemRequestData
    ) {
        log.info(
                "ItemRequestController: добавление нового запроса вещи (userIf = {}, itemRequestData = {})",
                userId,
                itemRequestData
        );
        return itemRequestClient.createItemRequest(userId, itemRequestData);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestController: получение списка запросов пользователя (userId = {})", userId);
        return itemRequestClient.getItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ItemRequestController: получение списка всех запросов вещей (userId = {})", userId);
        return itemRequestClient.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId
    ) {
        log.info("ItemRequestController: получение запроса вещи по id (userId = {}, requestId = {})", userId, requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

}
