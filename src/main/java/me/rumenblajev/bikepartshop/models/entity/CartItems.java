package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
public class CartItems {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "part_id")
    private BikePart part;


    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItems() {

    }

    public CartItems(BikePart part, Cart cart) {
        this.part = part;
        this.cart = cart;
    }

    @Getter
    @Setter
    private Integer amount = 1;
    private final String status = "pending";

}
