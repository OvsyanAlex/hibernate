package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class Birthday {
    private final LocalDate birthDate;

    public Birthday(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public long getAge() {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    public LocalDate birthDate() {
        return birthDate;
    }
}
