package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    private final Long userId = 1L;

    @Test
    void getUserById_whenValid_thenStatusIsOk() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.getUserById(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient, times(1)).getUserById(userId);
    }

    @Test
    void createUser_whenValid_thenStatusIsOk() throws Exception {
        CreateUserRequest requestDto = new CreateUserRequest();
        requestDto.setName("user");
        requestDto.setEmail("user@test.com");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.createUser(any(CreateUserRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).createUser(requestDto);
    }

    @Test
    void updateUser_whenValid_thenStatusIsOk() throws Exception {
        UpdateUserRequest requestDto = new UpdateUserRequest();
        requestDto.setName("user updated");

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.updateUser(anyLong(), any(UpdateUserRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).updateUser(userId, requestDto);
    }

    @Test
    void deleteUser_whenValid_thenStatusIsOk() throws Exception {
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient, times(1)).deleteUser(userId);
    }

    @Test
    void createUser_whenEmailIsInvalid_thenStatusIsBadRequest() throws Exception {
        CreateUserRequest requestDto = new CreateUserRequest();
        requestDto.setName("Ivan");
        requestDto.setEmail("not-an-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userClient);
    }

    @Test
    void createUser_whenNameIsEmpty_thenStatusIsBadRequest() throws Exception {
        CreateUserRequest requestDto = new CreateUserRequest();
        requestDto.setName("");
        requestDto.setEmail("ivan@yandex.ru");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userClient);
    }

    @Test
    void updateUser_whenEmailIsInvalid_thenStatusIsBadRequest() throws Exception {
        UpdateUserRequest requestDto = new UpdateUserRequest();
        requestDto.setEmail("invalid-email-format");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userClient);
    }
}
