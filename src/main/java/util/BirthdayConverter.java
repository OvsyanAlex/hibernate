package util;



import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.util.Optional;
/**в данном примере класс Birthday заменяет LocalDate, на сомом деле Hibernate может и сам конвертировать LocalDate в Date*/
public class BirthdayConverter implements AttributeConverter<Birthday, Date> {
    @Override
    public Date convertToDatabaseColumn(Birthday attribute) {
        return Optional.ofNullable(attribute)
                .map(Birthday::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(Date::toLocalDate)
                .map(Birthday::new)
                .orElse(null);
    }
}
