package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery extends BaseEntity {
    @Column(nullable = false)
    private String person;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false,name = "postal_code")
    private String postalCode;

    @Column(nullable = false)
    private String courier;


    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @OneToMany
    private List<Order> orders;

    public Delivery() {
    }
}
