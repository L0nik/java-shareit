package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private final Long userId = 1L;
    private final Long bookingId = 2L;

    @Test
    void createBooking_whenValid_thenStatusIsOk() throws Exception {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.createBooking(anyLong(), any(BookingCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).createBooking(userId, request);
    }

    @Test
    void createBooking_whenMissingUserHeader_thenStatusIsBadRequest() throws Exception {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingClient);
    }

    @Test
    void approveRejectBooking_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.approveRejectBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(mockResponse);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).approveRejectBooking(userId, bookingId, true);
    }

    @Test
    void getBookingById_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingById(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookingById(bookingId);
    }

    @Test
    void getBookingsOfUser_withDefaultState_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingsOfUser(anyLong(), any(BookingState.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookingsOfUser(userId, BookingState.ALL);
    }

    @Test
    void getBookingsOfUser_withCustomState_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingsOfUser(anyLong(), any(BookingState.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookingsOfUser(userId, BookingState.FUTURE);
    }

    @Test
    void getBookingsOfUser_whenStateIsUnknown_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingClient);
    }

    @Test
    void getBookingsOfOwner_withDefaultState_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingsOfOwner(anyLong(), any(BookingState.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookingsOfOwner(userId, BookingState.ALL);
    }

    @Test
    void getBookingsOfOwner_withCustomState_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingsOfOwner(anyLong(), any(BookingState.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getBookingsOfOwner(userId, BookingState.FUTURE);
    }

    @Test
    void getBookingsOfOwner_whenStateIsUnknown_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingClient);
    }

}
