package org.prilepskiy.aston_step_two.controller;

import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.dao.UserDaoImpl;
import org.prilepskiy.aston_step_two.model.User;
import org.prilepskiy.aston_step_two.service.UserService;
import org.prilepskiy.aston_step_two.service.UserServiceImpl;
import org.prilepskiy.aston_step_two.utils.ConsoleHelper;

import java.util.List;
import java.util.Optional;

import static org.prilepskiy.aston_step_two.utils.Constants.*;

/**
 * Консольная реализация контроллера пользователей.
 *
 * <p>
 * Класс отвечает за взаимодействие с пользователем через консольное меню
 * и предоставляет операции создания, поиска, просмотра, обновления и удаления
 * пользователей.
 * </p>
 *
 * <p>
 * Использует {@link ConsoleHelper} для чтения и валидации пользовательского ввода,
 * а также {@link UserService} для выполнения бизнес-логики, связанной с пользователями.
 * </p>
 *
 * <p>
 * Класс реализует паттерн Singleton: в приложении создается только один экземпляр
 * {@code UserConsoleController}.
 * </p>
 *
 * @see UserController
 * @see UserService
 * @see ConsoleHelper
 */
public class UserConsoleController implements UserController {


    /**
     * Единственный экземпляр консольного контроллера пользователей.
     */
    private static UserConsoleController instance;

    /**
     * Вспомогательный объект для работы с консолью:
     * чтения строк, чисел и валидированных пользовательских данных.
     */
    private final ConsoleHelper consoleHelper;

    /**
     * Сервис для выполнения операций над пользователями.
     */
    private final UserService userService;

    /**
     * Создает экземпляр {@code UserConsoleController}.
     *
     * <p>
     * Конструктор приватный, так как класс использует Singleton.
     * При создании контроллера инициализируются:
     * </p>
     *
     * <ul>
     *     <li>{@link ConsoleHelper} для работы с консольным вводом;</li>
     *     <li>{@link UserDao} через реализацию {@link UserDaoImpl};</li>
     *     <li>{@link UserService} через реализацию {@link UserServiceImpl}.</li>
     * </ul>
     */
    private UserConsoleController() {
        this.consoleHelper = new ConsoleHelper();
        UserDao  dao = new UserDaoImpl();
        this.userService = new UserServiceImpl(dao);
    }

    /**
     * Возвращает единственный экземпляр {@code UserConsoleController}.
     *
     * <p>
     * Если экземпляр еще не был создан, метод создает его.
     * </p>
     *
     * @return единственный экземпляр {@code UserConsoleController}.
     */
    public static UserConsoleController getInstance() {
        if (instance == null) {
            instance = new UserConsoleController();
        }
        return instance;
    }

    /**
     * Отображает главное меню пользовательского сервиса в консоли.
     *
     * <p>
     * Меню содержит пункты для выполнения основных CRUD-операций:
     * создание, поиск, просмотр всех пользователей, обновление и удаление.
     * </p>
     */
    private void showMenu() {
        System.out.println("========== USER SERVICE ==========");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Показать всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.println("==================================");
    }

    /**
     * Приостанавливает выполнение программы до нажатия клавиши ENTER.
     *
     * <p>
     * Метод используется после выполнения операций, чтобы пользователь успел
     * прочитать результат перед возвратом к главному меню.
     * </p>
     *
     * <p>
     * При необходимости метод может быть расширен логикой визуальной очистки консоли.
     * </p>
     */
    private void clearConsole() {
        consoleHelper.readLine("Нажмите ENTER! ");
//        Для удобства, на случай если нужна очистка консоли
//        for (int i = 0; i < 50; i++) {
//            System.out.println();
//        }
    }

    /**
     * Запускает основной цикл работы консольного контроллера.
     *
     * <p>
     * Метод отображает меню, считывает выбор пользователя и вызывает
     * соответствующий метод контроллера. Цикл продолжается до тех пор,
     * пока пользователь не выберет пункт выхода.
     * </p>
     *
     * <p>
     * Доступные действия определяются константами интерфейса {@link UserController},
     * такими как {@code CREATE_USER}, {@code GET_USER_BY_ID}, {@code GET_ALL_USERS},
     * {@code UPDATE_USER}, {@code DELETE_USER} и {@code EXIT}.
     * </p>
     */
    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = consoleHelper.readInt("Выбери пункт меню: ");

