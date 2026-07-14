package org.prilepskiy.aston_step_two.service;

import org.prilepskiy.aston_step_two.dao.UserDao;
import org.prilepskiy.aston_step_two.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService}, содержащая бизнес-логику
 * для работы с пользователями.
 *
 * <p>
 * Класс является сервисным слоем приложения и использует {@link UserDao}
 * для выполнения операций доступа к данным. В отличие от DAO-слоя,
 * данный класс отвечает за проверки бизнес-правил перед сохранением,
 * обновлением или удалением пользователей.
 * </p>
 *
 * <p>
 * Основные задачи класса:
 * проверка уникальности email, создание пользователей, получение пользователей
 * по идентификатору, получение списка всех пользователей, обновление данных
 * пользователя, удаление пользователя и проверка существования email.
 * </p>
 *
 * <p>
 * При нарушении бизнес-правил методы выбрасывают
 * {@link IllegalArgumentException}.
 * </p>
 */
public class UserServiceImpl implements UserService {
    /**
     * Логгер для записи информации о работе сервисного слоя,
     * включая сообщения о нарушении бизнес-правил.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * DAO-объект для выполнения операций с пользователями в базе данных.
     */
    private final UserDao userDao;

    /**
     * Создает экземпляр сервиса пользователей с указанным DAO.
     *
     * <p>
     * Через переданный {@link UserDao} сервис получает доступ к операциям
     * сохранения, поиска, обновления и удаления пользователей.
     * </p>
     *
     * @param userDao DAO для работы с пользователями.
     *                Не должен быть {@code null}.
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Создает нового пользователя.
     *
     * <p>
     * Перед созданием пользователя метод проверяет, существует ли уже пользователь
     * с указанным email. Если такой пользователь найден, создание не выполняется,
     * логируется информационное сообщение и выбрасывается
     * {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * Если email свободен, создается новый объект {@link User}, после чего он
     * сохраняется через {@link UserDao#save(User)}.
     * </p>
     *
     * @param name имя пользователя.
     *             Не должно быть {@code null} или пустым.
     * @param email email пользователя.
     *              Должен быть уникальным.
     * @param age возраст пользователя.
     *            Должен соответствовать бизнес-правилам приложения.
     * @return созданный и сохраненный пользователь.
     * @throws IllegalArgumentException если пользователь с таким email уже существует.
     */
    @Override
    public User createUser(String name, String email, int age) {
        if (userDao.findByEmail(email).isPresent()) {
            logger.info("Пользователь с таким email уже существует");
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + email);
        }
        User user = new User(name, email, age);
        return userDao.save(user);
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * <p>
     * Метод делегирует поиск DAO-слою через {@link UserDao#findById(Long)}.
     * Если пользователь найден, возвращается {@link Optional} с объектом
     * пользователя. Если пользователь отсутствует, возвращается пустой
     * {@link Optional}.
     * </p>
     *
     * @param id идентификатор пользователя.
     *           Не должен быть {@code null}.
     * @return {@link Optional}, содержащий найденного пользователя,
     *         или {@link Optional#empty()}, если пользователь не найден.
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * <p>
     * Метод получает данные из DAO-слоя через {@link UserDao#findAll()}.
     * Если в базе данных нет пользователей, возвращается пустой список.
     * </p>
     *
     * @return список всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }


    /**
     * Обновляет данные существующего пользователя.
     *
     * <p>
     * Сначала метод ищет пользователя по переданному идентификатору.
     * Если пользователь не найден, обновление не выполняется, логируется
     * информационное сообщение и выбрасывается {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * Далее выполняется проверка уникальности email. Если новый email отличается
     * от текущего email пользователя и уже принадлежит другому пользователю,
     * обновление не выполняется и выбрасывается {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * Если проверки пройдены успешно, метод обновляет имя, email и возраст
     * пользователя, после чего сохраняет изменения через
     * {@link UserDao#update(User)}.
     * </p>
     *
     * @param id идентификатор пользователя, которого необходимо обновить.
     * @param name новое имя пользователя.
     * @param email новый email пользователя.
     * @param age новый возраст пользователя.
     * @return обновленный пользователь.
     * @throws IllegalArgumentException если пользователь с указанным ID не найден
     *                                  или если указанный email уже используется.
     */
    @Override
    public User updateUser(Long id, String name, String email, int age) {
        Optional<User> existingOpt = userDao.findById(id);
        if (existingOpt.isEmpty()) {
            logger.info("Пользователь не найден по ID");
            throw new IllegalArgumentException("Пользователь не найден по ID: " + id);
        }

        User existing = existingOpt.get();

        if (!existing.getEmail().equals(email) && userDao.findByEmail(email).isPresent()) {
            logger.info("Пользователь с таким email уже существует");
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + email);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setAge(age);

        return userDao.update(existing);
    }


    /**
     * Удаляет пользователя по идентификатору.
     *
     * <p>
     * Метод делегирует удаление DAO-слою через
     * {@link UserDao#deleteById(Long)}.
     * </p>
     *
     * @param id идентификатор пользователя, которого необходимо удалить.
     * @return {@code true}, если пользователь был найден и удален;
     *         {@code false}, если пользователь с указанным ID не найден.
     */
    @Override
    public boolean deleteUserById(Long id) {
        return userDao.deleteById(id);
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * <p>
     * Метод выполняет поиск пользователя по email через
     * {@link UserDao#findByEmail(String)} и возвращает результат проверки.
     * </p>
     *
     * @param email email, наличие которого необходимо проверить.
     * @return {@code true}, если пользователь с таким email существует;
     *         {@code false}, если пользователь с таким email не найден.
     */
    @Override
    public boolean isEmailExists(String email) {
        return userDao.findByEmail(email).isPresent();
    }
}
