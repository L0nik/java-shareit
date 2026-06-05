package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private final Long userId = 1L;
    private final Long itemId = 2L;

    @Test
    void createItem_whenValid_thenStatusIsOk() throws Exception {
        ItemCreateRequest request = new ItemCreateRequest();
        request.setName("item");
        request.setDescription("description");
        request.setAvailable(true);

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.createItem(anyLong(), any(ItemCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).createItem(userId, request);
    }

    @Test
    void createItem_whenMissingUserHeader_thenStatusIsBadRequest() throws Exception {
        ItemCreateRequest request = new ItemCreateRequest();
        request.setName("item");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemClient);
    }

    @Test
    void updateItem_whenValid_thenStatusIsOk() throws Exception {
        ItemUpdateRequest request = new ItemUpdateRequest();
        request.setName("item updated");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.updateItem(anyLong(), anyLong(), any(ItemUpdateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).updateItem(userId, itemId, request);
    }

    @Test
    void getItemById_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.getItemById(anyLong(), anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).getItemById(userId, itemId);
    }

    @Test
    void getItemsByOwner_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.getItemsByOwner(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).getItemsByOwner(userId);
    }

    @Test
    void searchForItems_whenValid_thenStatusIsOk() throws Exception {
        String searchText = "item";
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.searchForItems(anyLong(), anyString())).thenReturn(mockResponse);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", searchText))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).searchForItems(userId, searchText);
    }

    @Test
    void searchForItems_whenMissingTextParam_thenStatusIsBadRequest() throws Exception {

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemClient);
    }

    @Test
    void createComment_whenValid_thenStatusIsOk() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setText("comment text");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.createComment(anyLong(), anyLong(), any(CommentCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).createComment(userId, itemId, request);
    }
}
