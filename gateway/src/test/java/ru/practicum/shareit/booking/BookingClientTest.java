package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(BookingClient.class)
class BookingClientTest {

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private MockRestServiceServer mockServer;

    private final long userId = 1L;
    private final Long bookingId = 2L;

    @Test
    void getBookingsOfUser_shouldSendCorrectRequest() {
        mockServer.expect(requestTo(containsString("/bookings?state=FUTURE")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("[]"));

        ResponseEntity<Object> response = bookingClient.getBookingsOfUser(userId, BookingState.FUTURE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void getBookingsOfOwner_shouldSendCorrectRequest() {
        mockServer.expect(requestTo(containsString("/bookings/owner?state=ALL")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("[]"));

        ResponseEntity<Object> response = bookingClient.getBookingsOfOwner(userId, BookingState.ALL);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void createBooking_shouldSendCorrectRequestWithBody() {
        BookingCreateRequest requestDto = new BookingCreateRequest();
        requestDto.setItemId(10L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        mockServer.expect(requestTo(containsString("/bookings")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"id\": 1}"));

        ResponseEntity<Object> response = bookingClient.createBooking(userId, requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void getBookingById_shouldSendCorrectRequest() {

        mockServer.expect(requestTo(containsString("/bookings/2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        ResponseEntity<Object> response = bookingClient.getBookingById(bookingId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void approveRejectBooking_shouldSendCorrectPatchRequest() {
        mockServer.expect(requestTo(containsString("/bookings/2?approved=true")))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        ResponseEntity<Object> response = bookingClient.approveRejectBooking(userId, bookingId, true);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }
}
