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
import ru.practicum.shareit.item.dto.CommentCreateRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentCreateRequestJsonTest {

    @Autowired
    private JacksonTester<CommentCreateRequest> json;

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
                "    \"text\": \"comment text\"" +
                "}";

        CommentCreateRequest request = json.parseObject(jsonContent);

        assertThat(request.getText()).isEqualTo("comment text");

        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenTextIsBlank_shouldTriggerNotBlankValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"text\": \"     \"" +
                "}";

        String expectedError = "Отсутствует текст комментария";

        CommentCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);


        assertThat(violations).hasSize(1);

        boolean hasNotBlankErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasNotBlankErrorMessage).isTrue();
    }

    @Test
    void deserialize_whenTextIsEmptyOrMissing_shouldTriggerNotBlankValidationError() throws Exception {

        String jsonContent = "{}";

        String expectedError = "Отсутствует текст комментария";

        CommentCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasNotBlankErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasNotBlankErrorMessage).isTrue();
    }
}
