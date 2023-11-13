package me.rumenblajev.bikepartshop.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="categories")
@Getter
@Setter
public class BikePartCategory extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BikePartCategoryEnum name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER)
    private Set<BikePart> part;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BikePartCategory category = (BikePartCategory) o;
        return Objects.equals(part, category.part);
    }
}
