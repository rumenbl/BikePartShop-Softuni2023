package me.rumenblajev.bikepartshop.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.models.entity.Role;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private Integer age;

    private String email;

    private GenderEnum gender;

    private String phoneNumber;

    private Role role;
}
