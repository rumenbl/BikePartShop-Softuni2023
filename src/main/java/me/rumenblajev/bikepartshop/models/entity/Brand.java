package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand extends BaseEntity {
    @Column(nullable = false)
    @Size(min = 3, message = "Brand name must be at least 3 characters")
    private String brandName;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<BikePart> bikeParts;

    @Override
    public String toString() {
        return brandName;
    }
}
