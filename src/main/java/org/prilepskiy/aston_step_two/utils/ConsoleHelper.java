package org.prilepskiy.aston_step_two.utils;

import java.util.Scanner;

import static org.prilepskiy.aston_step_two.utils.Constants.*;

/**
 * Вспомогательный класс для работы с консольным вводом пользователя.
 *
 * <p>
 * Класс инкапсулирует логику чтения строковых, числовых и валидированных
 * значений из консоли. Используется для получения данных от пользователя
 * через стандартный поток ввода {@link System#in}.
 * </p>
 *
 * <p>
 * Помимо базового чтения данных, класс содержит методы валидации имени,
 * возраста и email, а также методы, которые повторяют ввод до тех пор,
 * пока пользователь не введет корректное значение.
 * </p>
 *
 * <p>
 * Правила валидации основаны на константах и регулярных выражениях,
 * определенных в классе {@link Constants}.
 * </p>
 *
 */
public class ConsoleHelper {

    /**
     * Объект {@link Scanner}, используемый для чтения данных из консоли.
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Считывает строку из консоли.
     *
     * <p>
     * Перед чтением выводит пользователю переданное сообщение-приглашение.
     * Метод возвращает строку в том виде, в котором она была введена
     * пользователем.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return строка, введенная пользователем.
     */
    public String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Считывает целое число типа {@code int} из консоли.
     *
     * <p>
     * Метод выводит переданное сообщение, после чего ожидает ввод числа.
     * Если пользователь вводит значение, которое невозможно преобразовать
     * в {@code int}, выводится сообщение об ошибке, и ввод повторяется.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return корректно введенное целое число типа {@code int}.
     */
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

    /**
     * Считывает целое число типа {@link Long} из консоли.
     *
     * <p>
     * Метод выводит переданное сообщение, после чего ожидает ввод числового
     * идентификатора. Если пользователь вводит значение, которое невозможно
     * преобразовать в {@link Long}, выводится сообщение об ошибке, и ввод
     * повторяется.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return корректно введенное целое число типа {@link Long}.
     */
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

    /**
     * Считывает и валидирует имя пользователя.
     *
     * <p>
     * Метод повторяет ввод до тех пор, пока пользователь не введет имя,
     * соответствующее правилам метода {@link #isValidName(String)}.
     * Если введенное значение некорректно, выводится сообщение с описанием
     * допустимого формата имени.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return корректное имя пользователя.
     */
    public String readValidName(String message) {
        while (true) {
            String name = readLine(message);

            if (isValidName(name)) {
                return name;
            }

            System.out.println("Имя должно быть от 2 до 100 символов и содержать только буквы, пробелы, '-' или апостроф.");
        }
    }

    /**
     * Считывает и валидирует возраст пользователя.
     *
     * <p>
     * Метод повторяет ввод до тех пор, пока пользователь не введет возраст,
     * соответствующий правилам метода {@link #isValidAge(int)}.
     * Если введенное значение некорректно, выводится сообщение с допустимым
     * диапазоном возраста.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return корректный возраст пользователя.
     */
    public int readValidAge(String message) {
        while (true) {
            int age = readInt(message);

            if (isValidAge(age)) {
                return age;
            }

            System.out.println("Возраст должен быть в диапазоне от 0 до 100.");
        }
    }

    /**
     * Считывает и валидирует email пользователя.
     *
     * <p>
     * Метод повторяет ввод до тех пор, пока пользователь не введет email,
     * соответствующий правилам метода {@link #isValidEmail(String)}.
     * Если введенное значение некорректно, выводится сообщение об ошибке.
     * </p>
     *
     * @param message сообщение, которое будет выведено перед вводом.
     * @return корректный email пользователя.
     */
    public String readValidEmail(String message) {
        while (true) {
            String email = readLine(message);

            if (isValidEmail(email)) {
                return email;
            }

            System.out.println("Введите корректный email.");
        }
    }

    /**
     * Проверяет корректность имени пользователя.
     *
     * <p>
     * Имя считается корректным, если оно:
     * </p>
     *
     * <ul>
     *     <li>не равно {@code null};</li>
     *     <li>после удаления пробелов по краям имеет длину
     *     от {@link Constants#MIN_NAME_LEN} до {@link Constants#MAX_NAME_LEN};</li>
     *     <li>соответствует регулярному выражению {@link Constants#NAME_PATTERN}.</li>
     * </ul>
     *
     * @param name имя пользователя для проверки.
     * @return {@code true}, если имя корректно;
     *         {@code false}, если имя некорректно.
     */
    public boolean isValidName(String name) {
        if (name == null) {
            return false;
        }

        String trimmed = name.trim();

        return trimmed.length() >= MIN_NAME_LEN
                && trimmed.length() <= MAX_NAME_LEN
                && NAME_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Проверяет корректность возраста пользователя.
     *
     * <p>
     * Возраст считается корректным, если он находится в диапазоне
     * от {@link Constants#MIN_AGE_LEN} до {@link Constants#MAX_AGE_LEN}
     * включительно.
     * </p>
     *
     * @param age возраст пользователя для проверки.
     * @return {@code true}, если возраст находится в допустимом диапазоне;
     *         {@code false}, если возраст некорректен.
     */
    public boolean isValidAge(int age) {
        return age >= MIN_AGE_LEN && age <= MAX_AGE_LEN;
    }

    /**
     * Проверяет корректность email-адреса.
     *
     * <p>
     * Email считается корректным, если он:
     * </p>
     *
     * <ul>
     *     <li>не равен {@code null};</li>
     *     <li>после удаления пробелов по краям имеет длину
     *     от {@link Constants#MIN_EMAIL_LEN} до {@link Constants#MAX_EMAIL_LEN};</li>
     *     <li>соответствует регулярному выражению {@link Constants#EMAIL_PATTERN};</li>
     *     <li>не содержит двух точек подряд;</li>
     *     <li>содержит ровно один символ {@code @};</li>
     *     <li>имеет непустую локальную часть перед {@code @};</li>
     *     <li>локальная часть не начинается и не заканчивается точкой;</li>
     *     <li>доменная часть содержит хотя бы одну точку;</li>
     *     <li>доменные метки не являются пустыми;</li>
     *     <li>доменные метки не начинаются и не заканчиваются дефисом.</li>
     * </ul>
     *
     * @param email email-адрес для проверки.
     * @return {@code true}, если email корректен;
     *         {@code false}, если email некорректен.
     */
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
