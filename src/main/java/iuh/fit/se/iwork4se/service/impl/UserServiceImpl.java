package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.UserRepository;
import iuh.fit.se.iwork4se.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id).map(existing -> {
            existing.setEmail(user.getEmail());
            existing.setFullName(user.getFullName());
            existing.setRole(user.getRole());
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
