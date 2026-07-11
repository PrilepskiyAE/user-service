import io.qameta.allure.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.model.User;
import org.prilepskiy.aston_step_two.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Epic("Service Layer Tests")
@Feature("UserServiceImpl Business Logic")
@ExtendWith(MockitoExtension.class)

public class UserServiceImplTest extends BaseTest{
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("createUser: должен проверить email, сохранить пользователя и вернуть результат")
    @Description("Проверяет, что createUser проверяет существование пользователя по email, делегирует сохранение в DAO, не выбрасывает исключение при корректных данных и возвращает объект.")
    @Story("Создание пользователя")
    @Owner("Prilepskiy AE")
    void test1() {

        User input = new User(name, email, age);
        User saved = new User(name, email, age);

        when(userDao.findByEmail(eq(email))).thenReturn(Optional.empty());
        when(userDao.save(any(User.class))).thenReturn(saved);

        User result = userService.createUser(name, email, age);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getAge()).isEqualTo(age);

        verify(userDao, times(1)).findByEmail(eq(email));
        verify(userDao, times(1)).save(any(User.class));

        verifyNoMoreInteractions(userDao);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("createUser: при существующем email должен выбросить IllegalArgumentException")
    @Description("Проверяет, что createUser проверяет уникальность email и выбрасывает понятное исключение, если пользователь уже есть.")
    @Story("Уникальность email при создании")
    @Owner("Prilepskiy AE")
    void test2() {

        when(userDao.findByEmail(eq(email))).thenReturn(Optional.of(new User()));

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(name, email, age)
        );
        verify(userDao).findByEmail(eq(email));
        verifyNoMoreInteractions(userDao);
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("getUserById: делегирует findById и возвращает Optional")
    @Description("Проверяет, что getUserById просто вызывает DAO и корректно передаёт результат.")
    @Story("Поиск пользователя по ID")
    @Owner("Prilepskiy AE")
    void test3(){
        Long id = 1L;
        User expected = new User(name, email, age);
        when(userDao.findById(eq(id))).thenReturn(Optional.of(expected));

        Optional<User> result = userService.getUserById(id);

        assertThat(result).isEqualTo(Optional.of(expected));
        verify(userDao).findById(eq(id));
        verifyNoMoreInteractions(userDao);
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("getUserById: для несуществующего ID возвращает пустой Optional")
    @Description("Проверяет обработку случая, когда пользователь не найден.")
    @Story("Поиск по несуществующему ID")
    @Owner("Prilepskiy AE")
    void test4(){
        Long nonExistingId = 999L;
        when(userDao.findById(eq(nonExistingId))).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(nonExistingId);

        assertThat(result).isEmpty();
        verify(userDao).findById(eq(nonExistingId));
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("getAllUsers: делегирует findAll и возвращает список")
    @Description("Проверяет, что getAllUsers просто возвращает результат DAO без изменений.")
    @Story("Получение всех пользователей")
    @Owner("Prilepskiy AE")
    void test5(){
        List<User> expected = List.of(
                new User( name, email, age),
                new User( "test1", "test_test@test.com", 40)
        );
        when(userDao.findAll()).thenReturn(expected);

        List<User> result = userService.getAllUsers();

        assertThat(result).isEqualTo(expected);
        verify(userDao).findAll();
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("updateUser: обновляет пользователя, проверяет уникальность email (если email меняется)")
    @Description("Проверяет логику обновления: проверка уникальности email (кроме текущего пользователя), вызов update в DAO и возврат обновлённого объекта.")
    @Story("Обновление пользователя")
    @Owner("Prilepskiy AE")
    void test6(){
        Long id = 1L;
        String newEmail = "test_alex.new@test.com";
        User existing = new User(name, email, 50);

        when(userDao.findById(eq(id))).thenReturn(Optional.of(existing));
        when(userDao.findByEmail(eq(newEmail))).thenReturn(Optional.empty());

        User updated = new User( name, newEmail, 51);
        when(userDao.update(any(User.class))).thenReturn(updated);

        User result = userService.updateUser(id, "Frank Updated", newEmail, 51);

        assertThat(result).isEqualTo(updated);
        verify(userDao).findById(eq(id));
        verify(userDao).findByEmail(eq(newEmail));
        verify(userDao).update(any(User.class));
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("deleteUserById: делегирует удаление в DAO и возвращает результат")
    @Description("Проверяет, что deleteUserById корректно передаёт ID в DAO и возвращает булево значение.")
    @Story("Удаление пользователя")
    @Owner("Prilepskiy AE")
    void test7(){
        Long id = 1L;
        boolean expectedResult = true;
        when(userDao.deleteById(eq(id))).thenReturn(expectedResult);

        boolean result = userService.deleteUserById(id);

        assertThat(result).isEqualTo(expectedResult);
        verify(userDao).deleteById(eq(id));
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("isEmailExists: делегирует проверку в DAO")
    @Description("Проверяет, что isEmailExists просто оборачивает вызов DAO.")
    @Story("Проверка существования email")
    @Owner("Prilepskiy AE")
    void test8(){
        boolean exists = true;
        when(userDao.findByEmail(eq(email))).thenReturn(Optional.of(new User()));

        boolean result = userService.isEmailExists(email);

        assertThat(result).isEqualTo(exists);
        verify(userDao).findByEmail(eq(email));
        verifyNoMoreInteractions(userDao);
    }
}
