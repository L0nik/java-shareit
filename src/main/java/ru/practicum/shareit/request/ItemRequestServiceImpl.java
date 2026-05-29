package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestResponseDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestData) {
        log.info(
                "ItemRequestServiceImpl: добавление нового запроса вещи (userIf = {}, itemRequestData = {})",
                userId,
                itemRequestData
        );

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            String message = String.format("Пользователь с id=%d не найден", userId);
            return new NotFoundException(message);
        });

        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.mapItemRequestCreateDtoToItemRequest(itemRequestData, requester, now);
        itemRequestRepository.save(itemRequest);
        log.info("ItemRequestServiceImpl: успешно создан новый запрос вещи {}", itemRequest);
        return ItemRequestMapper.mapItemRequestToItemRequestResponseDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestResponseDto> getItemRequestsOfUser(Long userId) {
        log.info("ItemRequestServiceImpl: получение списка запросов пользователя (userId = {})", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String message = String.format("Пользователь с id=%d не найден", userId);
            return new NotFoundException(message);
        });
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Collection<ItemRequest> itemRequests = itemRequestRepository.findByRequesterId(userId, sort);
        return itemRequests.stream()
                .map(itemRequest -> {
                    Collection<ItemResponse> items = itemRequest.getItems().stream()
                            .map(ItemMapper::mapToItemResponse)
                            .toList();
                    return ItemRequestMapper.mapItemRequestToItemRequestResponseDto(itemRequest, items);
                })
                .toList();
    }

    @Override
    public Collection<ItemRequestResponseDto> getAllItemRequests(Long userId) {
        log.info("ItemRequestServiceImpl: получение списка всех запросов вещей (userId = {})", userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAll(sort);
        return itemRequests.stream()
                .map(itemRequest -> {
                    Collection<ItemResponse> items = itemRequest.getItems().stream()
                            .map(ItemMapper::mapToItemResponse)
                            .toList();
                    return ItemRequestMapper.mapItemRequestToItemRequestResponseDto(itemRequest, items);
                })
                .toList();
    }

    @Override
    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        log.info("ItemRequestServiceImpl: получение запроса вещи по id (userId = {}, requestId = {})", userId, requestId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            String message = String.format("Запрос вещи с id=%d не найден", requestId);
            return new NotFoundException(message);
        });
        Collection<ItemResponse> items = itemRequest.getItems().stream()
                .map(ItemMapper::mapToItemResponse)
                .toList();
        return ItemRequestMapper.mapItemRequestToItemRequestResponseDto(itemRequest, items);
    }
}
