package org.prilepskiy.aston_step_two.service;

import org.prilepskiy.aston_step_two.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String name, String email, int age);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, String name, String email, int age);
    boolean deleteUserById(Long id);
    boolean isEmailExists(String email);
}