            switch (choice) {
                case CREATE_USER:
                    createUser();
                    break;
                case GET_USER_BY_ID:
                    getUserById();
                    break;
                case GET_ALL_USERS:
                    getAllUsers();
                    break;
                case UPDATE_USER:
                    updateUser();
                    break;
                case DELETE_USER:
                    deleteUser();
                    break;
                case EXIT:
                    running = false;
                    System.out.println("Выход из программы...");
                    break;
                default:
                    System.out.println("Неверный пункт меню. Попробуй ещё раз.");
            }

            System.out.println();
        }
    }

    /**
     * Создает нового пользователя на основе данных, введенных через консоль.
     *
     * <p>
     * Метод запрашивает имя, email и возраст пользователя. Перед созданием
     * проверяется, существует ли пользователь с таким email. Если email уже занят,
     * пользователь должен ввести другой email.
     * </p>
     *
     * <p>
     * При успешном создании выводится информация о сохраненном пользователе.
     * Если сервис выбрасывает {@link IllegalArgumentException}, в консоль
     * выводится сообщение об ошибке.
     * </p>
     */
    @Override
    public void createUser() {
        System.out.println("=======Создание пользователя======");
        String name = consoleHelper.readValidName("Введите имя: ");
        String email;
        while (true) {
            email = consoleHelper.readValidEmail("Введите Email: ");
            if (userService.isEmailExists(email)) {
                System.out.println("Пользователь с таким email уже существует. Попробуйте другой.");
                continue;
            }
            break;
        }
        int age = consoleHelper.readValidAge("Возраст: ");

        try {
            User saved = userService.createUser(name, email, age);
            System.out.println("Пользователь успешно создан: " + saved);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании: " + e.getMessage());
        } finally {
            clearConsole();
        }
    }

    /**
     * Ищет пользователя по идентификатору, введенному через консоль.
     *
     * <p>
     * Если пользователь найден, информация о нем выводится в консоль.
     * Если пользователь отсутствует, выводится сообщение о том, что он не найден.
     * </p>
     */
    @Override
    public void getUserById() {
        System.out.println("=======Найти пользователя по ID======");
        long id = consoleHelper.readLong("Введите идентификатор пользователя: ");

        Optional<User> opt = userService.getUserById(id);
        opt.ifPresentOrElse(
                u -> System.out.println("Нашел: " + u),
                () -> System.out.println("Пользователь не найден")
        );
        clearConsole();
    }

    /**
     * Выводит в консоль список всех пользователей.
     *
     * <p>
     * Если список пользователей пуст, выводится сообщение о том, что пользователи
     * не найдены. Иначе каждый пользователь выводится отдельной строкой.
     * </p>
     */
    @Override
    public void getAllUsers() {
        System.out.println("=======Показать всех пользователей======");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            users.forEach(System.out::println);
        }
        clearConsole();
    }

    /**
     * Обновляет данные пользователя по идентификатору.
     *
     * <p>
     * Метод запрашивает ID пользователя, а затем новые значения имени, email
     * и возраста. Перед обновлением проверяется, существует ли пользователь
     * с указанным email. Если email уже используется, пользователь должен
     * ввести другой email.
     * </p>
     *
     * <p>
     * При успешном обновлении выводится обновленная информация о пользователе.
     * Если сервис выбрасывает {@link IllegalArgumentException}, в консоль
     * выводится сообщение об ошибке.
     * </p>
     */
    @Override
    public void updateUser() {
        System.out.println("=======Обновить пользователя======");
        long id = consoleHelper.readLong("Введите ID пользователя для обновления: ");

        try {
            String name = consoleHelper.readValidName("Введите новое имя: ");
            String email;
            while (true) {
                email = consoleHelper.readValidEmail("Введите новый email: ");
                if (userService.isEmailExists(email)) {
                    System.out.println("Пользователь с таким email уже существует. Попробуйте другой.");
                    continue;
                }
                break;
            }
            int age = consoleHelper.readValidAge("Введите новый возраст: ");

            User updated = userService.updateUser(id, name, email, age);
            System.out.println("Обновлено: " + updated);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        } finally {
            clearConsole();
        }
    }

    /**
     * Удаляет пользователя по идентификатору, введенному через консоль.
     *
     * <p>
     * Если пользователь был успешно удален, выводится сообщение об успешном удалении.
     * Если удалить пользователя не удалось, выводится сообщение о возможном отсутствии
     * пользователя с указанным ID.
     * </p>
     */
    @Override
    public void deleteUser() {
        System.out.println("=======Удалить пользователя======");
        long id = consoleHelper.readLong("Введите ID пользователя для удаления: ");

        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            System.out.println("Пользователь успешно удален");
        } else {
            System.out.println("Не удалось удалить (возможно, пользователь не существует)");
        }
        clearConsole();
    }

}
