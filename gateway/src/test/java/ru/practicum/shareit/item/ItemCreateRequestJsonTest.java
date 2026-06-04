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
import ru.practicum.shareit.item.dto.ItemCreateRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCreateRequestJsonTest {

    @Autowired
    private JacksonTester<ItemCreateRequest> json;

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
                "    \"name\": \"item\"," +
                "    \"description\": \"description\"," +
                "    \"available\": true," +
                "    \"requestId\": 1" +
                "}";

        ItemCreateRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("item");
        assertThat(request.getDescription()).isEqualTo("description");
        assertThat(request.getAvailable()).isTrue();
        assertThat(request.getRequestId()).isEqualTo(1L);

        Set<ConstraintViolation<ItemCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenRequestIdIsNull_shouldBeValid() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"item\"," +
                "    \"description\": \"description\"," +
                "    \"available\": true" +
                "}";

        ItemCreateRequest request = json.parseObject(jsonContent);

        assertThat(request.getRequestId()).isNull();

        Set<ConstraintViolation<ItemCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenFieldsAreBlankOrNull_shouldTriggerValidationErrors() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"  \"," +
                "    \"description\": \"\"," +
                "    \"available\": null" +
                "}";

        ItemCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemCreateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(3);
    }

    @Test
    void deserialize_whenFieldsAreMissing_shouldTriggerValidationErrors() throws Exception {

        String jsonContent = "{}";

        ItemCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<ItemCreateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(3);
    }
}
