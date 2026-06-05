package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private final Long userId = 1L;
    private final Long requestId = 2L;

    @Test
    void createItemRequest_whenValid_thenStatusIsOk() throws Exception {
        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("description");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemRequestClient.createItemRequest(anyLong(), any(ItemRequestCreateDto.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).createItemRequest(userId, requestDto);
    }

    @Test
    void createItemRequest_whenMissingUserHeader_thenStatusIsBadRequest() throws Exception {
        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("description");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestClient);
    }

    @Test
    void getItemRequestsOfUser_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemRequestClient.getItemRequestsOfUser(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).getItemRequestsOfUser(userId);
    }

    @Test
    void getAllItemRequests_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemRequestClient.getAllItemRequests(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).getAllItemRequests(userId);
    }

    @Test
    void getItemRequestById_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemRequestClient.getItemRequestById(anyLong(), anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).getItemRequestById(userId, requestId);
    }
}
