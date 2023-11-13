package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<CartItems> items;
    @ManyToOne
    private Delivery delivery;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false,name = "total_value")
    private Double totalValue;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User client;
}
