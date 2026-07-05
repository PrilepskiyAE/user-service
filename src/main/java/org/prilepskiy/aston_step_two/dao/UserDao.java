package org.prilepskiy.aston_step_two.dao;

import org.prilepskiy.aston_step_two.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    User update(User user);
    boolean deleteById(Long id);
}
