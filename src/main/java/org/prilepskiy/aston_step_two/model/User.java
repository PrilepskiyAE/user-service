package org.prilepskiy.aston_step_two.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Сущность пользователя системы.
 *
 * <p>
 * Класс представляет таблицу {@code users} в базе данных и используется
 * Hibernate/JPA для сохранения, получения, обновления и удаления данных
 * о пользователях.
 * </p>
 *
 * <p>
 * Каждый пользователь содержит уникальный идентификатор, имя, email,
 * возраст и дату создания записи.
 * </p>
 */
    @Entity
    @Table(name = "users")
    public class User {

        /**
        * Уникальный идентификатор пользователя.
        *
        * <p>
        * Значение генерируется базой данных автоматически с использованием
        * стратегии {@link GenerationType#IDENTITY}.
        * </p>
        */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

     /**
         * Имя пользователя.
         *
         * <p>
        * Поле является обязательным и соответствует колонке {@code name}
         * в таблице {@code users}. Максимальная длина значения — 100 символов.
        * </p>
        */
        @Column(name = "name", nullable = false, length = 100)
        private String name;


        /**
         * Email пользователя.
         *
         * <p>
         * Поле является обязательным, должно быть уникальным и соответствует
         * колонке {@code email} в таблице {@code users}. Максимальная длина
         * значения — 255 символов.
         * </p>
         */
        @Column(name = "email", nullable = false, unique = true, length = 255)
        private String email;


        /**
         * Возраст пользователя.
         *
         * <p>
        * Поле является обязательным и соответствует колонке {@code age}
         * в таблице {@code users}.
         * </p>
         */
        @Column(name = "age", nullable = false)
        private int age;

        /**
        * Дата и время создания записи пользователя.
         *
        * <p>
        * Поле является обязательным и не должно обновляться после создания
        * записи, так как имеет параметр {@code updatable = false}.
        * </p>
        */

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;


        public User() {}

        /**
        * Создает нового пользователя с указанными именем, email и возрастом.
        *
         * <p>
         * Дата создания {@code createdAt} устанавливается автоматически
         * в момент создания объекта с помощью {@link LocalDateTime#now()}.
         * </p>
         **
         * @param name  имя пользователя.
         * @param email email пользователя.
         * @param age   возраст пользователя.
         */

        public User(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
            this.createdAt = LocalDateTime.now();
        }

        /**
        * Возвращает идентификатор пользователя.
        *
        * @return идентификатор пользователя.
        */
        public Long getId() {
            return id;
        }

     /**
        * Устанавливает идентификатор пользователя.
        *
        * <p>
        * Обычно идентификатор не задается вручную, так как он генерируется
        * базой данных автоматически.
        * </p>
         *
         * @param id идентификатор пользователя.
     */

        public void setId(Long id) {
            this.id = id;
        }

        /**
         * Возвращает имя пользователя.
         *
        * @return имя пользователя.
         */
        public String getName() {
            return name;
        }

        /**
        * Устанавливает имя пользователя.
        *
        * @param name имя пользователя.
        */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Возвращает email пользователя.
         *
         * @return email пользователя.
         */

        public String getEmail() {
            return email;
        }

        /**
        * Устанавливает email пользователя.
        *
        * @param email email пользователя.
        */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
        * Возвращает возраст пользователя.
        *
        * @return возраст пользователя.
        */

        public int getAge() {
            return age;
        }

        /**
        * Устанавливает возраст пользователя.
        *
        * @param age возраст пользователя.
        */

        public void setAge(int age) {
            this.age = age;
        }

         /**
         * Возвращает строковое представление пользователя.
         *
         * <p>
         * Используется для удобного вывода информации об объекте в логах,
         * консоли или при отладке.
         * </p>
         *
         * @return строковое представление объекта {@link User}.
         */
        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", age=" + age +
                    ", createdAt=" + createdAt +
                    '}';
        }
    }

