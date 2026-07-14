package org.prilepskiy.aston_step_two.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.prilepskiy.aston_step_two.config.HibernateUtil;
import org.prilepskiy.aston_step_two.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса {@link UserDao} для работы с сущностью {@link User}
 * через Hibernate.
 *
 * <p>
 * Класс предоставляет базовые CRUD-операции для пользователей:
 * сохранение, поиск по идентификатору, получение списка всех пользователей,
 * обновление, удаление и поиск по email.
 * </p>
 *
 * <p>
 * Для каждой операции открывается отдельная Hibernate-сессия через
 * {@link HibernateUtil#getSessionFactory()}. Операции, изменяющие данные
 * в базе, выполняются внутри транзакции.
 * </p>
 *
 * <p>
 * Ошибки логируются с помощью SLF4J {@link Logger}, после чего исключения
 * пробрасываются выше для обработки на уровне сервиса или контроллера.
 * </p>
 */
public class UserDaoImpl implements UserDao {

    /**
     * Логгер для записи информации о выполнении DAO-операций,
     * успешных действиях и ошибках.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * <p>
     * Метод открывает новую Hibernate-сессию, начинает транзакцию,
     * сохраняет переданную сущность с помощью {@link Session#persist(Object)}
     * и фиксирует транзакцию.
     * </p>
     *
     * <p>
     * В случае ошибки транзакция откатывается, ошибка логируется,
     * а исключение пробрасывается дальше.
     * </p>
     *
     * @param user пользователь, которого необходимо сохранить.
     *             Не должен быть {@code null}.
     * @return сохраненный пользователь с заполненным идентификатором.
     * @throws RuntimeException если произошла ошибка при сохранении пользователя.
     */
    @Override
    public User save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(user);
                tx.commit();
                logger.info("User saved: id={}, email={}", user.getId(), user.getEmail());
                return user;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to save user", e);
                throw e;
            }
        }
    }

    /**
     * Ищет пользователя в базе данных по его идентификатору.
     *
     * <p>
     * Метод использует {@link Session#get(Class, Object)} для получения
     * сущности {@link User}. Если пользователь найден, возвращается
     * {@link Optional} с пользователем. Если пользователь отсутствует,
     * возвращается {@link Optional#empty()}.
     * </p>
     *
     * @param id идентификатор пользователя.
     *           Не должен быть {@code null}.
     * @return {@link Optional}, содержащий найденного пользователя,
     *         или пустой {@link Optional}, если пользователь не найден.
     * @throws RuntimeException если произошла ошибка при поиске пользователя.
     */
    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.debug("User found by id={}", id);
                return Optional.of(user);
            } else {
                logger.debug("User not found by id={}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding user by id={}", id, e);
            throw e;
        }
    }

    /**
     * Возвращает список всех пользователей из базы данных.
     *
     * <p>
     * Для получения данных используется HQL-запрос:
     * {@code SELECT u FROM User u}.
     * </p>
     *
     * @return список всех пользователей. Если пользователей нет,
     *         возвращается пустой список.
     * @throws RuntimeException если произошла ошибка при получении списка пользователей.
     */
    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT u FROM User u", User.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            throw e;
        }
    }

    /**
     * Обновляет данные существующего пользователя в базе данных.
     *
     * <p>
     * Метод открывает новую Hibernate-сессию, начинает транзакцию,
     * объединяет переданное состояние сущности с текущим persistence context
     * с помощью {@link Session#merge(Object)} и фиксирует транзакцию.
     * </p>
     *
     * <p>
     * Возвращаемый объект может отличаться от переданного объекта {@code user},
     * так как {@code merge} возвращает управляемую Hibernate-сущность.
     * </p>
     *
     * <p>
     * В случае ошибки транзакция откатывается, ошибка логируется,
     * а исключение пробрасывается дальше.
     * </p>
     *
     * @param user пользователь с обновленными данными.
     *             Не должен быть {@code null}.
     * @return обновленная управляемая сущность пользователя.
     * @throws RuntimeException если произошла ошибка при обновлении пользователя.
     */
    @Override
    public User update(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                User merged = session.merge(user);
                tx.commit();
                logger.info("User updated: id={}", merged.getId());
                return merged;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to update user", e);
                throw e;
            }
        }
    }

    /**
     * Удаляет пользователя из базы данных по идентификатору.
     *
     * <p>
     * Метод открывает новую Hibernate-сессию, начинает транзакцию,
     * ищет пользователя по ID с помощью {@link Session#get(Class, Object)}.
     * Если пользователь найден, он удаляется с помощью
     * {@link Session#remove(Object)}, после чего транзакция фиксируется.
     * </p>
     *
     * <p>
     * Если пользователь с указанным идентификатором не найден, метод возвращает
     * {@code false}.
     * </p>
     *
     * <p>
     * В случае ошибки транзакция откатывается, ошибка логируется,
     * а исключение пробрасывается дальше.
     * </p>
     *
     * @param id идентификатор пользователя, которого необходимо удалить.
     *           Не должен быть {@code null}.
     * @return {@code true}, если пользователь был найден и удален;
     *         {@code false}, если пользователь с указанным ID не существует.
     * @throws RuntimeException если произошла ошибка при удалении пользователя.
     */
    @Override
    public boolean deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user == null) {
                    logger.warn("Attempt to delete non-existing user id={}", id);
                    return false;
                }
                session.remove(user);
                tx.commit();
                logger.info("User deleted: id={}", id);
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Failed to delete user id={}", id, e);
                throw e;
            }
        }
    }

    /**
     * Ищет пользователя в базе данных по email.
     *
     * <p>
     * Для поиска используется HQL-запрос:
     * {@code SELECT u FROM User u WHERE u.email = :email}.
     * Метод ограничивает результат одним значением с помощью
     * {@code setMaxResults(1)}.
     * </p>
     *
     * <p>
     * Если пользователь с указанным email найден, возвращается {@link Optional}
     * с пользователем. Если пользователь отсутствует, возвращается
     * {@link Optional#empty()}.
     * </p>
     *
     * @param email email пользователя.
     *              Не должен быть {@code null}.
     * @return {@link Optional}, содержащий найденного пользователя,
     *         или пустой {@link Optional}, если пользователь не найден.
     * @throws RuntimeException если произошла ошибка при поиске пользователя по email.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        }
    }
}

