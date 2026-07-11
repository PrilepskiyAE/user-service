import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.prilepskiy.aston_step_two.config.HibernateUtil;

abstract public class BaseTest {

    protected final String name = "test_Alex";
    protected final String email = "test_alex@test.com";
    protected final int age = 34;

    @AfterEach
    void setDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User u WHERE u.email LIKE :prefix")
                    .setParameter("prefix", "test_%")
                    .executeUpdate();
            tx.commit();
        }
    }
}
