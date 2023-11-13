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
    @ManyToOne
    private User user;

    private String status = "open";
    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private Set<CartItems> cartItems;
}
