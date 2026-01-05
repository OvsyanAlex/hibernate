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
@Table(name = "company",schema = "hibernate")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Builder.Default // для нужен для полей с дефолтным значением, чтобы Builder их не обнулял
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    public void addUser(User user){ // для нужен для сохранения сущностей user
        users.add(user);
        user.setCompany(this);
    }
}
