package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(ItemClient.class)
class ItemClientTest {

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private MockRestServiceServer mockServer;

    private final long userId = 1L;
    private final Long itemId = 2L;

    @Test
    void createItem_shouldSendCorrectPostRequest() {
        ItemCreateRequest request = new ItemCreateRequest();
        request.setName("item");
        request.setDescription("description");
        request.setAvailable(true);

        mockServer.expect(requestTo(containsString("/items")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"id\": 2}"));

        ResponseEntity<Object> response = itemClient.createItem(userId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void updateItem_shouldSendCorrectPutRequest() {
        ItemUpdateRequest request = new ItemUpdateRequest();
        request.setName("item updated");

        mockServer.expect(requestTo(containsString("/items/2")))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        ResponseEntity<Object> response = itemClient.updateItem(userId, itemId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void getItemById_shouldSendCorrectGetRequestWithHeader() {
        mockServer.expect(requestTo(containsString("/items/2")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        ResponseEntity<Object> response = itemClient.getItemById(userId, itemId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void getItemsByOwner_shouldSendCorrectGetRequestToRoot() {
        mockServer.expect(requestTo(containsString("/items")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("[]"));

        ResponseEntity<Object> response = itemClient.getItemsByOwner(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void searchForItems_shouldSendCorrectGetRequestWithQueryParam() {
        String searchText = "item";

        mockServer.expect(requestTo(containsString("/items/search?text=item")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("[]"));

        ResponseEntity<Object> response = itemClient.searchForItems(userId, searchText);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }

    @Test
    void createComment_shouldSendCorrectPostRequestToSubPath() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setText("comment text");

        mockServer.expect(requestTo(containsString("/items/2/comment")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        ResponseEntity<Object> response = itemClient.createComment(userId, itemId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        mockServer.verify();
    }
}
