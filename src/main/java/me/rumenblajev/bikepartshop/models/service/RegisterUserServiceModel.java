package me.rumenblajev.bikepartshop.models.service;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.Role;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class RegisterUserServiceModel {
    private String username;

    private String firstName;

    private String lastName;

    private Integer age;

    private String email;

    private String password;

    private GenderEnum gender;

    private String phoneNumber;

    private Role role;

    private List<Cart> carts;
}
