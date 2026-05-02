package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@Component
public class UserMapper {

    public static User mapCreateUserDtoToUser(CreateUserRequest userData) {
        User user = new User();
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        return user;
    }

    public static User updateUserFields(User user, UpdateUserRequest userData) {

        if (userData.hasName()) {
            user.setName(userData.getName());
        }

        if (userData.hasEmail()) {
            user.setEmail(userData.getEmail());
        }

        return user;
    }
}
