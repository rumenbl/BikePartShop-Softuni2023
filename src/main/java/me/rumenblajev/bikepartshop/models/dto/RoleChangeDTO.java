package me.rumenblajev.bikepartshop.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.view.UserViewModel;

@NoArgsConstructor
@Getter
@Setter
public class RoleChangeDTO {
    private RolesEnum role;
    private Long userId;
}
