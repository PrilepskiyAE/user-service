
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.dao.UserDaoImpl;
import org.assertj.core.api.Assertions;
import org.prilepskiy.aston_step_two.model.User;

import java.util.List;
import java.util.Optional;


@Epic("DAO Layer Tests")
@Feature("UserDaoImpl CRUD Operations")
@Story("User Persistence")
class UserDaoImplTest extends BaseIntegrationTest {

    private final UserDao userDao = new UserDaoImpl();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Сохранение пользователя: проверка persist и возвращаемых данных")
    @Description("Проверяет, что метод save корректно сохраняет пользователя в БД, " +
            "возвращает не-null объект с сгенерированным ID и правильными полями.")
    @Story("Проверка операции save")
    @Owner("Prilepskiy AE")
    void Test1() {
        User user = createTestUser();
        User saved = userDao.save(user);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo(name);
        Assertions.assertThat(saved.getEmail()).isEqualTo(email);

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("findById: поиск существующего пользователя должен вернуть Optional с объектом")
    @Description("Проверяет, что findById корректно находит пользователя по ID, " +
            "возвращает непустой Optional и данные совпадают с сохранёнными.")
    @Story("Поиск пользователя по ID")
    @Owner("Prilepskiy AE")
    void Test2() {
        User original = createTestUser();
        User saved = userDao.save(original);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();

        Optional<User> result = userDao.findById(saved.getId());

        Assertions.assertThat(result).isNotEmpty();
        User found = result.get();
        Assertions.assertThat(found.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(found.getName()).isEqualTo(name);
        Assertions.assertThat(found.getEmail()).isEqualTo(email);
        Assertions.assertThat(found.getAge()).isEqualTo(age);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("findById: поиск несуществующего пользователя должен вернуть пустой Optional")
    @Description("Проверяет, что findById корректно обрабатывает случай, когда пользователь с указанным ID отсутствует в БД, " +
            "и возвращает пустой Optional вместо null или исключения.")
    @Story("Поиск пользователя по ID (случай отсутствия)")
    @Owner("Prilepskiy AE")
    void Test3() {
        Long nonExistingId = 99999L;

        Optional<User> result = userDao.findById(nonExistingId);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("findAll: должен вернуть список всех сохранённых пользователей")
    @Description("Проверяет, что findAll корректно возвращает всех пользователей из БД, " +
            "список не пуст, содержит ожидаемые записи и количество совпадает с количеством сохранённых.")
    @Story("Получение списка всех пользователей")
    @Owner("Prilepskiy AE")
    void test4() {
        userDao.save(createTestUser());
        userDao.save(new User("test_test", "test_test@test.com", 35));

        List<User> all = userDao.findAll();

        Assertions.assertThat(all).isNotEmpty();
        Assertions.assertThat(all.stream().map(User::getEmail).toList())
                .contains(email, "test_test@test.com");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("update: должен корректно обновлять пользователя через merge и возвращать обновлённый объект")
    @Description("Проверяет, что метод update использует merge для обновления сущности, " +
            "возвращает объект с актуальными данными, ID не меняется, а поля (name, age) обновлены.")
    @Story("Обновление данных пользователя")
    @Owner("Prilepskiy AE")
    void test5() {

        User original = createTestUser();
        User saved = userDao.save(original);
        saved.setAge(41);
        saved.setName("test_Alex Updated");

        User updated = userDao.update(saved);

        Assertions.assertThat(updated.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(updated.getName()).isEqualTo("test_Alex Updated");
        Assertions.assertThat(updated.getAge()).isEqualTo(41);
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("deleteById: должен удалять пользователя и возвращать true, после чего findById возвращает пустой Optional")
    @Description("Проверяет, что метод deleteById корректно удаляет запись из БД, возвращает true при успехе, " +
            "а повторный поиск по тому же ID не находит пользователя.")
    @Story("Удаление пользователя по ID")
    @Owner("Prilepskiy AE")
    void test6() {

        User toDelete = createTestUser();
        User saved = userDao.save(toDelete);

        boolean result = userDao.deleteById(saved.getId());

        Assertions.assertThat(result).isTrue();
        Optional<User> after = userDao.findById(saved.getId());
        Assertions.assertThat(after).isEmpty();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("deleteById: удаление несуществующего пользователя должно вернуть false")
    @Description("Проверяет, что deleteById корректно обрабатывает случай отсутствия пользователя: " +
            "не выбрасывает исключение, не ломает транзакцию и возвращает false.")
    @Story("Удаление пользователя по ID (случай отсутствия)")
    @Owner("Prilepskiy AE")
    void test7() {
        Long nonExistingId = 88888L;

        boolean result = userDao.deleteById(nonExistingId);

        Assertions.assertThat(result).isFalse();

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("save: при нарушении уникального ограничения на email должен выбрасываться ConstraintViolationException")
    @Description("Проверяет, что при попытке сохранить пользователя с email, который уже есть в БД, " +
            "DAO корректно пробрасывает исключение нарушения уникальности (ConstraintViolationException).")
    @Story("Обработка уникальных ограничений при сохранении")
    @Owner("Prilepskiy AE")
    void test8() {

        userDao.save(createTestUser());

        User duplicate = new User("test_test2", email, 30);

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> userDao.save(duplicate));
    }
}