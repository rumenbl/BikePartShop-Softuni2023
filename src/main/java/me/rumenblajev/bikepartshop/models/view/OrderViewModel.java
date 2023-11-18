package me.rumenblajev.bikepartshop.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderViewModel {
    private Long id;
    private List<CartItems> items;
    private LocalDate date;
    private Double totalValue;
    private User client;
}
