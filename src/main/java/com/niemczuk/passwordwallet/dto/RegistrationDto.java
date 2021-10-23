package com.niemczuk.passwordwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private boolean isPasswordKeptAsHash;

}
