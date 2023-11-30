package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends BaseEntity {
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;

    private String status = "open";
    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, fetch =  FetchType.LAZY)
    private Set<CartItems> cartItems;
}
