package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemUpdateRequestJsonTest {

    @Autowired
    private JacksonTester<ItemUpdateRequest> json;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void deserialize_whenJsonIsValidAndFull_shouldCreateObjectWithoutValidationErrors() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"item updated\"," +
                "    \"description\": \"description updated\"," +
                "    \"available\": false" +
                "}";

        ItemUpdateRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("item updated");
        assertThat(request.getDescription()).isEqualTo("description updated");
        assertThat(request.getAvailable()).isFalse();

        assertThat(request.hasName()).isTrue();
        assertThat(request.hasDescription()).isTrue();
        assertThat(request.hasAvailable()).isTrue();

        Set<ConstraintViolation<ItemUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenJsonIsEmpty_shouldBeValidForPartialUpdate() throws Exception {
        String jsonContent = "{}";

        ItemUpdateRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isNull();
        assertThat(request.getDescription()).isNull();
        assertThat(request.getAvailable()).isNull();

        assertThat(request.hasName()).isFalse();
        assertThat(request.hasDescription()).isFalse();
        assertThat(request.hasAvailable()).isFalse();

        Set<ConstraintViolation<ItemUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenNameConsistsOfSpaces_shouldTriggerPatternValidationError() throws Exception {
        String jsonContent = "{" +
                "    \"name\": \"   \"" +
                "}";

        String expectedError = "Название не должно быть пустым или состоять только из пробелов";

        ItemUpdateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasPatternErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasPatternErrorMessage).isTrue();
    }

    @Test
    void deserialize_whenDescriptionConsistsOfSpaces_shouldTriggerPatternValidationError() throws Exception {
        String jsonContent = "{" +
                "    \"description\": \"\\t   \"" +
                "}";

        String expectedError = "Описание не должно быть пустым или состоять только из пробелов";

        ItemUpdateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasPatternErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasPatternErrorMessage).isTrue();
    }
}
