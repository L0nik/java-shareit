package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequestCreateDto {

    @NotBlank(message = "Отсутствует описание запроса")
    private String description;

}
