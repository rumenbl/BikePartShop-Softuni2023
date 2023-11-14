package me.rumenblajev.bikepartshop.models.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.GenderEnum;
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDTO {
    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 4,max = 20,message = "Username must be between 4 and 20 characters!")
    private String username;

    @NotBlank(message = "First name cannot be empty!")
    @Size(min = 4,max = 20,message = "First name must be between 4 and 20 characters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty!")
    @Size(min = 4,max = 20,message = "Last name must be between 4 and 20 characters!")
    private String lastName;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 0, message = "Age must be over 0")
    @Max(value = 99, message = "Age must be under 100")
    private Integer age;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Invalid email!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 4,message = "Password must contain at least 4 characters!")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty!")
    @Size(min = 4,message = "Confirm Password must contain at least 4 characters!")
    private String confirmPassword;


    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please select your gender!")
    private GenderEnum gender;
}
