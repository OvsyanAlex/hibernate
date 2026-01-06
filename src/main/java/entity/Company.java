package entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "company", schema = "hibernate")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    // Двунаправленная ManyToOne
    // связывает с полем company в классе User
    // CascadeType.ALL - все операции (persist, merge, remove) на компании будут применяться к её пользователям
    // по умолчанию @OneToMany = LAZY
    // @OneToMany НЕ управляет связью
    // orphanRemoval - для @OneToMany, @OneToOne. Если у дочерней сущности удалена ссылка на родителя — его нужно удалить
    // однонаправленная ManyToOne исключает использование данного поля - связь остается только в User
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // @Builder.Default → new ArrayList<>() → иначе Builder обнуляет поле List<User> users = null;
    private List<User> users = new ArrayList<>();

    // добавление пользака с двунаправленной ManyToOne
    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }

    // удаление пользака с двунаправленной ManyToOne
    public void removeUser(User user){
        users.remove(user);
        user.setCompany(null);
    }
}
