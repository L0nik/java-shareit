package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserStorage {

    public User getUserById(Long id);

    public User addUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

    public Optional<User> getUserByEmail(String email);

    public void checkIfUserExists(Long id);

}
