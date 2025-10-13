package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}