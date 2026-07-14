package org.prilepskiy.aston_step_two.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Утилитный класс для настройки и управления жизненным циклом {@link SessionFactory}.
 * Обеспечивает централизованный доступ к фабрике сессий для работы с базой данных.
 *
 * <p>Инициализирует SessionFactory при первой загрузке класса, используя
 * конфигурационный файл по умолчанию (hibernate.cfg.xml).</p>
 */

public class HibernateUtil {
    /**
     * Единственный экземпляр SessionFactory для всего приложения.
     */
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Создает и настраивает объект {@link SessionFactory}.
     *
     * @return сконфигурированный экземпляр SessionFactory.
     * @throws ExceptionInInitializerError если возникла ошибка при чтении конфигурации
     *                                      или создании реестра сервисов.
     */

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            return new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Возвращает глобальный экземпляр фабрики сессий.
     *
     * @return действующий объект {@link SessionFactory}.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Закрывает фабрику сессий и освобождает все связанные ресурсы,
     * такие как пулы соединений и кэши.
     * Должен вызываться при завершении работы приложения.
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
