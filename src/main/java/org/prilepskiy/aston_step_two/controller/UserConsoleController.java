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

public class UserConsoleController implements UserController {

    private static UserConsoleController instance;

    private final ConsoleHelper consoleHelper;
    private final UserService userService;

    private UserConsoleController() {
        this.consoleHelper = new ConsoleHelper();
        UserDao  dao = new UserDaoImpl();
        this.userService = new UserServiceImpl(dao);
    }

    public static UserConsoleController getInstance() {
        if (instance == null) {
            instance = new UserConsoleController();
        }
        return instance;
    }

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

    private void clearConsole() {
        consoleHelper.readLine("Нажмите ENTER! ");
//        Для удобства, на случай если нужна очистка консоли
//        for (int i = 0; i < 50; i++) {
//            System.out.println();
//        }
    }
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
