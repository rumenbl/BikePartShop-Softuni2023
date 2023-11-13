package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="bike_parts")
@Getter
@Setter
public class BikePart extends BaseEntity {
    private String title;
    private String description;
    @ManyToOne
    private BikePartCategory category;
    private String pictureUrl;
    private Double price;
    @ManyToOne
    private Brand brand;
    private Integer stock;
    @OneToMany(mappedBy = "part",cascade = CascadeType.ALL)
    private Set<CartItems> parts;
}
