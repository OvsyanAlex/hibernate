package util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@UtilityClass //final class + private constructor
public class HibernateUtil {
    public static SessionFactory createSessionFactory() {
        // Создание реестра сервисов
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // Hibernate ищет hibernate.cfg.xml в classpath
                .build();

        // Создаем MetadataSources
        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(entity.User.class) // регистрация Entity
                .addAnnotatedClass(entity.Company.class)
                .addAnnotatedClass(entity.Profile.class)
                .addAnnotatedClass(entity.Chat.class);

        // Создаем MetadataBuilder
        Metadata metadata = sources.getMetadataBuilder()
                // .applyBasicType(new JsonBinaryType()) регистрация кастомного типа
                .build();

        // Строим SessionFactory
        return metadata.buildSessionFactory();

        // альтернатива
        //  Configuration configuration = new Configuration();
        //  configuration.registerTypeOverride(new JsonBinaryType()); //подключение библиотеки для конвертации JSON
        // configuration.configure(); //конфигурирование через hibernate.cfg.xml - путь не указывается, если он лежит не в рутовой дериктории
        // return configuration.buildSessionFactory(); // создание sessionFactory
    }
}
