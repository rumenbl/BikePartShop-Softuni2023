package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<CartItems> items;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false,name = "total_value")
    private Double totalValue;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User client;
}
