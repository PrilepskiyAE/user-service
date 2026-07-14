package org.prilepskiy.aston_step_two.utils;

import java.util.regex.Pattern;
/**
 * Класс, содержащий константы приложения.
 *
 * <p>
 * Константы используются для работы с консольным меню, валидации пользовательских
 * данных и настройки ограничений для имени, возраста и email.
 * </p>
 *
 * <p>
 * Класс является утилитарным и не предназначен для создания объектов.
 * </p>
 */
public class Constants {
    /**
     * Код команды меню для создания пользователя.
     */
    public final static int CREATE_USER = 1;

    /**
     * Код команды меню для получения пользователя по идентификатору.
     */
    public final static int GET_USER_BY_ID = 2;

    /**
     * Код команды меню для получения списка всех пользователей.
     */
    public final static int GET_ALL_USERS = 3;

    /**
     * Код команды меню для обновления данных пользователя.
     */
    public final static int  UPDATE_USER = 4;

    /**
     * Код команды меню для удаления пользователя.
     */
    public final static int  DELETE_USER = 5;

    /**
     * Код команды меню для выхода из приложения.
     */
    public final static int  EXIT = 0;

    /**
     * Регулярное выражение для базовой проверки email-адреса.
     *
     * <p>
     * Шаблон проверяет, что email содержит локальную часть, символ {@code @}
     * и доменную часть. Дополнительные проверки email выполняются отдельно
     * в логике валидатора.
     * </p>
     */
    public final static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Минимально допустимая длина имени пользователя.
     */
    public final static int MIN_NAME_LEN = 2;

    /**
     * Максимально допустимая длина имени пользователя.
     */
    public final static int MAX_NAME_LEN = 100;

    /**
     * Регулярное выражение для проверки имени пользователя.
     *
     * <p>
     * Имя может содержать латинские буквы, кириллические буквы, пробелы,
     * дефис и апостроф.
     * </p>
     */
    public final static Pattern NAME_PATTERN  = Pattern.compile("^[A-Za-zА-Яа-яЁё\\s'-]+$");

    /**
     * Минимально допустимый возраст пользователя.
     */
    public final static int MIN_AGE_LEN = 0;

    /**
     * Максимально допустимый возраст пользователя.
     */
    public final static int MAX_AGE_LEN = 100;

    /**
     * Минимально допустимая длина email-адреса.
     */
    public final static int MIN_EMAIL_LEN = 6;

    /**
     * Максимально допустимая длина email-адреса.
     */
    public final static int MAX_EMAIL_LEN = 255;

    /**
     * Индекс первого символа строки.
     *
     * <p>
     * Используется при работе со строками, например при получении подстроки.
     * </p>
     */
    public final static int FIRST_INDEX = 0;

    /**
     * Значение сдвига индекса на одну позицию.
     *
     * <p>
     * Используется при работе со строками, например для получения части строки
     * после определенного символа.
     * </p>
     */
    public final static int SHIFT_INDEX = 1;

}
