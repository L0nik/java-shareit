package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UpdateUserRequestJsonTest {

    @Autowired
    private JacksonTester<UpdateUserRequest> json;

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
                "    \"name\": \"user\"," +
                "    \"email\": \"user@test.com\"" +
                "}";

        UpdateUserRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("user");
        assertThat(request.getEmail()).isEqualTo("user@test.com");

        assertThat(request.hasName()).isTrue();
        assertThat(request.hasEmail()).isTrue();

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();

    }

    @Test
    void deserialize_whenJsonIsEmpty_shouldBeValidForPartialUpdate() throws Exception {

        String jsonContent = "{}";

        UpdateUserRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isNull();
        assertThat(request.getEmail()).isNull();

        assertThat(request.hasName()).isFalse();
        assertThat(request.hasEmail()).isFalse();

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();

    }

    @Test
    void deserialize_whenNameConsistsOfSpaces_shouldTriggerPatternValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"   \"" +
                "}";

        String expectedError = "Имя не должно быть пустым или состоять только из пробелов";

        UpdateUserRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        boolean hasPatternErrorMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasPatternErrorMessage).isTrue();

    }

    @Test
    void deserialize_whenEmailIsInvalid_shouldTriggerEmailValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"email\": \"invalid-email\"" +
                "}";

        UpdateUserRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

    }
}
