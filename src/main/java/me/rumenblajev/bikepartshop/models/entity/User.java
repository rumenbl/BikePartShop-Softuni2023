package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.GenderEnum;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne
    private Role role;

    @OneToMany(cascade = CascadeType.REMOVE)
    private Collection<Cart> cart;

    @Override
    public String toString() {
        return String.format("Name: %s %s | Username: %s | Phone: %s | Email: %s",
                            firstName,lastName,username,phoneNumber,email);
    }
}
