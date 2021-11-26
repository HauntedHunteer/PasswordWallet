package com.niemczuk.passwordwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {


    @NotBlank(message = "User's name cannot be empty")
    @Size(min = 5, max = 250)
    private String login;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16)
    private String password;

    private boolean isPasswordKeptAsHash;

}
