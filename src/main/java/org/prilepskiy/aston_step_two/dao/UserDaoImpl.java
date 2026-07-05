package org.prilepskiy.aston_step_two.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.prilepskiy.aston_step_two.config.HibernateUtil;
import org.prilepskiy.aston_step_two.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(user);
                tx.commit();
                logger.info("User saved: id={}, email={}", user.getId(), user.getEmail());
                return user;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to save user", e);
                throw e;
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.debug("User found by id={}", id);
                return Optional.of(user);
            } else {
                logger.debug("User not found by id={}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding user by id={}", id, e);
            throw e;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT u FROM User u", User.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            throw e;
        }
    }

    @Override
    public User update(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                User merged = session.merge(user);
                tx.commit();
                logger.info("User updated: id={}", merged.getId());
                return merged;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to update user", e);
                throw e;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user == null) {
                    logger.warn("Attempt to delete non-existing user id={}", id);
                    return false;
                }
                session.remove(user);
                tx.commit();
                logger.info("User deleted: id={}", id);
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to delete user id={}", id, e);
                throw e;
            }
        }
    }
}

