package org.prilepskiy.aston_step_two.service;

import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.dao.UserDaoImpl;
import org.prilepskiy.aston_step_two.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao = new UserDaoImpl();;

    @Override
    public User createUser(String name, String email, int age) {
        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + email);
        }
        User user = new User(name, email, age);
        return userDao.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User updateUser(Long id, String name, String email, int age) {
        Optional<User> existingOpt = userDao.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден по ID: " + id);
        }

        User existing = existingOpt.get();

        // Проверка уникальности email: если email меняется и новый уже занят
        if (!existing.getEmail().equals(email) && userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + email);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setAge(age);

        return userDao.update(existing);
    }

    @Override
    public boolean deleteUserById(Long id) {
        return userDao.deleteById(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userDao.findByEmail(email).isPresent();
    }
}
