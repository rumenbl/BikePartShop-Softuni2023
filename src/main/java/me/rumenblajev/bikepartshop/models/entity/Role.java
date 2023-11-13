package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.RolesEnum;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private RolesEnum name;
    @OneToMany(mappedBy = "role")
    private List<User> users;
}
