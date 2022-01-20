package com.niemczuk.passwordwallet.dto;

import com.niemczuk.passwordwallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedPasswordForUserDto {

    private String login;

    private String password;

    private String webAddress;

    private String description;

    private User owner;
}
