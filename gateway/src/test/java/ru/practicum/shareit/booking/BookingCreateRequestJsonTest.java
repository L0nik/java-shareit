package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateRequestJsonTest {

    @Autowired
    private JacksonTester<BookingCreateRequest> json;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void deserialize_whenJsonIsValid_shouldCreateObjectWithoutValidationErrors() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withNano(0);

        String jsonContent = String.format(
                "{\n" +
                        "    \"itemId\": 1,\n" +
                        "    \"start\": \"%s\",\n" +
                        "    \"end\": \"%s\"\n" +
                        "}",
                start,
                end
        );

        BookingCreateRequest request = json.parseObject(jsonContent);

        assertThat(request.getItemId()).isEqualTo(1L);
        assertThat(request.getStart()).isEqualTo(start);
        assertThat(request.getEnd()).isEqualTo(end);

        Set<ConstraintViolation<BookingCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void deserialize_whenStartAfterEnd_shouldTriggerValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"itemId\": 1," +
                "    \"start\": \"2026-06-06T12:00:00\"," +
                "    \"end\": \"2026-06-05T12:00:00\"" +
                "}";

        String expectedError = "Дата начала бронирования не может быть позже даты окончания бронирования";

        BookingCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<BookingCreateRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();

        boolean hasStartBeforeEndMessage = violations.stream()
                .anyMatch(
                        v -> v.getMessage().equals(expectedError)
                );

        assertThat(hasStartBeforeEndMessage).isTrue();
    }

    @Test
    void deserialize_whenStartEqualsEnd_shouldTriggerValidationError() throws Exception {

        String jsonContent = "{" +
                "    \"itemId\": 1," +
                "    \"start\": \"2026-06-05T12:00:00\"," +
                "    \"end\": \"2026-06-05T12:00:00\"" +
                "}";

        String expectedError = "Дата начала бронирования не должна совпадать с датой окончания бронирования";

        BookingCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<BookingCreateRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();

        boolean hasStartNotEqualEndMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedError));

        assertThat(hasStartNotEqualEndMessage).isTrue();
    }

    @Test
    void deserialize_whenFieldsAreNull_shouldTriggerValidationErrors() throws Exception {

        String jsonContent = "{}";

        BookingCreateRequest request = json.parseObject(jsonContent);
        Set<ConstraintViolation<BookingCreateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(3);
    }
}
