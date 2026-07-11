import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.prilepskiy.aston_step_two.config.HibernateUtil;
import org.prilepskiy.aston_step_two.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest extends BaseTest{
    protected final String name = "test_Alex";
    protected final String email = "test_alex@test.com";
    protected final int age = 34;
    private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16"))
            .withDatabaseName("aston_test")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    void setUpContainer() {
        try {
            if (!POSTGRES_CONTAINER.isRunning()) {
                POSTGRES_CONTAINER.start();

                System.setProperty("hibernate.connection.url", POSTGRES_CONTAINER.getJdbcUrl());
                System.setProperty("hibernate.connection.username", POSTGRES_CONTAINER.getUsername());
                System.setProperty("hibernate.connection.password", POSTGRES_CONTAINER.getPassword());
                System.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
                logger.info("PostgreSQL container started on {}", POSTGRES_CONTAINER.getJdbcUrl());
            }

            HibernateUtil.getSessionFactory();
        } catch (NotFoundException e) {
            throw new IllegalStateException("Docker image postgres:16 not found locally.", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize test environment", e);
        }
    }

    @AfterAll
    void tearDownContainer() {
        System.clearProperty("hibernate.connection.url");
        System.clearProperty("hibernate.connection.username");
        System.clearProperty("hibernate.connection.password");
        System.clearProperty("hibernate.dialect");
        System.clearProperty("hibernate.hbm2ddl.auto");

       HibernateUtil.closeSessionFactory();

        if (POSTGRES_CONTAINER.isRunning()) {
            POSTGRES_CONTAINER.stop();
            logger.info("PostgreSQL container stopped.");
        }
    }
    protected User createTestUser() {
        return new User(name, email, age);
    }

}
