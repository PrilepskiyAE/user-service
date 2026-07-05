package org.prilepskiy.aston_step_two.controller;

import org.prilepskiy.aston_step_two.utils.ConsoleHelper;

public class UserConsoleController extends UserController {

    private final ConsoleHelper consoleHelper;

    public UserConsoleController( ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
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
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
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
    void createUser() {
        System.out.println("=======Создать пользователя======");
        consoleHelper.readLine("test: ");
        clearConsole();
    }

    @Override
    void getUserById() {
        System.out.println("=======Найти пользователя по ID======");
        consoleHelper.readLine("test: ");
        clearConsole();
    }

    @Override
    void getAllUsers() {
        System.out.println("=======Показать всех пользователей======");
        consoleHelper.readLine("test: ");
        clearConsole();
    }

    @Override
    void updateUser() {
        System.out.println("=======Обновить пользователя======");
        consoleHelper.readLine("test: ");
        clearConsole();
    }


    @Override
    void deleteUser() {
        System.out.println("=======Удалить пользователя======");
        consoleHelper.readLine("test: ");
        clearConsole();
    }
}
