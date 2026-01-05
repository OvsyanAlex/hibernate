package entity;

import lombok.*;
import util.Birthday;
import util.BirthdayConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class PersonalInfo {
    private String first_name;

    private String last_name;

    @Column(name = "birth_date")
    @Convert(converter = BirthdayConverter.class)
    private Birthday birth_date;
}
