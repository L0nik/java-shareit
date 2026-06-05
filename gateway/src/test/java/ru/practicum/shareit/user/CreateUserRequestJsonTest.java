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
import ru.practicum.shareit.user.dto.CreateUserRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateUserRequestJsonTest {

    @Autowired
    private JacksonTester<CreateUserRequest> json;

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
                "    \"name\": \"user\"," +
                "    \"email\": \"user@user.com\"" +
                "}";

        CreateUserRequest request = json.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("user");
        assertThat(request.getEmail()).isEqualTo("user@user.com");

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();

    }

    @Test
    void deserialize_whenNameIsBlank_shouldTriggerNotBlankValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"   \"," +
                "    \"email\": \"user@user.com\"" +
                "}";

        CreateUserRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

    }

    @Test
    void deserialize_whenEmailIsInvalid_shouldTriggerEmailValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"name\": \"user\"," +
                "    \"email\": \"not-an-email\"" +
                "}";

        CreateUserRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

    }

    @Test
    void deserialize_whenFieldsAreNull_shouldTriggerValidationErrors() throws Exception {

        String jsonContent = "{}";

        CreateUserRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);

    }
}
