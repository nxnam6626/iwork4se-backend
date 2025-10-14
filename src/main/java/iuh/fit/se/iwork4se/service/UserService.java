package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
}