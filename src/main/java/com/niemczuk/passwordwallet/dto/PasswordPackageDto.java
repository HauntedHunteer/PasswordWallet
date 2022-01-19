package com.niemczuk.passwordwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordPackageDto {

    private UUID id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String webAddress;

    private String description;
}
