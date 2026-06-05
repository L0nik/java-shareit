package ru.practicum.shareit.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestCreateDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> json;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void deserialize_whenJsonIsValid_shouldCreateObjectWithoutValidationErrors() throws Exception {

        String jsonContent = "{" +
                "    \"description\": \"request description\"" +
                "}";

        ItemRequestCreateDto request = json.parseObject(jsonContent);

        assertThat(request.getDescription()).isEqualTo("request description");

        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenDescriptionIsBlank_shouldTriggerNotBlankValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"description\": \"     \"" +
                "}";

        String expectedError = "Отсутствует описание запроса";

        ItemRequestCreateDto request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasNotBlankErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasNotBlankErrorMessage).isTrue();
    }

    @Test
    void deserialize_whenDescriptionIsEmptyOrMissing_shouldTriggerNotBlankValidationError() throws Exception {

        String jsonContent = "{}";

        String expectedError = "Отсутствует описание запроса";

        ItemRequestCreateDto request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasNotBlankErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasNotBlankErrorMessage).isTrue();
    }
}
