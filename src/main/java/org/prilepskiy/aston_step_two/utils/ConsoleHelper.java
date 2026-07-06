package org.prilepskiy.aston_step_two.utils;

import java.util.Scanner;

import static org.prilepskiy.aston_step_two.utils.Constants.*;

public class ConsoleHelper {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
            }
        }
    }

    public Long readLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректный ID.");
            }
        }
    }

    public String readValidName(String message) {
        while (true) {
            String name = readLine(message);

            if (isValidName(name)) {
                return name;
            }

            System.out.println("Имя должно быть от 2 до 100 символов и содержать только буквы, пробелы, '-' или апостроф.");
        }
    }

    public int readValidAge(String message) {
        while (true) {
            int age = readInt(message);

            if (isValidAge(age)) {
                return age;
            }

            System.out.println("Возраст должен быть в диапазоне от 0 до 100.");
        }
    }

    public String readValidEmail(String message) {
        while (true) {
            String email = readLine(message);

            if (isValidEmail(email)) {
                return email;
            }

            System.out.println("Введите корректный email.");
        }
    }

    public boolean isValidName(String name) {
        if (name == null) {
            return false;
        }

        String trimmed = name.trim();

        return trimmed.length() >= MIN_NAME_LEN
                && trimmed.length() <= MAX_NAME_LEN
                && NAME_PATTERN.matcher(trimmed).matches();
    }

    public boolean isValidAge(int age) {
        return age >= MIN_AGE_LEN && age <= MAX_AGE_LEN;
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        String trimmed = email.trim();

        if (trimmed.length() < MIN_EMAIL_LEN || trimmed.length() > MAX_EMAIL_LEN) {
            return false;
        }

        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            return false;
        }

        if (trimmed.contains("..")) {
            return false;
        }

        int atIndex = trimmed.indexOf('@');
        if (atIndex <= FIRST_INDEX || atIndex != trimmed.lastIndexOf('@')) {
            return false;
        }

        String localPart = trimmed.substring(FIRST_INDEX, atIndex);
        String domainPart = trimmed.substring(atIndex + SHIFT_INDEX);

        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }

        if (!domainPart.contains(".")) {
            return false;
        }

        String[] labels = domainPart.split("\\.");
        for (String label : labels) {
            if (label.isEmpty()) {
                return false;
            }
            if (label.startsWith("-") || label.endsWith("-")) {
                return false;
            }
        }

        return true;
    }
}
