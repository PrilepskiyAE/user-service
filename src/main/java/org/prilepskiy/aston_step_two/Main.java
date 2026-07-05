package org.prilepskiy.aston_step_two;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.prilepskiy.aston_step_two.model.User;
import org.prilepskiy.aston_step_two.utils.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            logger.info("=== CREATE ===");
            User user = new User("Prilepskiy Test", "Prilepskiy_AE@Aston.com", 33);
            session.persist(user);
            logger.info("Created user: id={}, email={}", user.getId(), user.getEmail());

            session.flush();
            Long savedId = user.getId();
            if (savedId == null) {
                throw new IllegalStateException("User ID is null after persist");
            }

            logger.info("=== READ by ID ===");
            User loaded = session.get(User.class, savedId);
            if (loaded == null) {
                throw new IllegalStateException("User not found by ID after persist");
            }
            logger.info("Loaded user: {}", loaded);

            logger.info("=== UPDATE ===");
            loaded.setAge(34);
            loaded.setName("Prilepskiy_AE");
            session.update(loaded);
            tx.commit();

            tx = session.beginTransaction();

            logger.info("=== READ ALL ===");
            List<User> allUsers = session.createQuery("SELECT u FROM User u", User.class).list();
            logger.info("Total users in DB: {}", allUsers.size());
            allUsers.forEach(u -> logger.info("  - {}", u));

            logger.info("=== DELETE ===");
            User toDelete = session.get(User.class, savedId);
            if (toDelete != null) {
                session.delete(toDelete);
                logger.info("Deleted user with id={}", savedId);
            } else {
                logger.warn("User to delete not found (id={})", savedId);
            }

            tx.commit();
            logger.info("All operations completed successfully");

        } catch (Exception e) {
            logger.error("Error during CRUD test", e);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("CRUD test failed", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}