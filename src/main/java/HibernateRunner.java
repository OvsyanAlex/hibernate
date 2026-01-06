import entity.*;
import util.Birthday;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static util.HibernateUtil.createSessionFactory;

public class HibernateRunner {
    public static void main(String[] args) {

        try (SessionFactory sessionFactory = createSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                /** простые действия без связынных сущностей*/

                // получение persistent и изменения поля, Hibernate отслеживает dirty checking
                User user = session.get(User.class,3L);
                user.setUser_name("new_name");
                // Загружает proxy, бросает ObjectNotFoundException, если объект отсутствует при доступе к нему
                // используется для lazy-загрузки
                session.load(User.class, 3L);

                // сначала происходит select для проверки наличия указанного объекта по id, удаляет объект из БД, persistent -> transient
                // Object может быть в любом статусе, главное, чтобы был установлен Id
                // получение persistent и удаление компании со всеми пользаками(orphanRemoval=true), Hibernate отслеживает dirty checking
                // Сначала удаляются users, потом company
                Company company = session.get(Company.class, 10);
                session.delete(company);

                // применяются только к transient → превращают в persistent, в кеш 1 уровня
                // в зависимости от GenerationType сначала может происходить запрос для присвоения id, потом insert
                // возвращает идентификатор
                Serializable save = session.save(user);
                // Тоже делает transient → persistent, но не возвращает id, строго следует JPA спецификации
                session.persist(user);


                // Поиск сущности с таким же id:
                // 1. Если сущность уже есть в persistence context — обновляет её поля
                // 2. Если нет — загружает из БД в persistence context — обновляет её поля
                // 3. Если в БД тоже нет — создаёт новый экземпляр и сохраняет его
                // Если entity.id == null → всегда INSERT
                // Если entity.id != null, но в БД нет такой строки → INSERT
                // Возвращает persistent-объект (новый или обновлённый)
                // Переданный объект остаётся detached
                // session.merge(user);

                // объект должен быть persistent, выполняет SELECT из базы по указанному id поля перезаписываются свежими значениями из базы
                // несохранённые локальные изменения в этом объекте теряются, объект остается persistent
                // session.refresh(user);

                // сначала происходит select для проверки наличия указанного объекта по id в сессии, потом update, иначе Exception
                // нельзя вызывать update() на persistent или transient объекте, работает только с detached
                // session.update(user);

                // сначала происходит select для проверки наличия указанного объекта по id, потом update или insert
                // если transient → вызывает save → persistent
                // если detached → вызывает update → persistent
                // session.saveOrUpdate(user);

                // session.isDirty(); есть ли в сессии объекты, которые были изменены, но ещё не отправлены в базу данных, не flush()
                // session.flush(); сбрасываем все изменения (добавленные, изменённые, удалённые) в БД, транзакция не коммитится, кеш сохраняется, после flush() новый объект получает id, если он генерируется БД, можно использовать внутри текущей транзакции
                // session.evict(user);  удаляем сущность из кеша первого уровня
                // session.clear();  очищаем кеш первого уровня
                // session.close(); закрываем сессию -> очищаем кеш первого уровня

                /** создание Entity */
//                Company company = Company.builder()
//                        .name("Byratino_3")
//                        .build();

//                User user = User.builder()
//                        .user_name("ivan@mail.ru")
//                        .personalInfo(PersonalInfo.builder()
//                                .first_name("Ivan123")
//                                .last_name("Ivanov")
//                                .birth_date(new Birthday(LocalDate.of(2000, 1, 19))).build())
//                        .role(Role.USER)
//                        .info("""
//                                {"name":"Ivan",
//                                "id":25}
//                                """)
//                        .company(company)
//                        .build();
//                Profile profile = Profile.builder()
//                        .street("Lenina")
//                        .language("RU")
//                        .build();

                /** двусторонняя OneToMany, с CascadeType.ALL*/
                // синхронизированное сохранение нового пользователя и компании
                // save() предназначен для transient сущностей - новых сущностей - используем его
//                company.addUser(user);
//                session.save(company);

                // добавление нового пользователя к уже существующей компании
                // company1 уже persistent, save() предназначен для transient сущностей
//                Company company1 = session.get(Company.class, 10);
//                company1.addUser(user);

                // получить компанию и всех пользователей c избеганием N + 1
//                Company company = session.createQuery("SELECT c FROM Company c " +
//                                                      "LEFT JOIN FETCH c.users " +
//                                                      "WHERE c.id = :id", Company.class)
//                        .setParameter("id", 10)
//                        .getSingleResult();


                /** односторонняя ManyToOne */
                // синхронизированное сохранение нового пользователя и компании
//                session.save(company);
//                session.save(user);

                // получение пользователей компании - односторонняя ManyToOne
//                Company companyById = session.get(Company.class, 10);
//                List<User> users = session.createQuery("SELECT u FROM User u WHERE u.company = :company", User.class)
//                        .setParameter("company", companyById)
//                        .getResultList();




//                User user = session.get(User.class, 4L);
//                Chat chat = new Chat();
//                chat.setName("OrmTalking");
//                user.addChat(chat);
//                session.save(chat);
//                session.find(User.class, 1L,LockModeType.OPTIMISTIC);
//                session.lock
//                User user = session.get(User.class, 4L); к существующему user добавляем profile
//                profile.setUser(user);
//                session.save(profile);

//                company.addUser(user); добавление company c user в бд
//

                // Company company1 = session.get(Company.class,4);
                // session.delete(company1);  удаление company cо всеми user в бд, каскадное удаление лучше задавать через SQL, выше перфоманс


                // Для связи с другими сущностями (foreign key), не делает SELECT сразу, возвращает ленивый прокси
                // бросает исключение, если объекта нет, используется для экономии запросов, особенно в связях
                //  User user = session.load(User.class, 4L); // прокси
                //  profile.setUser(user);
                //  session.save(profile);

//                user.setRole(Role.ADMIN); // Hibernate будет делать update в рамках транзакции

//                User user_2 = session.get(User.class, "ivan3@mail.ru"); // происходит селект, класс нужен для получения java объекта
//                user_2.setLastName("Petrov"); // dirty session - мы меняем состоянии у закешированного entity, которое зафлашится (= вызову session.flush()) в БД без нашего вызова update в момен вызова session.getTransaction().commit();
//                User user_3 = session.get(User.class, "ivan3@mail.ru"); // методы save(), get() и др выполняя select хранят найденный объект в кеше первого уровня, при повторном выполнении get запроса с одинаковым id объект будет получен из persistenceContext, кеш хранится в рамках сессии
//
                // используется при репликации между базами данных, когда нужно:
                //вставить объект в целевую БД, если его там нет
                //обновить объект в целевой БД, если он отличается от того, что есть
//                Hibernate не делает обычный save/update, а ориентируется на ReplicationMode.
//                IGNORE	Не обновлять объект, если он уже существует в БД
//                OVERWRITE	Перезаписать объект в базе независимо от того, есть он или нет
//                LATEST_VERSION	Обновить объект только если версия пришедшего объекта выше
//                EXCEPTION	Если объект существует — бросить исключение
//                session.replicate(entity, replicationMode);
                session.getTransaction().commit(); // Отправляет изменения и завершает транзакцию, делает её постоянной
            }
        }
    }
}
