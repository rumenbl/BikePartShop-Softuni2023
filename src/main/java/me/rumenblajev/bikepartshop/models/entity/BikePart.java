package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private BikePartCategory category;
    private String pictureUrl;
    private Double price;
    private Brand brand;
    private Integer stock;
    @OneToMany(mappedBy = "bike_part",cascade = CascadeType.ALL)
    private Set<CartItems> parts;
}
