package org.prilepskiy.aston_step_two.controller;

import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.dao.UserDaoImpl;
import org.prilepskiy.aston_step_two.model.User;
import org.prilepskiy.aston_step_two.utils.ConsoleHelper;

import java.util.List;
import java.util.Optional;

public class UserConsoleController implements UserController {

    private final ConsoleHelper consoleHelper;
    private final UserDao userDao;
    public UserConsoleController( ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
        this.userDao = new UserDaoImpl();
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
        consoleHelper.readLine("Нажмите любую кнопку! ");
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
                case 1:
                    createUser();
                    break;
                case 2:
                    getUserById();
                    break;
                case 3:
                    getAllUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
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

            if (userDao.findByEmail(email).isPresent()) {
                System.out.println("Пользователь с таким email уже существует. Попробуйте другой.");
                continue;
            }
            break;
        }


        int age = consoleHelper.readValidAge("Возраст: ");
        User user = new User(name, email, age);
        User saved = userDao.save(user);
        System.out.println("Пользователь успешно создан: " + saved);
        clearConsole();
    }

    @Override
    public void getUserById() {
        System.out.println("=======Найти пользователя по ID======");
        long id = consoleHelper.readLong("Введите идентификатор пользователя: ");
        Optional<User> opt = userDao.findById(id);
        opt.ifPresentOrElse(
                u -> System.out.println("Нашел: " + u),
                () -> System.out.println("Пользователь не найден")
        );
        clearConsole();
    }

    @Override
    public void getAllUsers() {
        System.out.println("=======Показать всех пользователей======");
        List<User> users = userDao.findAll();
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
        long id = consoleHelper.readLong("Введите идентификатор пользователя для обновления: ");
        Optional<User> opt = userDao.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Пользователь не найден");
            return;
        }
        User existing = opt.get();
        System.out.println("Текущий: " + existing);

        String name = consoleHelper.readValidName("Введите новое имя: ");
        String email;
        while (true) {
            email = consoleHelper.readValidEmail("Введите новый email: ");

            if (userDao.findByEmail(email).isPresent()) {
                System.out.println("Пользователь с таким email уже существует. Попробуйте другой.");
                continue;
            }
            break;

        }

        int age = consoleHelper.readValidAge("Введите новый возраст: ");

        existing.setName(name);
        existing.setEmail(email);
        existing.setAge(age);

        User updated = userDao.update(existing);
        System.out.println("Обновленно: " + updated);
        clearConsole();
    }


    @Override
    public void deleteUser() {
        System.out.println("=======Удалить пользователя======");
        long id = consoleHelper.readLong("Введите идентификатор пользователя для удаления: ");
        boolean deleted = userDao.deleteById(id);
        if (deleted) {
            System.out.println("Пользователь успешно удален");
        } else {
            System.out.println("Не удалось удалить (возможно, пользователь не существует)");
        }
        clearConsole();
    }

}
